package com.silenteight.sens.webapp.backend;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.backend.config.WebModule;
import com.silenteight.sens.webapp.backend.rest.RestModule;
import com.silenteight.sens.webapp.common.support.app.SensWebAppApplicationTemplate;
import com.silenteight.sens.webapp.user.UsersModule;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;

@Slf4j
@SpringBootApplication(
    scanBasePackageClasses = {
        RestModule.class,
        WebModule.class,
        UsersModule.class
    },
    exclude = {
        FreeMarkerAutoConfiguration.class
    })
public class WebApplication {

  public static void main(String[] args) {
    new SensWebAppApplicationTemplate(args, WebApplication.class)
        .contextHandler(applicationContext -> waitForFinish())
        .run(app -> app
            .configName("sens", "sens-webapp")
            .properties("spring.batch.job.enabled=false")
            // TODO: is it still needed?
            //.profiles(AlertSolvingMode.NORMAL.getSolvingStrategyName())
            .web(true));
  }

  private static void waitForFinish() {
    try {
      Thread.currentThread().join();
    } catch (InterruptedException ignored) {
      log.info("Interrupted, exiting...");
      Thread.currentThread().interrupt();
    }
  }
}
