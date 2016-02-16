package controllers

import java.util.UUID
import javax.inject.Inject

import dao.{UserSessionDao, ProductDao}
import models.{UserSession, Product}
import org.slf4j.LoggerFactory
import play.api.data.validation.ValidationError
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.Future
import scala.util.Success

class ProductApp @Inject()(dao: ProductDao, userSessionDao: UserSessionDao) extends Controller {
  val LOGGER = LoggerFactory.getLogger(this.getClass.getName);


  def getAll(sort: String) = Action.async { request =>
      dao.get(sort).map(p => Ok(Json.toJson(p)))
  }

  def getById(id: UUID) = Action.async {
    dao.get(id).map(_.fold(NotFound("Not Found"))(a => Ok(Json.toJson(a))))
  }

  def insert() = Action.async(BodyParsers.parse.json) { request =>
    request.body.validate[Product].fold(badRequest, { a: Product =>
      authenticate(request, () => dao.insert(a) map (x => Ok(Json.toJson(x))))
    })
  }

  def update(id: UUID) = Action.async(BodyParsers.parse.json) { request =>
    request.body.validate[Product].fold(badRequest, { a: Product =>
      authenticate(request, () => dao.update(id, a) map (x => Ok(Json.toJson(x))))
    })
  }

  def delete(id: UUID) = Action.async { request =>
    authenticate(request, () => dao.delete(id) map (x => Ok(Json.toJson(x))))
  }

  def deleteAll() = Action.async { request =>
    authenticate(request, () => dao.deleteAll() map (x => Ok(Json.toJson(x))))
  }

  private[this] def badRequest(errors: Seq[(JsPath, Seq[ValidationError])]) = Future(BadRequest(JsError.toJson(errors)))

  def authenticate(request: Request[AnyRef], f: () => Future[Result]): Future[Result] = {
    if (Authentication.isValid(userSessionDao, request)) {
      f()
    } else {
      Future.successful(Forbidden("Unauthorized access"))
    }
  }
}
