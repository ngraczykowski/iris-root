package com.silenteight.sens.webapp.permission.domain;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {
    PermissionDomainConfiguration.class
})
class PermissionDomainTestConfiguration {
}
