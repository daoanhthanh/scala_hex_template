package vn.ventures.primaryAdapter.controllers

import vn.ventures.primaryAdapter.utils.Extensions.*
import vn.ventures.domain.*
import vn.ventures.primaryAdapter.forms.*
import vn.ventures.primaryAdapter.utils.{Extractor, Utils}
import zio.*
import zio.http.*
import zio.json.*

object ItemController extends JsonSupport {
  val routes: HttpApp[ItemRepository, Nothing] = Http.collectZIO {
    case Method.GET -> _ / "items" =>
      val effect: ZIO[ItemRepository, DomainError, List[Item]] =
        ItemService.getAllItems

      effect.foldZIO(Utils.handleError, _.toResponse)

    case Method.GET -> _ / "items" / itemId =>
      val effect: ZIO[ItemRepository, DomainError, Item] =
        for {
          id        <- Extractor.extractLong(itemId)
          maybeItem <- ItemService.getItemById(ItemId(id))
          item <- maybeItem
            .map(ZIO.succeed(_))
            .getOrElse(ZIO.fail(NotFoundError))
        } yield item

      effect.foldZIO(Utils.handleError, _.toResponse)

    case Method.DELETE -> _ / "items" / itemId =>
      val effect: ZIO[ItemRepository, DomainError, Unit] =
        for {
          id     <- Extractor.extractLong(itemId)
          amount <- ItemService.deleteItem(ItemId(id))
          _ <-
            if (amount == 0) ZIO.fail(NotFoundError)
            else ZIO.unit
        } yield ()

      effect.foldZIO(Utils.handleError, _.toEmptyResponse)

    case req @ Method.POST -> _ / "items" =>
      val effect: ZIO[ItemRepository, DomainError, Item] =
        for {
          createItem <- req.jsonBodyAs[CreateItemRequest]
          itemId     <- ItemService.addItem(createItem.name, createItem.price)
        } yield Item(itemId, createItem.name, createItem.price)

      effect.foldZIO(Utils.handleError, _.toResponse(Status.Created))

    case req @ Method.PUT -> _ / "items" / itemId =>
      val effect: ZIO[ItemRepository, DomainError, Item] =
        for {
          id         <- Extractor.extractLong(itemId)
          updateItem <- req.jsonBodyAs[UpdateItemRequest]
          maybeItem  <- ItemService.updateItem(ItemId(id), updateItem.name, updateItem.price)
          item <- maybeItem
            .map(ZIO.succeed(_))
            .getOrElse(ZIO.fail(NotFoundError))
        } yield item

      effect.foldZIO(Utils.handleError, _.toResponse)

    case req @ Method.PATCH -> _ / "items" / itemId =>
      val effect: ZIO[ItemRepository, DomainError, Item] =
        for {
          id                <- Extractor.extractLong(itemId)
          partialUpdateItem <- req.jsonBodyAs[PartialUpdateItemRequest]
          maybeItem <- ItemService.partialUpdateItem(
            id = ItemId(id),
            name = partialUpdateItem.name,
            price = partialUpdateItem.price
          )
          item <- maybeItem
            .map(ZIO.succeed(_))
            .getOrElse(ZIO.fail(NotFoundError))
        } yield item

      effect.foldZIO(Utils.handleError, _.toResponse)

  }

}
