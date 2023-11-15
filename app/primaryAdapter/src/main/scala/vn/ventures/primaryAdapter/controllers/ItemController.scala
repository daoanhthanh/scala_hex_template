package vn.ventures.primaryAdapter.controllers

import vn.ventures.primaryAdapter.utils.Extensions.*
import vn.ventures.domain.*
import vn.ventures.primaryAdapter.utils.Utils
import zio.*
import zio.http.*
import zio.json.*

object ItemController extends JsonSupport {
  val routes: HttpApp[ItemRepository, Nothing] = Http.collectZIO {
    case Method.GET -> !! / "items" =>
      val effect: ZIO[ItemRepository, DomainError, List[Item]] =
        ItemService.getAllItems

      effect.foldZIO(Utils.handleError, _.toResponseZIO)

    case Method.GET -> !! / "items" / itemId =>
      val effect: ZIO[ItemRepository, DomainError, Item] =
        for {
          id        <- Utils.extractLong(itemId)
          maybeItem <- ItemService.getItemById(id)
          item <- maybeItem
            .map(ZIO.succeed(_))
            .getOrElse(ZIO.fail(NotFoundError))
        } yield item

      effect.foldZIO(Utils.handleError, _.toResponseZIO)

    case Method.DELETE -> !! / "items" / itemId =>
      val effect: ZIO[ItemRepository, DomainError, Unit] =
        for {
          id     <- Utils.extractLong(itemId)
          amount <- ItemService.deleteItem(id)
          _ <-
            if (amount == 0) ZIO.fail(NotFoundError)
            else ZIO.unit
        } yield ()

      effect.foldZIO(Utils.handleError, _.toEmptyResponseZIO)

    case req @ Method.POST -> !! / "items" =>
      val effect: ZIO[ItemRepository, DomainError, Item] =
        for {
          createItem <- req.jsonBodyAs[CreateItemRequest]
          itemId     <- ItemService.addItem(createItem.name, createItem.price)
        } yield Item(itemId, createItem.name, createItem.price)

      effect.foldZIO(Utils.handleError, _.toResponseZIO(Status.Created))

    case req @ Method.PUT -> !! / "items" / itemId =>
      val effect: ZIO[ItemRepository, DomainError, Item] =
        for {
          id         <- Utils.extractLong(itemId)
          updateItem <- req.jsonBodyAs[UpdateItemRequest]
          maybeItem  <- ItemService.updateItem(id, updateItem.name, updateItem.price)
          item <- maybeItem
            .map(ZIO.succeed(_))
            .getOrElse(ZIO.fail(NotFoundError))
        } yield item

      effect.foldZIO(Utils.handleError, _.toResponseZIO)

    case req @ Method.PATCH -> !! / "items" / itemId =>
      val effect: ZIO[ItemRepository, DomainError, Item] =
        for {
          id                <- Utils.extractLong(itemId)
          partialUpdateItem <- req.jsonBodyAs[PartialUpdateItemRequest]
          maybeItem <- ItemService.partialUpdateItem(
            id = id,
            name = partialUpdateItem.name,
            price = partialUpdateItem.price
          )
          item <- maybeItem
            .map(ZIO.succeed(_))
            .getOrElse(ZIO.fail(NotFoundError))
        } yield item

      effect.foldZIO(Utils.handleError, _.toResponseZIO)

  }

}
