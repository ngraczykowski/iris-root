package com.silenteight.warehouse.test.hsbcbridgeclient.app;

import com.silenteight.commons.app.spring.ApplicationBuilderConfigurer;
import com.silenteight.commons.app.spring.ConfigurableApplicationBuilder;
import com.silenteight.commons.app.spring.DefaultSpringApplicationContextCallback;
import com.silenteight.commons.app.spring.SpringApplicationTemplate;
import com.silenteight.warehouse.test.hsbcbridgeclient.client.ClientConfiguration;
import com.silenteight.warehouse.test.hsbcbridgeclient.datageneration.DataGeneratorConfiguration;
import com.silenteight.warehouse.test.hsbcbridgeclient.usecases.UseCasesConfiguration;

import org.springframework.boot.Banner.Mode;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@ComponentScan(basePackageClasses = {
    // Keep list of modules alphabetically sorted within section.
    // Domain modules
    ClientConfiguration.class,
    DataGeneratorConfiguration.class,
    UseCasesConfiguration.class,
    HsbcBridgeClientTestApplication.class
    // Interface modules
})
public class HsbcBridgeClientTestApplication {

  public static void main(String[] args) {
    new SpringApplicationTemplate(args, HsbcBridgeClientTestApplication.class)
        .contextCallback(new DefaultSpringApplicationContextCallback())
        .runAndExit(new Configurer());
  }

  private static class Configurer implements ApplicationBuilderConfigurer {

    @Override
    public ConfigurableApplicationBuilder configure(ConfigurableApplicationBuilder builder) {
      return builder
          .bootstrapProperties("spring.application.name=warehouse-hsbc-bridge-client-test");
    }

    @Override
    public SpringApplicationBuilder customize(SpringApplicationBuilder springBuilder) {
      return springBuilder
          .bannerMode(Mode.OFF);
    }
  }
}
