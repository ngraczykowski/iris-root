package com.silenteight.simulator.management;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = ManagementModule.class)
class ManagementTestConfiguration {
}
