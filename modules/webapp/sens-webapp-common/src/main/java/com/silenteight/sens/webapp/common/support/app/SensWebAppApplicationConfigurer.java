package com.silenteight.sens.webapp.common.support.app;

import org.springframework.boot.builder.SpringApplicationBuilder;

public interface SensWebAppApplicationConfigurer {

  SensWebAppApplicationBuilder configure(SensWebAppApplicationBuilder builder);

  default SpringApplicationBuilder customize(SpringApplicationBuilder springBuilder) {
    return springBuilder;
  }
}
