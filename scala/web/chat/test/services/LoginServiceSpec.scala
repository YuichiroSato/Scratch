package services

import org.mockito.Mockito._
import org.scalatest.mockito._
import org.scalatestplus.play._
import repositories.{UserLockRepository, UserRepository}

class LoginServiceSpec extends PlaySpec with MockitoSugar {

  "LoginService" must {
    "be success when lock is available and user name has not been registered" in {
      val mockUserRepository = mock[UserRepository]
      when (mockUserRepository.contains("aaa")) thenReturn false
      val mockUserLockRepository = mock[UserLockRepository]
      when (mockUserLockRepository.lock) thenReturn true

      val loginService = new LoginService(mockUserRepository, mockUserLockRepository)

      val result = loginService.login("aaa")

      result mustBe Right(())
    }
    "be failure when lock is available and user name has been registered" in {
      val mockUserRepository = mock[UserRepository]
      when (mockUserRepository.contains("aaa")) thenReturn true
      val mockUserLockRepository = mock[UserLockRepository]
      when (mockUserLockRepository.lock) thenReturn true

      val loginService = new LoginService(mockUserRepository, mockUserLockRepository)

      val result = loginService.login("aaa")

      result mustBe Left("This user name is already registered.")
    }
    "be failure when lock is not available" in {
      val mockUserRepository = mock[UserRepository]
      when (mockUserRepository.contains("aaa")) thenReturn false
      val mockUserLockRepository = mock[UserLockRepository]
      when (mockUserLockRepository.lock) thenReturn false

      val loginService = new LoginService(mockUserRepository, mockUserLockRepository)

      val result = loginService.login("aaa")

      result mustBe Left("System is busy. Please try it again later.")
    }
  }
}
