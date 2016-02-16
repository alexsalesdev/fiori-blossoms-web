package dao

import java.util.Date

import models.{Currency, Product}
import org.scalatest.time.{Millis, Seconds, Span}
import play.api.Application
import utils.BaseSpec

import scala.concurrent.ExecutionContext.Implicits.global

class ProductDaoSpec extends BaseSpec {

  implicit val patience = PatienceConfig(timeout = Span(5, Seconds), interval = Span(500, Millis))

  def dao(implicit app: Application) = app.injector.instanceOf[ProductDao]

  "ProductDao" should {

    "return empty list" in {
      whenReady(dao.get("title"))(products => products mustBe empty)
    }

    "create new product" in {
      val product = Product(None, "title", "0.0", Currency.PHP, Some("http://"), true, 5, Some(new Date()), Some(new Date()), Some("alex"));
      val f = dao.insert(product).flatMap(_ => dao.get("title"))
      whenReady(f)(products => products must have length 1)
    }

    "get product by id" in {
      val product = Product(None, "title", "0.0", Currency.PHP, Some("http://"), true, 5, Some(new Date()), Some(new Date()), Some("alex"));
      val f = dao.insert(product).flatMap(a => dao.get(a.id.get))
      whenReady(f)(opt => opt.value.title mustEqual product.title)
    }

    "delete product" in {
      val product = Product(None, "title", "0.0", Currency.PHP, Some("http://"), true, 5, Some(new Date()), Some(new Date()), Some("alex"));
      val f = dao.insert(product)
        .flatMap(a => dao.delete(a.id.get))
        .flatMap(_ => dao.get("title"))

      whenReady(f) { products => products  must be(empty) }
    }

    "modify product" in {
      val oldProduct = Product(None, "title", "0.0", Currency.PHP, Some("http://"), true, 5, Some(new Date()), Some(new Date()), Some("alex"));
      val newProduct = oldProduct.copy(title = "newtitle")
      val f = dao.insert(oldProduct).flatMap { a =>
        dao.update(a.id.get, newProduct)
          .flatMap(_ => dao.get(a.id.get))
      }
      whenReady(f)(opt => opt.value.title mustEqual newProduct.title)
    }


  }

}
