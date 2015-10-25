package models

import slick.lifted.MappedTo

case class Id[+M](id: Long) extends AnyVal with MappedTo[Long] {
  override def value = id
}