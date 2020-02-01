import java.util.concurrent.Executors

import com.typesafe.scalalogging.Logger

object Main {

  val logger = Logger(this.getClass)

  var startAt: Option[Long] = None

  def elapsedTime(): Long = {
    if (startAt.isEmpty) {
      startAt = Some(System.currentTimeMillis())
    }

    System.currentTimeMillis() - startAt.get
  }

  def task(n: Int): Runnable = {
    () => {
      logger.info(s"Task $n sleeping at ${elapsedTime()} [${Thread.currentThread().getName}]")
      Thread.sleep(5000)
      logger.info(s"Task $n awoke at ${elapsedTime()} [${Thread.currentThread().getName}]")
    }
  }

  def main(args: Array[String]): Unit = {
    logger.info(s"Application start at ${elapsedTime()}")

    val ex = Executors.newFixedThreadPool(3)
    logger.info("Task 1 executing.")
    ex.execute(task(1))
    logger.info("Task 2 executing.")
    ex.execute(task(2))
    logger.info("Task 3 executing.")
    ex.execute(task(3))
    logger.info("Task 4 executing.")
    ex.execute(task(4))
    logger.info("Task 5 executing.")
    ex.execute(task(5))
    logger.info("Task 6 executing.")
    ex.execute(task(6))

    ex.shutdown()
  }
}