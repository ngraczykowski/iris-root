package com.silenteight.agent.common.database.hibernate;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.apache.commons.lang3.StringUtils.containsAny;

@RequiredArgsConstructor
@Configuration
@AutoConfigureBefore(
    name = "org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration")
public class SilentEightNamingConventionConfiguration {

  private final Environment environment;

  @Bean
  SilentEightImplicitNamingStrategy silentEightImplicitNamingStrategy() {
    var appName = getApplicationName();
    var tablePrefix = capitalize(appName).replace('-', '_');
    return new SilentEightImplicitNamingStrategy(tablePrefix);
  }

  @Bean
  SilentEightPhysicalNamingStrategy silentEightPhysicalNamingStrategy() {
    var appName = getApplicationName();
    return new SilentEightPhysicalNamingStrategy(appName);
  }

  private String getApplicationName() {
    var springApplicationName = environment.getRequiredProperty("spring.application.name");

    if (containsAny(springApplicationName, '.', '_')) {
      throw new BeanCreationException("Application name contains invalid characters");
    }

    return springApplicationName.toLowerCase();
  }
}
