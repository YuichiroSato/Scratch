package controllers

import models.LoginData

import scala.concurrent.Future
import org.mockito.Mockito._
import org.scalatest.mockito._
import org.scalatestplus.play._
import play.api.mvc._
import repositories.{UserLockRepository, UserRepository}
import services.LoginService
import play.api.test._
import play.api.test.Helpers._


class HomeControllerSpec extends PlaySpec with MockitoSugar with Results {

  "HomeContoller" must {
    "return Ok for get request" in {
      val mockUserRepository = mock[UserRepository]
      val mockUserLockRepository = mock[UserLockRepository]
      val loginService = new LoginService(mockUserRepository, mockUserLockRepository)

      val homeController = new HomeController(stubControllerComponents())(loginService)

      val result: Future[Result] = homeController.index().apply(FakeRequest())

      status(result) mustEqual 200
    }
    "return Redirect when login success" in {
      val mockUserRepository = mock[UserRepository]
      when (mockUserRepository.contains("aaa")) thenReturn false
      val mockUserLockRepository = mock[UserLockRepository]
      when (mockUserLockRepository.lock) thenReturn true
      val loginService = new LoginService(mockUserRepository, mockUserLockRepository)

      val homeController = new HomeController(stubControllerComponents())(loginService)

      val request: FakeRequest[LoginData] = FakeRequest(POST, "/").withBody(new LoginData("aaa"))
      val result = homeController.login().apply(request)

      status(result) mustEqual 303
    }
    "return error message when lock is not available" in {
      val mockUserRepository = mock[UserRepository]
      when (mockUserRepository.contains("aaa")) thenReturn true
      val mockUserLockRepository = mock[UserLockRepository]
      when (mockUserLockRepository.lock) thenReturn false
      val loginService = new LoginService(mockUserRepository, mockUserLockRepository)

      val homeController = new HomeController(stubControllerComponents())(loginService)

      val request: FakeRequest[LoginData] = FakeRequest(POST, "/").withBody(new LoginData("aaa"))
      val result = homeController.login().apply(request)

      status(result) mustEqual 200
      contentAsString(result).contains("System is busy. Please try it again later.") mustBe true
    }
    "return error message when user name is already registered" in {
      val mockUserRepository = mock[UserRepository]
      when (mockUserRepository.contains("aaa")) thenReturn true
      val mockUserLockRepository = mock[UserLockRepository]
      when (mockUserLockRepository.lock) thenReturn true
      val loginService = new LoginService(mockUserRepository, mockUserLockRepository)

      val homeController = new HomeController(stubControllerComponents())(loginService)

      val request: FakeRequest[LoginData] = FakeRequest(POST, "/").withBody(new LoginData("aaa"))
      val result = homeController.login().apply(request)

      status(result) mustEqual 200
      contentAsString(result).contains("This user name is already registered.") mustBe true
    }
  }
}
