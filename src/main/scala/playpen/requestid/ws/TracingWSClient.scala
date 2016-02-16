package playpen.requestid.ws

import com.ning.http.client._
import play.api.libs.ws.ning.{ NingWSClient, NingWSRequest }
import play.api.libs.ws.ssl.SystemConfiguration
import play.api.libs.ws.{ EmptyBody, WSRequest }
import playpen.requestid.{ RequestId, RequestIdHeader }

final case class TracingNingWSClient(config: AsyncHttpClientConfig) extends TracingWSClient {
  private val ningWsClient = NingWSClient(config)

  def underlying[T] = ningWsClient.underlying[T]

  def close() = ningWsClient.close()

  def url(url: String)(implicit requestId: RequestId): WSRequest =
    NingWSRequest(
      ningWsClient,
      url,
      "GET",
      EmptyBody,
      Map(
        RequestIdHeader.header -> Seq(requestId.id)
      ),
      Map(),
      None,
      None,
      None,
      None,
      None,
      None,
      None)
}

object TracingNingWSClient {
  /**
    * Convenient factory method that uses a [[play.api.libs.ws.WSClientConfig]] value for configuration instead of an [[AsyncHttpClientConfig]].
    *
    * Typical usage:
    *
    * {{{
    *   implicit val requestId: RequestId = ..
    *   val tracingClientConfig: TracingWSClientConfig = ...
    *   val client = TracingNingWSClient(tracingClientConfig)
    *   val request = client.url(someUrl).get()
    *   request.foreach { response =>
    *     doSomething(response)
    *     client.close()
    *   }
    * }}}
    *
    * @param config configuration settings
    */
  def apply(config: TracingWSClientConfig): TracingNingWSClient = {
    val client = new TracingNingWSClient(new TracingNingAsyncHttpClientConfigBuilder(config).build())
    new SystemConfiguration().configure(config.ningWSClientConfig.wsClientConfig)
    client
  }
}

trait TracingWSClient {

  /**
    * The underlying implementation of the client, if any.  You must cast explicitly to the type you want.
    *
    * @tparam T the type you are expecting (i.e. isInstanceOf)
    * @return the backing class.
    */
  def underlying[T]: T

  /**
    * Generates a request holder which can be used to build requests.
    *
    * @param url The base URL to make HTTP requests to.
    * @return a WSRequestHolder
    */
  def url(url: String)(implicit requestId: RequestId): WSRequest

  /** Closes this client, and releases underlying resources. */
  def close(): Unit
}
