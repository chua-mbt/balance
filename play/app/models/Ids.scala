package models

import java.security.MessageDigest
import java.nio.ByteBuffer
import slick.lifted.MappedTo

case class Id[+M](id: Long) extends AnyVal with MappedTo[Long] {
  override def value = id
}

object Id {
  def generate[M](seed: String) = {
    val digest = MessageDigest.getInstance("MD5").digest(seed.getBytes)
    Id[M](ByteBuffer.wrap(digest.take(8)).getLong)
  }
  def empty[M] = Id[M](-1)
}