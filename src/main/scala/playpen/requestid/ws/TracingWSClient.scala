package playpen.requestid.ws

import com.ning.http.client._
import play.api.libs.ws.WSRequest
import play.api.libs.ws.ning.NingWSClient
import play.api.libs.ws.ssl.SystemConfiguration
import playpen.requestid.{ RequestId, RequestIdHeader }

final case class TracingNingWSClient(config: AsyncHttpClientConfig) extends TracingWSClient {
  private val ningWsClient = NingWSClient(config)

  def close() = ningWsClient.close()

  def url(url: String)(implicit requestId: RequestId): WSRequest =
    ningWsClient url url withHeaders (RequestIdHeader.header -> requestId.id)
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
    * Generates a request holder which can be used to build requests.
    *
    * @param url The base URL to make HTTP requests to.
    * @return a WSRequestHolder
    */
  def url(url: String)(implicit requestId: RequestId): WSRequest

  /** Closes this client, and releases underlying resources. */
  def close(): Unit
}
