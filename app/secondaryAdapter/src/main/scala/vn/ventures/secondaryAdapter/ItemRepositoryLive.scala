package vn.ventures.secondaryAdapter

import io.getquill.*
import io.getquill.jdbczio.Quill
import vn.ventures.domain.*
import zio.*

import java.sql.SQLException

final class ItemRepositoryLive(quill: Quill.Mysql[SnakeCase]) extends ItemRepository {

  import quill.*

  inline def items: Quoted[EntityQuery[Item]] = quote {
    querySchema[Item]("items")
  }

  override def add(data: ItemData): IO[RepositoryError, ItemId] = {
    val effect: IO[SQLException, ItemId] = run {
      quote {
        items
          .insertValue(lift(Item.withData(ItemId(0), data)))
          .returningGenerated(_.id)
      }
    }

    effect.either.resurrect
      .refineOrDie { case e: NullPointerException =>
        RepositoryError(e)
      }
      .flatMap {
        case Left(e: SQLException) => ZIO.fail(RepositoryError(e))
        case Right(itemId: ItemId) => ZIO.succeed(itemId)
      }
  }

  override def delete(id: ItemId): IO[RepositoryError, Long] = {
    val effect: IO[SQLException, Long] = run {
      quote {
        items.filter(i => i.id == lift(id)).delete
      }
    }

    effect.refineOrDie { case e: SQLException =>
      RepositoryError(e)
    }
  }

  override def getAll: IO[RepositoryError, List[Item]] = {
    val effect: IO[SQLException, List[Item]] = run {
      quote {
        items
      }
    }

    effect.refineOrDie { case e: SQLException =>
      RepositoryError(e)
    }
  }

  override def getById(id: ItemId): IO[RepositoryError, Option[Item]] = {
    val effect: IO[SQLException, List[Item]] = run {
      quote {
        items.filter(_.id == lift(id))
      }
    }

    effect
      .map(_.headOption)
      .refineOrDie { case e: SQLException =>
        RepositoryError(e)
      }
  }

  override def update(itemId: ItemId, data: ItemData): IO[RepositoryError, Option[Unit]] = {
    val effect: IO[SQLException, Long] = run {
      quote {
        items
          .filter(item => item.id == lift(itemId))
          .updateValue(lift(Item.withData(itemId, data)))
      }
    }

    effect
      .map(n => if (n > 0) Some(()) else None)
      .refineOrDie { case e: SQLException =>
        RepositoryError(e)
      }
  }
}

object ItemRepositoryLive {

  val layer: URLayer[Quill.Mysql[SnakeCase], ItemRepository] = ZLayer {
    for {
      quill <- ZIO.service[Quill.Mysql[SnakeCase]]
    } yield ItemRepositoryLive(quill)
  }
}
