package com.silenteight.warehouse.management.group.domain;

import com.silenteight.warehouse.management.ManagementModule;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = ManagementModule.class)
public class CountryGroupTestConfiguration {
}
