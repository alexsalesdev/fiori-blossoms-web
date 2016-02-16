package service

import java.util.concurrent.TimeUnit

import play.api.Application
import utils.BaseSpec

import scala.concurrent.duration.FiniteDuration

import scala.concurrent.ExecutionContext.Implicits.global
/**
 * Created by asales on 17/1/2016.
 */
class EmailServiceSpec extends BaseSpec {

  def service(implicit app: Application) = app.injector.instanceOf[EmailService]
  "EmailService" should {

    "send email" in {
      service.send(new EmailMessage(
        "aaa",
        "aaa1@mailinator.com",
        "k642alex@yahoo.com",
        "any text",
        "<html><body><h1>Welcome</h1></body></html>",
        new SmtpConfig(false, false, 587, "smtp.gmail.com", "alexsalesdev@gmail.com", "a1exsales"),
        FiniteDuration.apply(5, TimeUnit.SECONDS),
        3));
    }

  }
}
