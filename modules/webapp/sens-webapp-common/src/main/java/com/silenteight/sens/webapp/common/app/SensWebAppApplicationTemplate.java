package com.silenteight.sens.webapp.common.app;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.commons.app.spring.SpringApplicationContextCallback;
import com.silenteight.sep.base.common.app.ApplicationBootstrapper;
import com.silenteight.sep.base.common.app.HomeDirectoryDiscoverer;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
public class SensWebAppApplicationTemplate {

  private final String appName;
  private final String[] args;
  private final Class<?> source;

  public SensWebAppApplicationTemplate(
      @NonNull String appName,
      @NonNull String[] args,
      @NonNull Class<?> source) {

    this.appName = appName;
    this.args = args;
    this.source = source;

    new ApplicationBootstrapper(appName, "sens.home", new HomeDirectoryDiscoverer("SENS_HOME"))
        .bootstrapApplication();
  }

  public void run() {
    doRun(setupApplicationBuilder());
  }

  private void doRun(SpringApplicationBuilder applicationBuilder) {
    int exitCode;
    try (ConfigurableApplicationContext context = applicationBuilder.run(args)) {
      onApplicationContext(context);

      exitCode = SpringApplication.exit(context);
    } catch (Throwable e) {
      log.warn("SENS application failed: exception={}", e.getClass().getName());
      throw e;
    } finally {
      log.info("SENS application finished");
    }

    if (exitCode != 0)
      throw new SensWebAppApplicationException(exitCode);
  }

  private SpringApplicationBuilder setupApplicationBuilder() {
    return new SpringApplicationBuilder(source)
        .bannerMode(Mode.OFF)
        .web(WebApplicationType.SERVLET);
  }

  private void onApplicationContext(ConfigurableApplicationContext context) {
    SpringApplicationContextCallback callback = new SensWebAppApplicationContextCallback();
    log.info("Running context callback");
    callback.onApplicationContext(context);
  }
}
