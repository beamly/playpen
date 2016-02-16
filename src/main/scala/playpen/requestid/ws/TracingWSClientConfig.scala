package playpen.requestid.ws

import javax.inject.{ Inject, Provider, Singleton }

import com.ning.http.client.AsyncHttpClientConfig.Builder
import play.api.libs.ws.ning.{ NingAsyncHttpClientConfigBuilder, NingWSClientConfig }
import play.api.{ Configuration, Environment }


/**
  * Tracing client config to enforce a user-agent is defined in the config
  *
  * @param userAgent          The user-agent to use when making a request.
  * @param ningWSClientConfig The Ning client config.
  */
final case class TracingWSClientConfig(userAgent: String,
                                       ningWSClientConfig: NingWSClientConfig = NingWSClientConfig())

@Singleton
class TracingWSConfigParser @Inject()(ningWSClientConfig: NingWSClientConfig,
                                      configuration: Configuration,
                                      environment: Environment) extends Provider[TracingWSClientConfig] {

  def get = parse()

  def parse(): TracingWSClientConfig = {
    val userAgent = configuration.underlying.getString("play.ws.useragent")

    TracingWSClientConfig(
      userAgent = userAgent,
      ningWSClientConfig = ningWSClientConfig)
  }
}


class TracingNingAsyncHttpClientConfigBuilder(tracingWsClientConfig: TracingWSClientConfig)
  extends NingAsyncHttpClientConfigBuilder(tracingWsClientConfig.ningWSClientConfig) {

  override protected val addCustomSettings: (Builder) => Builder = _.setUserAgent(tracingWsClientConfig.userAgent)

  override def modifyUnderlying(modify: (Builder) => Builder): TracingNingAsyncHttpClientConfigBuilder = {
    new TracingNingAsyncHttpClientConfigBuilder(tracingWsClientConfig) {
      override val addCustomSettings = modify compose TracingNingAsyncHttpClientConfigBuilder.this.addCustomSettings
      override val builder = TracingNingAsyncHttpClientConfigBuilder.this.builder
    }
  }
}
