package playpen

import java.time.{Clock, Instant}

case class RequestContext(requestId: RequestId, startTime: Instant)

object RequestContext {

  def apply(requestId: RequestId, clock: Clock = Clock.systemUTC()): RequestContext = RequestContext(requestId = requestId, Instant.now(clock))

  def random: RequestContext = RequestContext(RequestId.random)

  object Implicits {
    implicit def random = RequestContext.random
  }
}
