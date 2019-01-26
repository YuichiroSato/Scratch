package services

import javax.inject.{Inject, Singleton}
import repositories.{UserLockRepository, UserRepository}

@Singleton
class LoginService @Inject()(userRepository: UserRepository, userLockRepository: UserLockRepository) {

  def login(userName: String): Either[String, Unit] = {
    var n = 0
    while (true) {
      val locked = userLockRepository.lock()
      if (locked) {
        val isRegistered = userRepository.contains(userName)
        if (isRegistered) {
          return Left("This user name is already registered.")
        } else {
          userRepository.add(userName)
        }
        userLockRepository.unlock()
        return Right()
      }
      Thread.sleep(10)
      n += 1
      if (n >= 10) {
        return Left("System is busy. Please try it again later.")
      }
    }
    return Right()
  }
}
