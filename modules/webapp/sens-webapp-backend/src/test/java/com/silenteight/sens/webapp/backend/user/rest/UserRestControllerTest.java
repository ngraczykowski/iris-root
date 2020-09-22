package com.silenteight.sens.webapp.backend.user.rest;

import com.silenteight.sens.webapp.common.testing.rest.BaseRestControllerTest;
import com.silenteight.sens.webapp.user.password.reset.ResetInternalUserPasswordUseCase;
import com.silenteight.sens.webapp.user.registration.RegisterInternalUserUseCase;
import com.silenteight.sens.webapp.user.update.UpdateUserUseCase;
import com.silenteight.sep.usermanagement.api.RolesQuery;
import com.silenteight.sep.usermanagement.api.UserQuery;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

@Import({ UserRestController.class, UserRestControllerAdvice.class })
abstract class UserRestControllerTest extends BaseRestControllerTest {

  @MockBean
  protected RegisterInternalUserUseCase registerInternalUserUseCase;

  @MockBean
  protected UpdateUserUseCase updateUserUseCase;

  @MockBean
  protected UserQuery userQuery;

  @MockBean
  protected RolesQuery rolesQuery;

  @MockBean
  protected ResetInternalUserPasswordUseCase resetInternalUserPasswordUseCase;
}
