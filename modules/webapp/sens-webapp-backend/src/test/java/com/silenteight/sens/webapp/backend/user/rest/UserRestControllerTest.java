package com.silenteight.sens.webapp.backend.user.rest;

import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.webapp.user.list.ListUsersUseCase;
import com.silenteight.sens.webapp.user.list.ListUsersWithRoleUseCase;
import com.silenteight.sens.webapp.user.password.reset.ResetInternalUserPasswordUseCase;
import com.silenteight.sens.webapp.user.registration.RegisterInternalUserUseCase;
import com.silenteight.sens.webapp.user.remove.RemoveUserUseCase;
import com.silenteight.sens.webapp.user.roles.ListRolesUseCase;
import com.silenteight.sens.webapp.user.update.UpdateUserUseCase;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

@Import({ UserRestController.class, UserRestControllerAdvice.class })
abstract class UserRestControllerTest extends BaseRestControllerTest {

  @MockBean
  protected ListUsersUseCase listUsersUseCase;

  @MockBean
  protected RegisterInternalUserUseCase registerInternalUserUseCase;

  @MockBean
  protected UpdateUserUseCase updateUserUseCase;

  @MockBean
  protected RemoveUserUseCase removeUserUseCase;

  @MockBean
  protected ResetInternalUserPasswordUseCase resetInternalUserPasswordUseCase;

  @MockBean
  protected ListRolesUseCase listRolesUseCase;

  @MockBean
  protected ListUsersWithRoleUseCase listUsersWithRoleUseCase;
}
