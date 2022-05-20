package com.silenteight.sep.base.common.app;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.commons.app.spring.SpringApplicationContextCallback;
import com.silenteight.sep.base.common.logging.LogMarkers;

import org.springframework.context.ConfigurableApplicationContext;

@Slf4j
public class SerpApplicationContextCallback implements SpringApplicationContextCallback {

  @Override
  public void onApplicationContext(ConfigurableApplicationContext applicationContext) {
    try {
      Thread.currentThread().join();
    } catch (InterruptedException ignored) {
      log.info(LogMarkers.INFRASTRUCTURE, "Interrupted, exiting...");
      Thread.currentThread().interrupt();
    }
  }
}
