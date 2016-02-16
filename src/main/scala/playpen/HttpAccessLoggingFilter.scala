package playpen

import org.joda.time.format.DateTimeFormat
import org.joda.time.{DateTime, DateTimeZone}
import org.slf4j.{LoggerFactory, MDC}
import play.api.http.HeaderNames._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc._
import playpen.requestid.RequestIdHeader

import scala.collection.JavaConverters._
import java.{util ⇒ ju}

object HttpAccessLoggingFilter extends EssentialFilter {
  private val log = LoggerFactory getLogger "HttpAccessLogger"
  private val dateTimeFmt = DateTimeFormat forPattern "EEE MMM dd HH:mm:ss zzz yyyy"

  def apply(nextFilter: EssentialAction) = new EssentialAction {
    def apply(rh: RequestHeader) = {
      val startTime = DateTime now DateTimeZone.UTC
      nextFilter(rh) map { result =>
        val endTime = DateTime now DateTimeZone.UTC
        val requestTime = (endTime.getMillis - startTime.getMillis).toString

        // Adapted from zeebox-core AccessLogEvent
        val httpMdc = Map(
          "req.datetime"    → (startTime toString dateTimeFmt),
          "req.method"      → rh.method.padTo(4, ' ').mkString,
          "req.protocol"    → rh.version,
          "rsp.status"      → result.header.status.toString,
          "rsp.duration"    → requestTime,
          "req.ipAddresses" → rh.remoteAddress,
          "req.userAgent"   → (rh.headers get USER_AGENT getOrElse "-"),
        // TODO: Figure out how to really do Play response size
          "rsp.size"        → (result.header.headers get CONTENT_LENGTH getOrElse "0"),
          "requestId"       → (result.header.headers get RequestIdHeader.header getOrElse "-"))

        val oldContextMap = Option(MDC.getCopyOfContextMap) getOrElse new ju.HashMap[String, String]()
        try {
          MDC setContextMap (oldContextMap.asScala ++ httpMdc).asJava
          log info s"${rh.uri}"
        } finally
          MDC setContextMap oldContextMap

        result withHeaders "Request-Time" -> requestTime
      }
    }
  }
}
