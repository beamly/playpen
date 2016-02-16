package playpen

import java.util.UUID

final case class RequestId(id: String) extends AnyVal {
  override def toString: String = id
}

object RequestId {
  // Non-RFC headers
  val HTTP_REQUEST_HEADER = "X-Request-ID"

  object Implicits {
    implicit def random: RequestId = RequestId.random
  }

  def random: RequestId = RequestId("req" + UUID.randomUUID().toString)
}
