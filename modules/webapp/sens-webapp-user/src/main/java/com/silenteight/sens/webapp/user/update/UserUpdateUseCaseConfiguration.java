package com.silenteight.sens.webapp.user.update;

import com.silenteight.sens.webapp.audit.api.trace.AuditTracer;
import com.silenteight.sens.webapp.user.config.RolesProperties;
import com.silenteight.sens.webapp.user.domain.validator.NameLengthValidator;
import com.silenteight.sens.webapp.user.roles.UserRolesRetriever;
import com.silenteight.sep.usermanagement.api.role.RoleValidator;
import com.silenteight.sep.usermanagement.api.user.UserQuery;
import com.silenteight.sep.usermanagement.api.user.UserUpdater;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class UserUpdateUseCaseConfiguration {

  @Bean
  UpdateUserDisplayNameUseCase updateUserDisplayNameUseCase(
      UserUpdater userUpdater,
      AuditTracer auditTracer,
      UserRolesRetriever userRolesRetriever,
      RolesProperties rolesProperties) {

    return new UpdateUserDisplayNameUseCase(
        userUpdater, auditTracer, userRolesRetriever, rolesProperties.getRolesScope());
  }

  @Bean
  AddRolesToUserUseCase addRolesToUserUseCase(
      UserUpdater userUpdater,
      UserQuery userQuery,
      AuditTracer auditTracer,
      RolesProperties rolesProperties) {

    return new AddRolesToUserUseCase(
        userUpdater,
        userQuery,
        auditTracer,
        rolesProperties.getRolesScope(),
        rolesProperties.getCountryGroupsScope());
  }

  @Bean
  UpdateUserUseCase updateUserUseCase(
      UserUpdater userUpdater,
      RoleValidator roleValidator,
      @Qualifier("displayNameLengthValidator") NameLengthValidator displayNameLengthValidator,
      AuditTracer auditTracer,
      UserRolesRetriever userRolesRetriever,
      RolesProperties rolesProperties) {

    return new UpdateUserUseCase(
        userUpdater,
        displayNameLengthValidator,
        roleValidator,
        auditTracer,
        userRolesRetriever,
        rolesProperties.getRolesScope(),
        rolesProperties.getCountryGroupsScope(),
        rolesProperties.getDefaultCountryGroupRole());
  }
}
