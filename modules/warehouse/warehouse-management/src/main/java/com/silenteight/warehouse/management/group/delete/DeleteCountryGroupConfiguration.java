package com.silenteight.warehouse.management.group.delete;

import com.silenteight.warehouse.common.opendistro.roles.RoleService;
import com.silenteight.warehouse.common.opendistro.roles.RolesMappingService;
import com.silenteight.warehouse.management.group.domain.CountryGroupService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeleteCountryGroupConfiguration {

  @Bean
  DeleteCountryGroupUseCase deleteCountryGroupUseCase(
      CountryGroupService groupService, RolesMappingService rolesMappingService,
      RoleService roleService) {

    return new DeleteCountryGroupUseCase(groupService, rolesMappingService, roleService);
  }
}
