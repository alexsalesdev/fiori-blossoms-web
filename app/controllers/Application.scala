package controllers

import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.inject.Inject

import dao.{UserSessionDao, ProductDao}
import models.{User, UserSession}
import play.api.libs.json.Json
import play.api.mvc._

import play.api.libs.concurrent.Execution.Implicits.defaultContext

import play.api.data._
import play.api.data.Forms._
import service.{SmtpConfig, EmailMessage, EmailService}
import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration

class Application @Inject()(userSessionDao: UserSessionDao) extends Controller {

  def login() = Action.async(BodyParsers.parse.json) { request =>
    request.body.validate[User].fold(
      formWithErrors => Future.successful(Forbidden("Invalid")),
        user => {
          if (User.validate(user)) {
            val uuid = UUID.randomUUID()
            userSessionDao.insert(UserSession(Some(uuid), request.headers.get("User-Agent").get,request.headers.get("Host").get)).flatMap({
              userSession => Future.successful(Ok(userSession.id.get.toString))
            })
          } else {
            Future.successful(Forbidden("Invalid"))
          }
        }
    )
  }

  def authenticate() = Action { request =>
    if (Authentication.isValid(userSessionDao, request)) {
      Ok("Success")
    } else {
      Forbidden("Unauthorized")
    }
  }


  def email = Action {
    val config: SmtpConfig = new SmtpConfig(false, true, 465, "smtp.gmail.com", "alexsalesdev@gmail.com", "a1exsales")
    EmailService.send(new EmailMessage(
      "aaa",
      "aaa1@mailinator.com",
      "alexsalesdev@gmail.com",
      "any text",
      "<html><body><h1>Welcome</h1></body></html>",
      config,
      FiniteDuration.apply(5, TimeUnit.SECONDS),
      3))
    Ok("Success")
  }

}
