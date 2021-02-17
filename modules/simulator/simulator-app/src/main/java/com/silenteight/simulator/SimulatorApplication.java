package com.silenteight.simulator;

import com.silenteight.sep.auth.authentication.AuthenticationModule;
import com.silenteight.sep.auth.authorization.AuthorizationModule;
import com.silenteight.sep.base.common.app.SerpApplicationContextCallback;
import com.silenteight.sep.base.common.app.SerpApplicationTemplate;
import com.silenteight.simulator.common.integration.AmqpCommonModule;
import com.silenteight.simulator.common.web.WebModule;
import com.silenteight.simulator.management.ManagementModule;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.config.EnableIntegrationManagement;

@EnableAutoConfiguration
@ComponentScan(basePackageClasses = {
    // NOTE(ahaczewski): Keep list of modules alphabetically sorted within section.
    // Domain modules
    ManagementModule.class,
    // Interface modules
    AmqpCommonModule.class,
    AuthenticationModule.class,
    AuthorizationModule.class,
    WebModule.class,
})
@EnableIntegration
@EnableIntegrationManagement
public class SimulatorApplication {

  public static void main(String[] args) {
    new SerpApplicationTemplate("simulator", args, SimulatorApplication.class)
        .profiles("database", "rabbitmq", "messaging")
        .contextCallback(new SerpApplicationContextCallback())
        .runAndExit();
  }
}
