package com.silenteight.sens.webapp.backend.user.rest;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.backend.user.rest.dto.CreateUserDto;
import com.silenteight.sens.webapp.user.RolesQuery;
import com.silenteight.sens.webapp.user.UserQuery;
import com.silenteight.sens.webapp.user.dto.RolesDto;
import com.silenteight.sens.webapp.user.dto.UserDto;
import com.silenteight.sens.webapp.user.registration.RegisterInternalUserUseCase;
import com.silenteight.sens.webapp.user.registration.domain.UserRegistrationDomainError;

import io.vavr.control.Either;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import javax.validation.Valid;

import static com.silenteight.sens.webapp.common.rest.RestConstants.ROOT;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(ROOT + "/users")
class UserRestController {

  @NonNull
  private final RegisterInternalUserUseCase registerInternalUserUseCase;

  @NonNull
  private final UserQuery userQuery;

  @NonNull
  private final RolesQuery rolesQuery;

  @GetMapping
  public Page<UserDto> users(Pageable pageable) {
    return userQuery.list(pageable);
  }

  @PostMapping
  public ResponseEntity<Void> create(@Valid @RequestBody CreateUserDto dto) {
    log.debug("Creating new user. {}", dto);

    Either<UserRegistrationDomainError, RegisterInternalUserUseCase.Success> result =
        registerInternalUserUseCase.apply(dto.toCommand());

    return result
        .map(RegisterInternalUserUseCase.Success::getUsername)
        .map(UserRestController::buildUserUri)
        .map(uri -> ResponseEntity.created(uri).<Void>build())
        .getOrElseThrow(UserRegistrationException::new);
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
