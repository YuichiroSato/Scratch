import java.util.concurrent.Executors

import com.typesafe.scalalogging.Logger

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

object Main {

  val logger = Logger(this.getClass)

  var startAt: Option[Long] = None

  def elapsedTime(): Long = {
    if (startAt.isEmpty) {
      startAt = Some(System.currentTimeMillis())
    }

    System.currentTimeMillis() - startAt.get
  }

  def task(n: Int)(implicit ec: ExecutionContext): Future[Unit] = {
    Future{
      logger.info(s"Task $n sleeping at ${elapsedTime()} [${Thread.currentThread().getName}]")
      Thread.sleep(5000)
      logger.info(s"Task $n awoke at ${elapsedTime()} [${Thread.currentThread().getName}]")
    }
  }

  def main(args: Array[String]): Unit = {
    logger.info(s"Application start at ${elapsedTime()}")

    val threadPool = Executors.newFixedThreadPool(3)
    implicit val ex = ExecutionContext.fromExecutor(threadPool)
    logger.info("Task 1 executing.")
    task(1)
    logger.info("Task 2 executing.")
    task(2)
    logger.info("Task 3 executing.")
    task(3)
    logger.info("Task 4 executing.")
    task(4)
    logger.info("Task 5 executing.")
    task(5)
    logger.info("Task 6 executing.")
    task(6)

    threadPool.shutdown()
  }
}
