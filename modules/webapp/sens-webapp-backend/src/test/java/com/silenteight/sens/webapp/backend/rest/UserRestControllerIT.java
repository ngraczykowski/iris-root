package com.silenteight.sens.webapp.backend.rest;

import com.silenteight.sens.webapp.backend.presentation.dto.user.dto.AnalystDto;
import com.silenteight.sens.webapp.backend.presentation.dto.user.dto.CreateAnalystsDto;
import com.silenteight.sens.webapp.backend.presentation.dto.user.dto.CreateUserDto;
import com.silenteight.sens.webapp.backend.presentation.dto.user.dto.ModifyUserDto;
import com.silenteight.sens.webapp.common.rest.RestConstants;
import com.silenteight.sens.webapp.kernel.security.authority.Role;
import com.silenteight.sens.webapp.users.bulk.BulkUserManagementService;
import com.silenteight.sens.webapp.users.user.UserService;

import org.hamcrest.CustomTypeSafeMatcher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;

import static com.silenteight.sens.webapp.backend.rest.UsersDataInitializer.ANALYST;
import static com.silenteight.sens.webapp.backend.rest.UsersDataInitializer.USER_MANAGER;
import static java.util.Collections.singletonList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRestControllerIT extends BaseRestControllerIT {

  @SpyBean
  private UserService userService;

  @SpyBean
  private BulkUserManagementService bulkUserManagementService;

  @Test
  void shouldEndWithForbidden_whenCreateUserAsUserWithoutUserManagerRole() {
    executeAs(ANALYST, () -> performPost("/user", CreateUserDto.builder().userName("user").build())
        .statusCode(HttpStatus.FORBIDDEN.value()));
  }

  @Test
  void shouldEndWithForbidden_whenModifyUserAsUserWithoutUserManagerRole() {
    executeAs(ANALYST, () -> performPost("/user/1", ModifyUserDto.builder().build())
        .statusCode(HttpStatus.FORBIDDEN.value()));
  }

  @Test
  void shouldEndWithForbidden_whenListUsersAsUserWithoutUserManagerRole() {
    executeAs(ANALYST, () -> performGet("/users")
        .statusCode(HttpStatus.FORBIDDEN.value()));
  }

  @Test
  void shouldCreateNewUserSuccessfully() {
    executeAs(USER_MANAGER, () -> {
      CreateUserDto request = createUserRequest("newUser");

      performPost("/user", request)
          .statusCode(HttpStatus.CREATED.value())
          .header("Location", matchesPattern("^.*" + RestConstants.ROOT + "/user/[0-9]+$"));

      verify(userService).create(request.getDomainRequest());
    });
  }

  @Test
  void shouldEndWithConflict_whenCreateUserWithExistingName() {
    executeAs(USER_MANAGER, () -> {
      CreateUserDto request = createUserRequest(USER_MANAGER);

      performPost("/user", request)
          .statusCode(HttpStatus.CONFLICT.value());

      verify(userService).create(request.getDomainRequest());
    });
  }

  @Test
  void shouldCreateAnalystsSuccessfully() {
    executeAs(USER_MANAGER, () -> {
      CreateAnalystsDto request = createAnalystsRequest("analyst", "Analyst Name");

      performPost("/users/analysts", request)
          .statusCode(HttpStatus.OK.value());

      verify(bulkUserManagementService).synchronizeAnalysts(request.toDomainRequest());
    });
  }

  private static CreateUserDto createUserRequest(String userName) {
    return CreateUserDto
        .builder()
        .userName(userName)
        .password("password")
        .displayName("displayName")
        .superUser(false)
        .roles(singletonList(Role.ROLE_MAKER))
        .build();
  }

  private static CustomTypeSafeMatcher<String> matchesPattern(String regex) {
    return new CustomTypeSafeMatcher<>("matching the pattern " + regex) {

      @Override
      protected boolean matchesSafely(String s) {
        return s.matches(regex);
      }
    };
  }

  private static CreateAnalystsDto createAnalystsRequest(String userName, String displayName) {
    return CreateAnalystsDto
        .builder()
        .analysts(singletonList(createAnalyst(userName, displayName)))
        .build();
  }

  private static AnalystDto createAnalyst(String userName, String displayName) {
    return AnalystDto
        .builder()
        .userName(userName)
        .displayName(displayName)
        .build();
  }
}
