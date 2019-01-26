package repositories

import javax.inject.{Inject, Singleton}
import play.api.cache.redis.CacheApi

trait UserRepository {

  def add(userName: String): Unit
  def contains(userName: String): Boolean
}

@Singleton
class RedisUserRepository @Inject()(cache: CacheApi) extends UserRepository {

  def add(userName: String): Unit = {
    cache.set[String]("users").add(userName)
  }

  def contains(userName: String): Boolean = {
    cache.set[String]("users").contains(userName)
  }
}
