package vn.ventures.secondaryAdapter

import io.getquill.*
import io.getquill.jdbczio.Quill
import vn.ventures.domain.*
import zio.{IO, URLayer, ZIO, ZLayer}

import java.sql.SQLException

final class ItemRepositoryLive(quill: Quill.Postgres[Literal]) extends ItemRepository:

  import quill.*

  inline def items: Quoted[EntityQuery[Item]] = quote {
    querySchema[Item]("items")
  }

  override def add(data: ItemData): IO[RepositoryError, Long] =
    val effect: IO[SQLException, Long] = run {
      quote {
        items
          .insertValue(lift(Item.withData(0, data)))
          .returningGenerated(_.id)
      }
    }

    effect
      .either
      .resurrect
      .refineOrDie {
        case e: NullPointerException => RepositoryError(e)
      }
      .flatMap {
        case Left(e: SQLException) => ZIO.fail(RepositoryError(e))
        case Right(itemId: Long) => ZIO.succeed(itemId)
      }

  override def delete(id: Long): IO[RepositoryError, Long] =
    val effect: IO[SQLException, Long] = run {
      quote {
        items.filter(i => i.id == lift(id)).delete
      }
    }

    effect.refineOrDie {
      case e: SQLException => RepositoryError(e)
    }

  override def getAll: IO[RepositoryError, List[Item]] =
    val effect: IO[SQLException, List[Item]] = run {
      quote {
        items
      }
    }

    effect.refineOrDie {
      case e: SQLException => RepositoryError(e)
    }

  override def getById(id: Long): IO[RepositoryError, Option[Item]] =
    val effect: IO[SQLException, List[Item]] = run {
      quote {
        items.filter(_.id == lift(id))
      }
    }

    effect
      .map(_.headOption)
      .refineOrDie {
        case e: SQLException => RepositoryError(e)
      }

  override def update(itemId: Long, data: ItemData): IO[RepositoryError, Option[Unit]] =
    val effect: IO[SQLException, Long] = run {
      quote {
        items
          .filter(item => item.id == lift(itemId))
          .updateValue(lift(Item.withData(itemId, data)))
      }
    }

    effect
      .map(n => if (n > 0) Some(()) else None)
      .refineOrDie {
        case e: SQLException => RepositoryError(e)
      }

object ItemRepositoryLive:

  val layer: URLayer[Quill.Postgres[Literal], ItemRepository] = ZLayer {
    for {
      quill <- ZIO.service[Quill.Postgres[Literal]]
    } yield ItemRepositoryLive(quill)
  }
