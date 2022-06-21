package com.silenteight.sens.webapp.common.app;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.commons.app.spring.SpringApplicationContextCallback;

import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
public class SensWebAppApplicationContextCallback implements SpringApplicationContextCallback {

  @Override
  public void onApplicationContext(ConfigurableApplicationContext applicationContext) {
    try {
      Thread.currentThread().join();
    } catch (InterruptedException ignored) {
      log.info("Interrupted, exiting...");
      Thread.currentThread().interrupt();
    }
  }
}
