package com.silenteight.warehouse.test.app;

import com.silenteight.commons.app.spring.ApplicationBuilderConfigurer;
import com.silenteight.commons.app.spring.ConfigurableApplicationBuilder;
import com.silenteight.commons.app.spring.DefaultSpringApplicationContextCallback;
import com.silenteight.commons.app.spring.SpringApplicationTemplate;
import com.silenteight.warehouse.test.client.TestClientModule;
import com.silenteight.warehouse.test.generator.TestGeneratorModule;
import com.silenteight.warehouse.test.integration.TestAmqpCommonModule;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.config.EnableIntegrationManagement;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAutoConfiguration
@ComponentScan(basePackageClasses = {
    // NOTE(ahaczewski): Keep list of modules alphabetically sorted within section.
    // Domain modules
    TestGeneratorModule.class,
    TestClientModule.class,
    // Interface modules
    TestAmqpCommonModule.class
})
@EnableIntegration
@EnableIntegrationManagement
@EnableScheduling
public class WarehouseTestApplication {

  public static void main(String[] args) {
    new SpringApplicationTemplate(args, WarehouseTestApplication.class)
        .contextCallback(new DefaultSpringApplicationContextCallback())
        .runAndExit(new Configurer());
  }

  private static class Configurer implements ApplicationBuilderConfigurer {

    @Override
    public ConfigurableApplicationBuilder configure(ConfigurableApplicationBuilder builder) {
      return builder
          .bootstrapProperties("spring.application.name=warehouse-test-client");
    }

    @Override
    public SpringApplicationBuilder customize(SpringApplicationBuilder springBuilder) {
      return springBuilder
          .bannerMode(Mode.OFF);
    }
  }
}
