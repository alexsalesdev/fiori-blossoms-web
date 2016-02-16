package controllers

import java.util.UUID

import dao.UserSessionDao
import models.UserSession
import org.slf4j.LoggerFactory
import play.api.mvc.{Result, AnyContent, Request, Controller}

import play.api.libs.concurrent.Execution.Implicits.defaultContext

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.{Try, Failure, Success}

/**
 * Created by asales on 10/1/2016.
 */
object Authentication  extends Controller {
  val LOGGER = LoggerFactory.getLogger(this.getClass.getName)

  def isValid(userSessionDao: UserSessionDao, request: Request[AnyRef]): Boolean = {
    LOGGER.info("" + request.headers.get("X-AUTH-TOKEN"))
    LOGGER.info("" + request.headers.get("Host"))
    LOGGER.info("" + request.headers.get("User-Agent"))
    request.headers.get("X-AUTH-TOKEN") match {
      case Some(s) => {
          if (s.isEmpty) {
            false
          } else {
            val uuid = UUID.fromString(s)
            val future = userSessionDao.get(uuid) map {
              userSession => {
                userSession match {
                  case None => false
                  case Some(user) => {
                    val newSession = user.copy(userAgent = request.headers.get("User-Agent").get, host = request.headers.get("Host").get)
                    if (user == newSession) true else false
                  }
                }
              }
            }
            Await.ready(future, Duration.Inf).value.get match {
              case Success(t) => t
            }
        }
      }
      case None => false;
    }

  }
}
