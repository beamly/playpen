package playpen.requestid

import java.util.UUID

object RequestIdGenerator {

  object Implicits {
    implicit def random: RequestId = RequestIdGenerator.random
  }

  def random: RequestId = RequestId("req" + UUID.randomUUID().toString)


}
