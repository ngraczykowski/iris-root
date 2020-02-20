package com.silenteight.sens.webapp.user.update;

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
  AddRolesToUserUseCase addRolesToUserUseCase(UpdatedUserRepository updatedUserRepository) {
    return new AddRolesToUserUseCase(updatedUserRepository);
  }
}
