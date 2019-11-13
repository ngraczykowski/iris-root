package com.silenteight.sens.webapp.backend.config.security;

import com.silenteight.sens.webapp.security.SecurityModule;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = SecurityModule.class)
public class SecurityConfiguration {

}
