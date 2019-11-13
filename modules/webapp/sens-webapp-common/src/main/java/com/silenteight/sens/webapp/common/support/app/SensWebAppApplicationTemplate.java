package com.silenteight.sens.webapp.common.support.app;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javax.annotation.Nullable;

@Slf4j
public class SensWebAppApplicationTemplate {

  private final String[] args;
  private final Class<?>[] sources;

  private SensWebAppApplicationContextHandler contextHandler;

  public SensWebAppApplicationTemplate(@NonNull String[] args, @NonNull Class<?>... sources) {
    this.args = args;
    this.sources = sources;
  }

  public SensWebAppApplicationTemplate contextHandler(
      @Nullable SensWebAppApplicationContextHandler contextHandler) {
    this.contextHandler = contextHandler;
    return this;
  }

  public void run() {
    run(null);
  }

  public void run(@Nullable SensWebAppApplicationConfigurer configurer) {
    doRun(setupApplication(configurer));
  }

  private void doRun(ConfiguredApplication application) {
    int exitCode;
    try (ConfigurableApplicationContext context = application.run(args)) {
      if (contextHandler != null)
        contextHandler.handle(context);

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

  private ConfiguredApplication setupApplication(@Nullable SensWebAppApplicationConfigurer configurer) {
    SensWebAppApplicationBuilder sensBuilder = new SensWebAppApplicationBuilder();

    if (configurer != null)
      sensBuilder = configurer.configure(sensBuilder);

    SpringApplicationBuilder springBuilder = sensBuilder.createSpringBuilder(sources);

    if (configurer != null)
      springBuilder = configurer.customize(springBuilder);

    return new ConfiguredApplication(sensBuilder.getSystemPropertiesSetter(), springBuilder);
  }

  @RequiredArgsConstructor
  private static final class ConfiguredApplication {

    @NonNull
    private final Runnable systemPropertiesSetter;
    @NonNull
    private final SpringApplicationBuilder springApplicationBuilder;

    ConfigurableApplicationContext run(String[] args) {
      systemPropertiesSetter.run();
      return springApplicationBuilder.run(args);
    }
  }
}
