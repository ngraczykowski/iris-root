package com.silenteight.hsbc.bridge;

import com.silenteight.hsbc.bridge.alert.AlertModule;
import com.silenteight.hsbc.bridge.bulk.BulkModule;
import com.silenteight.hsbc.bridge.match.MatchModule;
import com.silenteight.hsbc.bridge.rest.RestModule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableAutoConfiguration
@EnableJpaRepositories
@ComponentScan(basePackageClasses = {
    AlertModule.class,
    BulkModule.class,
    MatchModule.class,
    RestModule.class
})
public class BridgeApplication {

  public static void main(String[] args) {
    SpringApplication.run(BridgeApplication.class, args);
  }
}
