package vn.ventures.domain

import zio._

trait ItemRepository:

  def add(data: ItemData): IO[RepositoryError, Long]

  def delete(id: Long): IO[RepositoryError, Long]

  def getAll: IO[RepositoryError, List[Item]]

  def getById(id: Long): IO[RepositoryError, Option[Item]]

  def update(itemId: Long, data: ItemData): IO[RepositoryError, Option[Unit]]

object ItemRepository:

  def add(data: ItemData): ZIO[ItemRepository, RepositoryError, Long] =
    ZIO.serviceWithZIO[ItemRepository](_.add(data))

  def delete(id: Long): ZIO[ItemRepository, RepositoryError, Long] =
    ZIO.serviceWithZIO[ItemRepository](_.delete(id))

  def getAll: ZIO[ItemRepository, RepositoryError, List[Item]] =
    ZIO.serviceWithZIO[ItemRepository](_.getAll)

  def getById(id: Long): ZIO[ItemRepository, RepositoryError, Option[Item]] =
    ZIO.serviceWithZIO[ItemRepository](_.getById(id))

  def update(itemId: Long, data: ItemData): ZIO[ItemRepository, RepositoryError, Option[Unit]] =
    ZIO.serviceWithZIO[ItemRepository](_.update(itemId, data))
