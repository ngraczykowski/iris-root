package com.silenteight.sens.webapp.user.update;

import com.silenteight.sens.webapp.audit.api.AuditLog;
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
      UpdatedUserRepository updatedUserRepository) {

    return new UpdateUserDisplayNameUseCase(updatedUserRepository);
  }

  @Bean
  AddRolesToUserUseCase addRolesToUserUseCase(
      UpdatedUserRepository updatedUserRepository, UserQuery userQuery) {
    return new AddRolesToUserUseCase(updatedUserRepository, userQuery);
  }

  @Bean
  UpdateUserUseCase updateUserUseCase(
      UpdatedUserRepository updatedUserRepository,
      RolesValidator rolesValidator,
      @Qualifier("displayNameLengthValidator") NameLengthValidator displayNameLengthValidator,
      AuditLog auditLog) {
    return new UpdateUserUseCase(updatedUserRepository, displayNameLengthValidator, rolesValidator,
        auditLog);
  }
}
