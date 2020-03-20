package com.silenteight.sens.webapp.backend.user.rest;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.backend.security.Authority;
import com.silenteight.sens.webapp.backend.user.rest.dto.CreateUserDto;
import com.silenteight.sens.webapp.backend.user.rest.dto.TemporaryPasswordDto;
import com.silenteight.sens.webapp.user.RolesQuery;
import com.silenteight.sens.webapp.user.UserQuery;
import com.silenteight.sens.webapp.user.dto.RolesDto;
import com.silenteight.sens.webapp.user.dto.UserDto;
import com.silenteight.sens.webapp.user.password.ResetInternalUserPasswordUseCase;
import com.silenteight.sens.webapp.user.password.ResetInternalUserPasswordUseCase.UserIsNotInternalException;
import com.silenteight.sens.webapp.user.password.ResetInternalUserPasswordUseCase.UserNotFoundException;
import com.silenteight.sens.webapp.user.password.TemporaryPassword;
import com.silenteight.sens.webapp.user.registration.RegisterInternalUserUseCase;
import com.silenteight.sens.webapp.user.registration.domain.UserRegistrationDomainError;

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
import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Predicates.instanceOf;
import static org.springframework.http.ResponseEntity.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(ROOT + "/users")
class UserRestController {

  @NonNull
  private final RegisterInternalUserUseCase registerInternalUserUseCase;

  @NonNull
  private final ResetInternalUserPasswordUseCase resetPasswordUseCase;

  @NonNull
  private final UserQuery userQuery;

  @NonNull
  private final RolesQuery rolesQuery;

  @GetMapping
  @PreAuthorize(Authority.ADMIN)
  public Page<UserDto> users(Pageable pageable) {
    return userQuery.listEnabled(pageable);
  }

  @PostMapping
  @PreAuthorize(Authority.ADMIN)
  public ResponseEntity<Void> create(@Valid @RequestBody CreateUserDto dto) {
    log.debug("Creating new user. {}", dto);

    Either<UserRegistrationDomainError, RegisterInternalUserUseCase.Success> result =
        registerInternalUserUseCase.apply(dto.toCommand());

    return result
        .map(RegisterInternalUserUseCase.Success::getUsername)
        .map(UserRestController::buildUserUri)
        .map(uri -> created(uri).<Void>build())
        .getOrElseThrow(UserRegistrationException::new);
  }

  @PatchMapping("/{username}/password/reset")
  public ResponseEntity<TemporaryPasswordDto> resetPassword(@PathVariable String username) {
    log.debug("Reset password. username={}", username);
    Try<TemporaryPassword> result = Try.of(() -> resetPasswordUseCase.execute(username));

    if (result.isSuccess())
      return ok(result.map(TemporaryPasswordDto::from).get());

    Throwable problem = result.getCause();
    log.error("Could not reset password for user {}", username, problem);

    return Match(problem).of(
        Case($(instanceOf(UserNotFoundException.class)), () -> notFound().build()),
        Case($(instanceOf(UserIsNotInternalException.class)), () -> badRequest().build()),
        Case($(), () -> status(500).build()));
  }

  @GetMapping("/roles")
  public ResponseEntity<RolesDto> roles() {
    return ok(rolesQuery.list());
  }

  @NotNull
  private static URI buildUserUri(String username) {
    return ServletUriComponentsBuilder
        .fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(username)
        .toUri();
  }

  @Getter
  static class UserRegistrationException extends RuntimeException {

    private static final long serialVersionUID = -2018001506771795525L;

    private final UserRegistrationDomainError error;

    UserRegistrationException(UserRegistrationDomainError error) {
      super(error.getReason());
      this.error = error;
    }
  }
}
