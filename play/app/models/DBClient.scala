package models

import slick.driver.PostgresDriver.api._

trait DBClient {
  val db = Database.forConfig("slick.dbs.default.db")
  val nothing = ()
}