package playpen.requestid

final case class RequestId(id: String) extends AnyVal {
  override def toString: String = id
}
