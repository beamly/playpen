package playpen

import org.specs2.mutable.Specification
import java.time.{ZoneId, Instant, Clock}

class RequestContextTest extends Specification {

  "RequestContext" should {

    "get created" in {
      val rc = RequestContext.random
      rc.requestId.toString must not beEmpty
    }

    "set start time" in {
      val inst = Instant.now()
      val rc = RequestContext(RequestId.random)
      rc.startTime.isBefore(inst) must beFalse
    }

    "set start time with a fixed clock" in {
      val inst = Instant.now()
      val rc = RequestContext(RequestId.random, Clock.fixed(inst, ZoneId.systemDefault()))
      rc.startTime mustEqual inst
    }
  }
}
