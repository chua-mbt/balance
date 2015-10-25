package services

import java.sql.Timestamp
import java.text.SimpleDateFormat
import scala.concurrent.Future
import scala.util.{Try, Success}
import play.api.Play
import play.api.libs.concurrent.Execution.Implicits._

import com.github.tototoshi.csv._
import models._

class RawRecords(path: String) {
  val root = new java.io.File(path)
  val subs = root.listFiles.toList
  val records = subs.flatMap(_.listFiles.toList)
  val formatter = new SimpleDateFormat("MM/dd/yyyy")

  def load(): Future[List[Unit]] = Future.sequence {
    records.flatMap { record =>
      CSVReader.open(record).toStream.collect {
        case date::detail::expense::revenue::aggregate::nil =>
          val timestamp = Try(new Timestamp(formatter.parse(date).getTime()))
          val value = Try(-expense.toFloat).orElse(Try(revenue.toFloat))
          val balance = Try(aggregate.toFloat)
          store(detail, timestamp, value, balance)
      }
    }
  }

  def store(detail: String, timestamp: Try[Timestamp], value: Try[Float], balance: Try[Float]): Future[Unit] = {
    (detail, timestamp, value, balance) match {
      case (detail, Success(timestamp), Success(value), Success(balance)) =>
        TransactionsAction.insert(Transaction(detail, value, balance, timestamp))
      case _ => Future.successful({})
    }
  }
}

object RawRecords {
  def apply() = {
    val config = Play.current.configuration
    new RawRecords(config.getString("records.path").getOrElse(""))
  }
}