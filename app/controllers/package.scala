import java.time.LocalDate
import java.util.UUID

import models.Currency
import models.Currency._
import models.Fuel.Fuel
import models._
import org.joda.time.LocalDateTime
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._
import java.util.Date

package object controllers {


  implicit val productWrites: Writes[Product] = (
    (JsPath \ "id").writeNullable[UUID] and
      (JsPath \ "title").write[String] and
      (JsPath \ "price").write[String] and
      (JsPath \ "currency").write[Currency] and
      (JsPath \ "imageUrl").writeNullable[String] and
      (JsPath \ "new").write[Boolean] and
      (JsPath \ "quantity").write[Int] and
      (JsPath \ "createdDate").writeNullable[Date] and
      (JsPath \ "updateDate").writeNullable[Date] and
      (JsPath \ "createdBy").writeNullable[String]
    ) (unlift(Product.unapply))

  implicit val currencyReads = new Reads[Currency] {
    override def reads(json: JsValue) = {
      json.validate[String].flatMap { s =>
        Currency.values.find(_.toString.equalsIgnoreCase(s)).map(JsSuccess(_)).getOrElse(JsError(s"Unknown currency '$s'"))
      }
    }
  }

  implicit val productReadsValid = new Reads[Product] {
    val productReads: Reads[Product] = (
      (JsPath \ "id").readNullable[UUID] and
        (JsPath \ "title").read[String] and
        (JsPath \ "price").read[String] and
        (JsPath \ "currency").read[Currency] and
        (JsPath \ "imageUrl").readNullable[String] and
        (JsPath \ "new").read[Boolean] and
        (JsPath \ "quantity").read[Int] and
        (JsPath \ "createdDate").readNullable[Date] and
        (JsPath \ "updateDate").readNullable[Date] and
        (JsPath \ "createdBy").readNullable[String]
      ) (Product.apply _)

    override def reads(json: JsValue): JsResult[Product] = {
      json.validate[Product](productReads)
    }
  }

  implicit val userWrites: Writes[User] = (
      (JsPath \ "username").write[String] and
      (JsPath \ "password").write[String]
    ) (unlift(User.unapply))

  implicit val userReadsValid = new Reads[User] {
    val userReads: Reads[User] = (
        (JsPath \ "username").read[String] and
          (JsPath \ "password").read[String]
      ) (User.apply _)

    override def reads(json: JsValue): JsResult[User] = {
      json.validate[User](userReads)
    }
  }

}
