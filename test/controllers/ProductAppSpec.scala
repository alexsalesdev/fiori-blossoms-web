package controllers

import java.time.LocalDate
import java.util.UUID

import models._
import org.joda.time.LocalDateTime
import play.api.Logger
import play.api.http.MimeTypes
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._
import utils.BaseSpec

import models.Product

class ProductAppSpec extends BaseSpec {

  val product = Product(None, "title", "0.0", Currency.PHP, Some("http://"), true, 5, Some(LocalDateTime.now().toDate), Some(LocalDateTime.now().toDate), Some("alex"));

  "Application" should {

    "send 404 on not existed path" in {
      val resp = route(FakeRequest(GET, "/nonexisting")).value
      status(resp) mustEqual NOT_FOUND
    }

    "show empty list" in {
      val Some(resp) = route(FakeRequest(GET, "/products"))
      status(resp) mustEqual OK
      contentType(resp) mustBe Some(MimeTypes.JSON)
      contentAsJson(resp).as[Seq[Product]] mustBe empty
    }

    "add new product" in {
      Logger.info(Json.toJson(product).toString());
      val resp1 = route(FakeRequest(POST, "/products").withJsonBody(Json.toJson(product))).value
      status(resp1) mustEqual OK

      val resp2 = route(FakeRequest(GET, "/products")).value
      status(resp2) mustEqual OK
      contentType(resp2) mustBe Some(MimeTypes.JSON)
      contentAsJson(resp2).as[Seq[Product]] mustNot be(empty)
    }

    "get product by id" in {
      val resp1 = route(FakeRequest(POST, "/products").withJsonBody(Json.toJson(product))).value
      status(resp1) mustEqual OK

      val id = contentAsJson(resp1).as[Product].id.value
      val Some(resp2) = route(FakeRequest(GET, "/products/" + id))
      status(resp2) mustEqual OK
      contentType(resp2) mustBe Some(MimeTypes.JSON)
      contentAsJson(resp2).as[Product] must not be null
    }

    "delete product" in {
      val resp1 = route(FakeRequest(POST, "/products").withJsonBody(Json.toJson(product))).value
      status(resp1) mustEqual OK

      val id = contentAsJson(resp1).as[Product].id.get
      val resp2 = route(FakeRequest(DELETE, "/products/" + id)).value
      status(resp2) mustEqual OK

      val resp3 = route(FakeRequest(GET, "/products/" + id)).value
      status(resp3) mustEqual NOT_FOUND
    }

    "modify product" in {
      val resp1 = route(FakeRequest(POST, "/products").withJsonBody(Json.toJson(product))).value
      status(resp1) mustEqual OK

      val id = contentAsJson(resp1).as[Product].id.get
      val newProduct = product.copy(title = "newTitle")
      val resp2 = route(FakeRequest(PUT, "/products/" + id).withJsonBody(Json.toJson(newProduct))).value
      status(resp2) mustEqual OK

      val resp3 = route(FakeRequest(GET, "/products/" + id)).value
      status(resp3) mustEqual OK
      contentType(resp3) mustBe Some(MimeTypes.JSON)
      contentAsJson(resp3).as[Product].title mustEqual newProduct.title
    }

    "throw bad request for empty body" in {
      val resp1 = route(FakeRequest(POST, "/products").withJsonBody(Json.obj())).value
      status(resp1) mustBe BAD_REQUEST

      val resp2 = route(FakeRequest(PUT, "/products/" + UUID.randomUUID()).withJsonBody(Json.obj())).value
      status(resp2) mustBe BAD_REQUEST
    }
  }
}
