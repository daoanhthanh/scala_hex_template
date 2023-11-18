package vn.ventures.primaryAdapter.controllers

import vn.ventures.domain.healthckeck.HealthCheckService
import zio.*
import zio.http.*
import zio.http.html.{html as html5, *}

import scala.sys.process.*
import scala.util.Try

object HealthCheckController {

  val routes: HttpApp[HealthCheckService, Nothing] = Http.collectZIO {

    case Method.HEAD -> _ / "healthcheck" =>
      ZIO.succeed {
        Response.status(Status.NoContent)
      }

    case Method.GET -> _ / "healthcheck" =>
      HealthCheckService.check.map { dbStatus =>
        if (dbStatus.status) Response.text("OK").withStatus(Status.Ok)
        else Response.status(Status.InternalServerError)
      }

  }

  val makeWebAssets: App[Any] =
    Http.collectHttp[Request] { case Method.GET -> _ / "assets" / asset =>
      Http
        .fromResource(asset)
        .tapZIO(_ => ZIO.logInfo(s"Asset $asset found"))
        .mapError(_ => Response.text("Not Found").withStatus(Status.NotFound))
    }

  val helloWorld: App[Any] = Http.collect { case Method.GET -> !! =>
    val currentUser = Try("git config user.name".!!.trim).getOrElse("My Friend")
    Response.html(
      withHtmlContent(
        canvas(idAttr := "Matrix"),
        h1(idAttr     := "animatedText", s"Hello $currentUser!"),
        a(
          idAttr     := "reference",
          targetAttr := "_blank",
          hrefAttr   := "https://zio.dev/overview/summary",
          "Learn ZIO here 🚀"
        )
      )
    )
  }

  private def withHtmlContent(content: zio.http.html.Html*) =
    html5(
      langAttr := "en",
      head(
        title("Design Tool"),
        link(relAttr     := "stylesheet", href := "/assets/styles.css"),
        meta(charsetAttr := "utf-8", nameAttr  := "viewport", contentAttr := "width=device-width, initial-scale=1.0"),
        script(srcAttr := "/assets/scripts.js", typeAttr := "text/javascript")
      ),
      body(
        content: _*
      )
    )
}
