package playpen

import com.ning.http.client._
import play.api.libs.ws.WSRequest
import play.api.libs.ws.ning.NingWSClient
import play.api.libs.ws.ssl.SystemConfiguration

final case class TracingNingWSClient(config: AsyncHttpClientConfig) extends TracingWSClient {
  private val ningWsClient = NingWSClient(config)

  def close() = ningWsClient.close()

  def url(url: String)(implicit requestId: RequestId): WSRequest =
    ningWsClient url url withHeaders (RequestId.HTTP_REQUEST_HEADER -> requestId.id)
}

object TracingNingWSClient {
  /**
    * Convenient factory method that uses a [[TracingWSClientConfig]] value for configuration instead of an AsyncHttpClientConfig.
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
