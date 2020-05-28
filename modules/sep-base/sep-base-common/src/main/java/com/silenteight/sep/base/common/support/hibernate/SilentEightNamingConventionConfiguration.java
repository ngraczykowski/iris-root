package com.silenteight.sep.base.common.support.hibernate;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.apache.commons.lang3.StringUtils.containsAny;

@RequiredArgsConstructor
@Configuration
@ConditionalOnClass(name = "org.hibernate.SessionFactory")
@AutoConfigureBefore(
    name = "org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration")
public class SilentEightNamingConventionConfiguration {

  private final Environment environment;

  @Bean
  SilentEightImplicitNamingStrategy silentEightImplicitNamingStrategy() {
    String appName = getApplicationName();
    String tablePrefix = capitalize(appName).replace("-", "_");
    return new SilentEightImplicitNamingStrategy(tablePrefix);
  }

  @Bean
  SilentEightPhysicalNamingStrategy silentEightPhysicalNamingStrategy() {
    String appName = getApplicationName();
    return new SilentEightPhysicalNamingStrategy(appName);
  }

  private String getApplicationName() {
    String springApplicationName = environment.getRequiredProperty("spring.application.name");

    if (containsAny(springApplicationName, '.', '_'))
      throw new BeanCreationException("Application name contains invalid characters");

    return springApplicationName.toLowerCase();
  }
}
