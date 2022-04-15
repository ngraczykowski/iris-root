package com.silenteight.sens.webapp.backend.user.rest;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.backend.user.rest.dto.CreateUserDto;
import com.silenteight.sens.webapp.backend.user.rest.dto.TemporaryPasswordDto;
import com.silenteight.sens.webapp.backend.user.rest.dto.UpdateUserDto;
import com.silenteight.sens.webapp.user.list.ListUsersUseCase;
import com.silenteight.sens.webapp.user.list.ListUsersWithRoleUseCase;
import com.silenteight.sens.webapp.user.list.UserListDto;
import com.silenteight.sens.webapp.user.password.reset.ResetInternalUserPasswordUseCase;
import com.silenteight.sens.webapp.user.password.reset.ResetInternalUserPasswordUseCase.UserIsNotInternalException;
import com.silenteight.sens.webapp.user.password.reset.ResetInternalUserPasswordUseCase.UserNotFoundException;
import com.silenteight.sens.webapp.user.registration.RegisterInternalUserUseCase;
import com.silenteight.sens.webapp.user.remove.RemoveUserUseCase;
import com.silenteight.sens.webapp.user.remove.RemoveUserUseCase.RemoveUserCommand;
import com.silenteight.sens.webapp.user.roles.ListRolesUseCase;
import com.silenteight.sens.webapp.user.update.UpdateUserUseCase;
import com.silenteight.sens.webapp.user.update.exception.DisplayNameValidationException;
import com.silenteight.sep.usermanagement.api.credentials.dto.TemporaryPassword;
import com.silenteight.sep.usermanagement.api.error.UserDomainError;
import com.silenteight.sep.usermanagement.api.role.RoleValidationException;
import com.silenteight.sep.usermanagement.api.role.dto.RolesDto;
import com.silenteight.sep.usermanagement.api.user.UserUpdater.UserUpdateException;


import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.vavr.control.Either;
import io.vavr.control.Try;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import static com.silenteight.sens.webapp.common.rest.RestConstants.*;
import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.USER_MANAGEMENT;
import static com.silenteight.sens.webapp.user.domain.DomainConstants.USER_ENDPOINT_TAG;
import static com.silenteight.sep.usermanagement.api.origin.SensOrigin.SENS_ORIGIN;
import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Predicates.instanceOf;
import static org.springframework.http.HttpStatus.INSUFFICIENT_STORAGE;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.ResponseEntity.*;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = ROOT + "/users", produces = APPLICATION_JSON_VALUE)
@Tag(name = USER_ENDPOINT_TAG)
class UserRestController {

  @NonNull
  private final ListUsersUseCase listUsersUseCase;

  @NonNull
  private final RegisterInternalUserUseCase registerInternalUserUseCase;

  @NonNull
  private final UpdateUserUseCase updateUserUseCase;

  @NonNull
  private final RemoveUserUseCase removeUserUseCase;

  @NonNull
  private final ResetInternalUserPasswordUseCase resetPasswordUseCase;

  @NonNull
  private final ListRolesUseCase listRolesUseCase;

  @NonNull
  private final ListUsersWithRoleUseCase listUsersWithRoleUseCase;

  @GetMapping
  @PreAuthorize("isAuthorized('LIST_USERS')")
  public List<UserListDto> users() {
    log.info(USER_MANAGEMENT, "Listing users");
    return listUsersUseCase.apply();
  }

  //TODO(jobarymski): this needs to be removed once the frontend switches
  // to the users endpoint above
  @Deprecated
  @GetMapping("/pageable")
  @PreAuthorize("isAuthorized('LIST_USERS')")
  public Page<UserListDto> pageableUsers(Pageable pageable) {
    log.info(
        USER_MANAGEMENT,
        "Listing users. pageNumber={},pageSize={}",
        pageable.getPageNumber(),
        pageable.getPageSize());
    List<UserListDto> users = listUsersUseCase.apply();
    return new PageImpl<>(users, pageable, users.size());
  }

  @PostMapping
  @PreAuthorize("isAuthorized('CREATE_USER')")
  @ApiResponses(value = {
      @ApiResponse(responseCode = CREATED_STATUS, description = SUCCESS_RESPONSE_DESCRIPTION)
  })
  public ResponseEntity<Void> create(@Valid @RequestBody CreateUserDto dto) {
    log.info(USER_MANAGEMENT, "Creating new User. dto={}", dto);

    Either<UserDomainError, RegisterInternalUserUseCase.Success> result =
        registerInternalUserUseCase.apply(dto.toCommand());

    if (result.isRight())
      log.info(USER_MANAGEMENT, "Successfully created new User");
    else
      log.error(USER_MANAGEMENT, "User creation error. reason={}", result.getLeft().getReason());

    return result
        .map(RegisterInternalUserUseCase.Success::getUsername)
        .map(UserRestController::buildUserUri)
        .map(uri -> created(uri).<Void>build())
        .getOrElseThrow(UserRegistrationException::new);
  }

  @PatchMapping("/{username}")
  @PreAuthorize("isAuthorized('EDIT_USER')")
  @ApiResponses(value = {
      @ApiResponse(responseCode = NO_CONTENT_STATUS, description = SUCCESS_RESPONSE_DESCRIPTION),
      @ApiResponse(responseCode = UNPROCESSABLE_ENTITY_STATUS, description = "invalid data"),
      @ApiResponse(responseCode = INSUFFICIENT_STORAGE_STATUS, description = "error during update")
  })
  public ResponseEntity<Void> update(
      @PathVariable String username, @Valid @RequestBody UpdateUserDto dto) {
    log.info(USER_MANAGEMENT, "Updating user. username={}, body={}", username, dto);

    return Try.run(() -> updateUserUseCase.apply(dto.toCommand(username)))
        .onSuccess(ignore -> log.info(USER_MANAGEMENT, "Updated user. username={}", username))
        .onFailure(
            e -> log.error(USER_MANAGEMENT, "Could not update user. username={}", username, e))
        .mapTry(ignore -> new ResponseEntity<Void>(NO_CONTENT))
        .recover(UserUpdateException.class, e -> status(INSUFFICIENT_STORAGE).build())
        .recover(DisplayNameValidationException.class, e -> status(UNPROCESSABLE_ENTITY).build())
        .recover(RoleValidationException.class, e -> status(UNPROCESSABLE_ENTITY).build())
        .getOrElse(status(INTERNAL_SERVER_ERROR).build());
  }

  @NotNull
  private static URI buildUserUri(String username) {
    return ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(username)
        .toUri();
  }

  @DeleteMapping("/{username}")
  @PreAuthorize("isAuthorized('REMOVE_USER')")
  @ApiResponses(value = {
      @ApiResponse(responseCode = NO_CONTENT_STATUS, description = SUCCESS_RESPONSE_DESCRIPTION)
  })
  public ResponseEntity<Void> delete(@PathVariable String username) {
    log.info(USER_MANAGEMENT, "Deleting User {}", username);
    return Try.run(
        () -> removeUserUseCase.apply(
            RemoveUserCommand.builder().username(username).expectedOrigin(SENS_ORIGIN).build()))
        .mapTry(ignore -> new ResponseEntity<Void>(NO_CONTENT))
        .get();
  }

  @PatchMapping("/{username}/password/reset")
  @PreAuthorize("isAuthorized('RESET_USER_PASSWORD')")
  public ResponseEntity<TemporaryPasswordDto> resetPassword(@PathVariable String username) {
    log.info(USER_MANAGEMENT, "Resetting password for a user. username={}", username);
    Try<TemporaryPassword> result = Try.of(() -> resetPasswordUseCase.execute(username));

    if (result.isSuccess()) {
      log.info(USER_MANAGEMENT, "Password has been reset. username={}", username);
      return ok(result.map(TemporaryPasswordDto::from).get());
    }

    Throwable problem = result.getCause();
    log.error(USER_MANAGEMENT, "Could not reset password. username={}", username, problem);

    return Match(problem).of(
        Case($(instanceOf(UserNotFoundException.class)), () -> notFound().build()),
        Case($(instanceOf(UserIsNotInternalException.class)), () -> badRequest().build()),
        Case($(), () -> status(INTERNAL_SERVER_ERROR).build()));
  }

  @GetMapping("/roles")
  @PreAuthorize("isAuthorized('LIST_ROLES')")
  public ResponseEntity<RolesDto> roles() {
    log.info(USER_MANAGEMENT, "Listing roles");
    return ok(listRolesUseCase.apply());
  }

  @GetMapping("/role/{roleName}")
  @PreAuthorize("isAuthorized('LIST_USERS')")
  public List<UserListDto> listUsersWithRole(@PathVariable String roleName) {
    log.info(USER_MANAGEMENT, "Listing users with roleName={}", roleName);
    return listUsersWithRoleUseCase.apply(roleName);
  }

  @Getter
  static class UserRegistrationException extends RuntimeException {

    private static final long serialVersionUID = -2018001506771795525L;

    private final UserDomainError error;

    UserRegistrationException(UserDomainError error) {
      super(error.getReason());
      this.error = error;
    }
  }
}
