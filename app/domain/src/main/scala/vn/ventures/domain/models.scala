package vn.ventures.domain

final case class ItemId(value: Long)

final case class ItemData(name: String, price: BigDecimal)

final case class Item(
    id: Long,
    name: String,
    price: BigDecimal
) {
  def data: ItemData =
    ItemData(name, price)
}

object Item {
  def withData(id: Long, data: ItemData): Item = Item(
    id,
    data.name,
    data.price
  )
}
