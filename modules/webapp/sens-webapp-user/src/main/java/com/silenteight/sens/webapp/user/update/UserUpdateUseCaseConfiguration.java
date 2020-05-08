package com.silenteight.sens.webapp.user.update;

import com.silenteight.sens.webapp.audit.trace.AuditTracer;
import com.silenteight.sens.webapp.user.UserQuery;
import com.silenteight.sens.webapp.user.domain.validator.NameLengthValidator;
import com.silenteight.sens.webapp.user.domain.validator.RolesValidator;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class UserUpdateUseCaseConfiguration {

  @Bean
  UpdateUserDisplayNameUseCase updateUserDisplayNameUseCase(
      UpdatedUserRepository updatedUserRepository, AuditTracer auditTracer) {

    return new UpdateUserDisplayNameUseCase(updatedUserRepository, auditTracer);
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
      AuditTracer auditTracer) {

    return new UpdateUserUseCase(
        updatedUserRepository, displayNameLengthValidator, rolesValidator, auditTracer);
  }
}
