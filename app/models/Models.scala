package models

import java.time.LocalDate
import java.util.{Date, UUID}

import com.mohiva.play.silhouette.api.LoginInfo
import models.Currency.Currency
import models.Fuel.Fuel

object Fuel extends Enumeration {
  type Fuel = Value
  val Gasoline, Diesel = Value
}

object Currency extends Enumeration {
  type Currency = Value
  val USD, PHP, SGD = Value
}

case class User(username:String, password:String) {

}

object User {
  val VALID_USER = User("aaa", "aaa");
  def validate(u: User): Boolean = u.password == VALID_USER.password && u.username == VALID_USER.username
}

case class CarAdvert(id: Option[UUID], title: String, fuel: Fuel, price: Int, `new`: Boolean, mileage: Option[Int],
                     firstRegistration: Option[LocalDate])

case class Product(id: Option[UUID], title: String, price: String, currency: Currency, imageUrl: Option[String], `new`: Boolean, quantity: Int,
                     createdDate: Option[Date], updateDate: Option[Date], createdBy: Option[String])

case class UserSession(id: Option[UUID], userAgent: String, host: String)

