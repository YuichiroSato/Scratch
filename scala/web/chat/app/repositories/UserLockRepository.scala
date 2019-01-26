package repositories

import javax.inject.{Inject, Singleton}
import play.api.cache.redis.CacheApi
import scala.concurrent.duration._

trait UserLockRepository {

  def lock(): Boolean
  def unlock(): Unit
}

@Singleton
class RedisUserLockRepository @Inject()(cache: CacheApi) extends UserLockRepository {

  def lock(): Boolean = {
    cache.setIfNotExists("users-lock", "writing", 1.seconds)
  }

  def unlock(): Unit = {
    cache.remove("users-lock")
  }
}
