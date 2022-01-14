package com.silenteight.warehouse.management.group.domain;

import com.silenteight.warehouse.common.domain.DomainModule;
import com.silenteight.warehouse.common.opendistro.roles.RoleService;
import com.silenteight.warehouse.common.opendistro.roles.RolesMappingService;
import com.silenteight.warehouse.management.ManagementModule;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import static org.mockito.Mockito.*;

@ComponentScan(basePackageClasses = {
    ManagementModule.class,
    DomainModule.class
})
class CountryGroupTestConfiguration {

  @Bean
  RoleService roleService() {
    return mock(RoleService.class);
  }

  @Bean
  RolesMappingService rolesMappingService() {
    return mock(RolesMappingService.class);
  }
}
