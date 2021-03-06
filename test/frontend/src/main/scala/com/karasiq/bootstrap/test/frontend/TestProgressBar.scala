package com.karasiq.bootstrap.test.frontend

import com.karasiq.bootstrap.BootstrapComponent
import com.karasiq.bootstrap.BootstrapImplicits._
import com.karasiq.bootstrap.alert.{Alert, AlertStyle}
import com.karasiq.bootstrap.progressbar.{ProgressBar, ProgressBarStyle}
import rx._
import rx.async._

import scala.concurrent.duration._
import scalatags.JsDom.all._

final class TestProgressBar(style: ProgressBarStyle, updateInterval: FiniteDuration)(implicit ctx: Ctx.Owner) extends BootstrapComponent {
  override def render(md: Modifier*): Modifier = {
    val progressBarValue = Var(0)

    val progressBar = ProgressBar.withLabel(progressBarValue).renderTag(style, ProgressBarStyle.striped, ProgressBarStyle.animated, md).render

    implicit val scheduler = new AsyncScheduler
    val timer = Timer(updateInterval)
    timer.foreach { _ ⇒
      if (progressBarValue.now < 100) {
        progressBarValue.update(progressBarValue.now + 1)
      } else {
        val alert = Alert(AlertStyle.success,
          strong("Testing"), " has finished. ",
          Alert.link(href := "https://getbootstrap.com/components/#alerts", target := "blank", "Alert inline link.")
        )
        progressBar.parentNode.replaceChild(alert.render, progressBar)
        progressBarValue.kill()
        timer.kill()
      }
    }

    progressBar
  }
}
