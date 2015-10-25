package models

import java.sql.Timestamp
import slick.driver.PostgresDriver.api._

case class Transaction(
  id: Id[Transaction],
  detail: String,
  value: Float,
  date: Timestamp
)

class Transactions(tag: Tag) extends Table[Transaction](tag, "transactions") {
  def id = column[Id[Transaction]]("transaction_id", O.PrimaryKey)
  def detail = column[String]("transaction_detail")
  def value = column[Float]("transaction_value")
  def date = column[Timestamp]("transaction_date")
  def * = (id, detail, value, date) <> (Transaction.tupled, Transaction.unapply)
}