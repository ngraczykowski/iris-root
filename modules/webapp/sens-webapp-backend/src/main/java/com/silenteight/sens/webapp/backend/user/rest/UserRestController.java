package com.silenteight.sens.webapp.backend.user.rest;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.backend.security.Authority;
import com.silenteight.sens.webapp.backend.user.rest.dto.CreateUserDto;
import com.silenteight.sens.webapp.backend.user.rest.dto.TemporaryPasswordDto;
import com.silenteight.sens.webapp.backend.user.rest.dto.UpdateUserDto;
import com.silenteight.sens.webapp.user.RolesQuery;
import com.silenteight.sens.webapp.user.UserQuery;
import com.silenteight.sens.webapp.user.domain.validator.UserDomainError;
import com.silenteight.sens.webapp.user.dto.RolesDto;
import com.silenteight.sens.webapp.user.dto.UserDto;
import com.silenteight.sens.webapp.user.password.reset.ResetInternalUserPasswordUseCase;
import com.silenteight.sens.webapp.user.password.reset.ResetInternalUserPasswordUseCase.UserIsNotInternalException;
import com.silenteight.sens.webapp.user.password.reset.ResetInternalUserPasswordUseCase.UserNotFoundException;
import com.silenteight.sens.webapp.user.password.reset.TemporaryPassword;
import com.silenteight.sens.webapp.user.registration.RegisterInternalUserUseCase;
import com.silenteight.sens.webapp.user.update.UpdateUserUseCase;
import com.silenteight.sens.webapp.user.update.UpdatedUserRepository.UserUpdateException;
import com.silenteight.sens.webapp.user.update.exception.DisplayNameValidationException;
import com.silenteight.sens.webapp.user.update.exception.RolesValidationException;

import io.vavr.control.Either;
import io.vavr.control.Try;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import javax.validation.Valid;

import static com.silenteight.sens.webapp.common.rest.RestConstants.ROOT;
import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.USER_MANAGEMENT;
import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Predicates.instanceOf;
import static org.springframework.http.HttpStatus.INSUFFICIENT_STORAGE;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.ResponseEntity.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(ROOT + "/users")
class UserRestController {

  @NonNull
  private final RegisterInternalUserUseCase registerInternalUserUseCase;

  @NonNull
  private final UpdateUserUseCase updateUserUseCase;

  @NonNull
  private final ResetInternalUserPasswordUseCase resetPasswordUseCase;

  @NonNull
  private final UserQuery userQuery;

  @NonNull
  private final RolesQuery rolesQuery;

  @GetMapping
  @PreAuthorize(Authority.ADMIN)
  public Page<UserDto> users(Pageable pageable) {
    log.debug(USER_MANAGEMENT,
        "Listing users. pageNumber={},pageSize={}",
        pageable.getPageNumber(),
        pageable.getPageSize());
    return userQuery.listEnabled(pageable);
  }

  @PostMapping
  @PreAuthorize(Authority.ADMIN)
  public ResponseEntity<Void> create(@Valid @RequestBody CreateUserDto dto) {
    log.debug(USER_MANAGEMENT, "Creating new User. dto={}", dto);

    Either<UserDomainError, RegisterInternalUserUseCase.Success> result =
        registerInternalUserUseCase.apply(dto.toCommand());

    if (result.isRight())
      log.debug(USER_MANAGEMENT, "Successfully created new User. dto={}", dto);
    else
      log.error(USER_MANAGEMENT, "User creation error. dto={}", dto);

    return result
        .map(RegisterInternalUserUseCase.Success::getUsername)
        .map(UserRestController::buildUserUri)
        .map(uri -> created(uri).<Void>build())
        .getOrElseThrow(UserRegistrationException::new);
  }

  @PatchMapping("/{username}")
  @PreAuthorize(Authority.ADMIN)
  public ResponseEntity<Void> update(
      @PathVariable String username, @Valid @RequestBody UpdateUserDto dto) {
    log.debug(USER_MANAGEMENT, "Updating user. username={}, body={}", username, dto);

    return Try.run(() -> updateUserUseCase.apply(dto.toCommand(username)))
        .onSuccess(ignore -> log.debug(USER_MANAGEMENT, "Updated user. username={}", username))
        .onFailure(
            e -> log.error(USER_MANAGEMENT, "Could not update user. username={}", username, e))
        .mapTry(ignore -> new ResponseEntity<Void>(NO_CONTENT))
        .recover(UserUpdateException.class, e -> status(INSUFFICIENT_STORAGE).build())
        .recover(DisplayNameValidationException.class, e -> status(UNPROCESSABLE_ENTITY).build())
        .recover(RolesValidationException.class, e -> status(UNPROCESSABLE_ENTITY).build())
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

  @PatchMapping("/{username}/password/reset")
  public ResponseEntity<TemporaryPasswordDto> resetPassword(@PathVariable String username) {
    log.debug(USER_MANAGEMENT, "Resetting password for a user. username={}", username);
    Try<TemporaryPassword> result = Try.of(() -> resetPasswordUseCase.execute(username));

    if (result.isSuccess()) {
      log.debug(USER_MANAGEMENT, "Password has been reset. username={}", username);
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
  public ResponseEntity<RolesDto> roles() {
    log.debug(USER_MANAGEMENT, "Listing roles");
    return ok(rolesQuery.list());
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
