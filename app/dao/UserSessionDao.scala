package dao

import java.util.{Date, UUID}
import javax.inject.Inject

import models.UserSession
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.driver.JdbcProfile
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class UserSessionDao @Inject()(val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  import driver.api._

  class Sessions(tag: Tag) extends Table[UserSession](tag, "USER_SESSION") {
    def id = column[Option[UUID]]("ID", O.PrimaryKey)
    def userAgent = column[String]("USER_AGENT")
    def host = column[String]("HOST")

    override def * = (id, userAgent, host) <>(UserSession.tupled, UserSession.unapply)
  }

  private val sessions = TableQuery[Sessions]


  def get(id: UUID): Future[Option[UserSession]] =  db.run(sessions.filter(_.id === id).result.headOption)

  def insert(a: UserSession): Future[UserSession] = {
    db.run(sessions += a).map(_ => a)
  }

}
