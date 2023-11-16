package vn.ventures.primaryAdapter.controllers

import vn.ventures.domain.authentication.AuthenticatedEntity
import vn.ventures.domain.{Item, ItemId}
import vn.ventures.primaryAdapter.forms.*
import zio.json.*

trait JsonSupport {
  implicit val itemIdEncoder: JsonEncoder[ItemId] = JsonEncoder[Long].contramap(_.value)
  implicit val itemEncoder: JsonEncoder[Item]     = DeriveJsonEncoder.gen[Item]

  implicit val updateItemDecoder: JsonDecoder[UpdateItemRequest] = DeriveJsonDecoder.gen[UpdateItemRequest]

  implicit val loginDecoder: JsonDecoder[LoginRequest]                = DeriveJsonDecoder.gen[LoginRequest]
  implicit val loginResponseEncoder: JsonEncoder[AuthenticatedEntity] = DeriveJsonEncoder.gen[AuthenticatedEntity]

  implicit val partialUpdateItemDecoder: JsonDecoder[PartialUpdateItemRequest] =
    DeriveJsonDecoder.gen[PartialUpdateItemRequest]

  implicit val createItemDecoder: JsonDecoder[CreateItemRequest] = DeriveJsonDecoder.gen[CreateItemRequest]
}

object JsonSupport extends JsonSupport
