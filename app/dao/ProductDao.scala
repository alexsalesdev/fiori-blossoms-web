package dao

import java.sql.Timestamp
import java.util.{Date, UUID}
import javax.inject.Inject

import models.Currency.Currency
import models.{Currency, Product}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ProductDao @Inject()(val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  implicit val dateColumn = MappedColumnType.base[Date, Timestamp]({d => new Timestamp(d.getTime)}, {t => new Date(t.getTime)})
  implicit val currencyColumn = MappedColumnType.base[Currency, String](_.toString, Currency.withName)

  class Products(tag: Tag) extends Table[Product](tag, "PRODUCT") {
    def id = column[Option[UUID]]("ID", O.PrimaryKey)
    def title = column[String]("TITLE")
    def price = column[String]("PRICE")
    def currency = column[Currency]("CURRENCY")
    def imageUrl = column[Option[String]]("IMAGE_URL")
    def quantity = column[Int]("QUANTITY")
    def `new` = column[Boolean]("NEW")
    def createdDate = column[Option[Date]]("CREATED_DATE")
    def lastUpdate = column[Option[Date]]("LAST_UPDATE")
    def createdBy = column[Option[String]]("CREATED_BY")

    override def * = (id, title, price, currency, imageUrl, `new`, quantity, createdDate, lastUpdate, createdBy) <>(Product.tupled, Product.unapply)
  }

  private val products = TableQuery[Products]

  def get(sort: String): Future[Seq[Product]] =  db.run(products.sortBy(x => sort match {
    case "title" => x.title.asc
    case "price" => x.price.asc
    case _ => x.id.asc
  }).result)

  def get(id: UUID): Future[Option[Product]] =  db.run(products.filter(_.id === id).result.headOption)

  def insert(a: Product): Future[Product] = {
    val a1 = a.copy(id = Some(UUID.randomUUID()), createdDate = Some(new Date()), updateDate = Some(new Date()), createdBy = Some("Admin"))
    db.run(products += a1).map(_ => a1)
  }

  def update(id: UUID, a: Product): Future[Int] = {
    val a1 = a.copy(id = Some(id), updateDate = Some(new Date()))
    db.run(products.filter(_.id === id).update(a1))
  }

  def delete(id: UUID): Future[Int] = db.run(products.filter(_.id === id).delete)

  def deleteAll(): Future[Int] = db.run(products.delete)
}
