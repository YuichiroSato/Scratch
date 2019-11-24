import com.typesafe.scalalogging.Logger

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.util.{Random, Try}

object Main {

  val logger = Logger(this.getClass)

  def generateRandomNumber(label: String, sleep: Int): Future[Int] = {
    Future {
      logger.info(s"$label start (sleep $sleep millis ...)")
      val num = Random.nextInt(10)
      Thread.sleep(sleep, 0)
      logger.info(s"$label generates $num")
      num
    }
  }

  def awaitTask[A](timeout: Int, label: String, task: Future[A]): A = {
    val start = System.currentTimeMillis()
    logger.info(s"Await $label")
    val result = Await.result(task, Duration(timeout, "millis"))
    logger.info(s"$label is finished in ${System.currentTimeMillis() - start}")
    result
  }

  def exp1(): Unit = {
    generateRandomNumber("task1", 1000)
    generateRandomNumber("task2", 3000)
    generateRandomNumber("task3", 5000)
    logger.info("Thread sleep 10 sec")
    Thread.sleep(10000)
    logger.info("Task1 is finished")
  }

  def exp2(): Unit = {
    val task1 = generateRandomNumber("3 sec task", 3000)
    val task2 = generateRandomNumber("1 sec task1", 1000)
    val task3 = generateRandomNumber("1 sec task2", 1000)
    val task4 = generateRandomNumber("1 sec task3", 1000)
    awaitTask(10000, "3 sec task", task1)
    awaitTask(100, "1 sec task1", task2)
    awaitTask(100, "1 sec task2", task3)
    awaitTask(100, "1 sec task3", task4)
  }

  def exp3(): Unit = {
    val task1 = generateRandomNumber("3 sec task", 3000)
    val task2 = generateRandomNumber("1 sec task1", 1000)
    val task3 = generateRandomNumber("1 sec task2", 1000)
    val task4 = generateRandomNumber("1 sec task3", 1000)
    Try(awaitTask(100, "1 sec task1", task2))
      .recoverWith{ case e =>
        logger.info("Task1 timeout!")
        Try(0)
      }
    awaitTask(10000, "3 sec task", task1)
    awaitTask(100, "1 sec task2", task3)
    awaitTask(100, "1 sec task3", task4)
  }

  def exp4(): Unit = {
    val total = for {
      a <- generateRandomNumber("task1", 200)
      b <- generateRandomNumber("task2", 300)
      c <- generateRandomNumber("task3", 400)
    } yield a + b + c
    logger.info("Slead sleep 1 sec")
    Thread.sleep(1000, 0)
    logger.info(s"total: ${Try(awaitTask(600, "Combined task", total))
      .recoverWith{case e =>
        logger.info("Timeout!")
      Try(-1)}}")
    logger.info(s"future value: ${total.value}")
  }

  def exp5(): Unit = {
    val total = for {
      a <- generateRandomNumber("task1", 200)
      b <- generateRandomNumber("task2", 300)
      c <- generateRandomNumber("task3", 400)
    } yield a + b + c
    logger.info(s"total : ${Try(awaitTask(600, "Combined task", total))
      .recoverWith{case e =>
        logger.info("Timeout!")
        Try(-1)}}")
    logger.info(s"future value: ${total.value}")
  }

  def main(args: Array[String]): Unit = {
    logger.info("Application start")

    logger.info("\n\nExp1 start\n")
    exp1()
    logger.info("\n\nExp2 start\n")
    exp2()
    logger.info("\n\nExp3 start\n")
    exp3()
    logger.info("\n\nExp4 start\n")
    exp4()
    logger.info("\n\nExp5 start\n")
    exp5()
  }
}