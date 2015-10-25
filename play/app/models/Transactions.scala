package models

import java.sql.Timestamp
import scala.concurrent.Future
import slick.driver.PostgresDriver.api._
import play.api.libs.concurrent.Execution.Implicits._

case class Transaction(
  detail: String,
  value: Float,
  balance: Float,
  date: Timestamp,
  id: Id[Transaction] = Id.empty[Transaction]
){
  def withID = {
    val seed = detail + value.toString + balance.toString + date.toString
    this.copy(id = Id.generate[Transaction](seed))
  }
}

class Transactions(tag: Tag) extends Table[Transaction](tag, "transactions") {
  def id = column[Id[Transaction]]("transaction_id", O.PrimaryKey)
  def detail = column[String]("transaction_detail")
  def value = column[Float]("transaction_value")
  def balance = column[Float]("transaction_balance")
  def date = column[Timestamp]("transaction_date")
  def * = (detail, value, balance, date, id) <> (Transaction.tupled, Transaction.unapply)
}

object Transactions extends TableQuery[Transactions](tag => new Transactions(tag)) {
  def contains(item: Transaction) = this.filter(_.id === item.withID.id).exists
  def insert(item: Transaction) = this += item.withID
}

object TransactionsAction extends DBClient {
  def insert(item: Transaction): Future[Unit] = {
    val operation = (for {
      exists <- Transactions.contains(item).result
      _ <- Transactions.insert(item) if !exists
    } yield nothing)
    db run operation.transactionally
  }
}