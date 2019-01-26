import com.google.inject.AbstractModule
import repositories._

class Module extends AbstractModule {

  override def configure() = {
    bind(classOf[UserRepository]).to(classOf[RedisUserRepository])
    bind(classOf[UserLockRepository]).to(classOf[RedisUserLockRepository])
  }
}