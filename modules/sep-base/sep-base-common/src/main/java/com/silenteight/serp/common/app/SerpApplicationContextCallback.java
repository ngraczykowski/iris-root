package com.silenteight.serp.common.app;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.commons.app.spring.SpringApplicationContextCallback;

import org.springframework.context.ConfigurableApplicationContext;

import static com.silenteight.serp.common.logging.LogMarkers.INFRASTRUCTURE;

@Slf4j
public class SerpApplicationContextCallback implements SpringApplicationContextCallback {

  @Override
  public void onApplicationContext(ConfigurableApplicationContext applicationContext) {
    try {
      Thread.currentThread().join();
    } catch (InterruptedException ignored) {
      log.info(INFRASTRUCTURE, "Interrupted, exiting...");
      Thread.currentThread().interrupt();
    }
  }
}
