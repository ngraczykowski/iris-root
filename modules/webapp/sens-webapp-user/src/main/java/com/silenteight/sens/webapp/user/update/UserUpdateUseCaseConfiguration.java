package com.silenteight.sens.webapp.user.update;

import com.silenteight.sens.webapp.audit.trace.AuditTracer;
import com.silenteight.sens.webapp.user.domain.validator.NameLengthValidator;
import com.silenteight.sens.webapp.user.roles.UserRolesRetriever;
import com.silenteight.sep.usermanagement.api.RolesValidator;
import com.silenteight.sep.usermanagement.api.UpdatedUserRepository;
import com.silenteight.sep.usermanagement.api.UserQuery;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class UserUpdateUseCaseConfiguration {

  @Bean
  UpdateUserDisplayNameUseCase updateUserDisplayNameUseCase(
      UpdatedUserRepository updatedUserRepository,
      AuditTracer auditTracer,
      UserRolesRetriever userRolesRetriever) {

    return new UpdateUserDisplayNameUseCase(updatedUserRepository, auditTracer, userRolesRetriever);
  }

  @Bean
  AddRolesToUserUseCase addRolesToUserUseCase(
      UpdatedUserRepository updatedUserRepository, UserQuery userQuery, AuditTracer auditTracer) {
    return new AddRolesToUserUseCase(updatedUserRepository, userQuery, auditTracer);
  }

  @Bean
  UpdateUserUseCase updateUserUseCase(
      UpdatedUserRepository updatedUserRepository,
      RolesValidator rolesValidator,
      @Qualifier("displayNameLengthValidator") NameLengthValidator displayNameLengthValidator,
      AuditTracer auditTracer,
      UserRolesRetriever userRolesRetriever) {

    return new UpdateUserUseCase(
        updatedUserRepository, 
        displayNameLengthValidator, 
        rolesValidator, 
        auditTracer, 
        userRolesRetriever);
  }
}
