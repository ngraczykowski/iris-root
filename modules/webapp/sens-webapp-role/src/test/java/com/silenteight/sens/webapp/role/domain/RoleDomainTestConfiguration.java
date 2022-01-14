package com.silenteight.sens.webapp.role.domain;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {
    RoleDomainConfiguration.class
})
class RoleDomainTestConfiguration {
}
