package com.silenteight.serp.universalbridge;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.common.app.SerpApplicationContextCallback;
import com.silenteight.sep.base.common.app.SerpApplicationTemplate;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAutoConfiguration
@EnableAsync
@Configuration
@Slf4j
@ComponentScan
public class UniversalBridgeApplication {

  public static void main(String[] args) {
    new SerpApplicationTemplate("universal-bridge", args, UniversalBridgeApplication.class)
        .profiles("database", "rabbitmq")
        .contextCallback(new SerpApplicationContextCallback())
        .runAndExit();
  }
}
