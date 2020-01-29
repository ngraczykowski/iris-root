package com.silenteight.sens.webapp.backend.users.rest;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.backend.users.UserQuery;
import com.silenteight.sens.webapp.backend.users.registration.RegisterUserUseCase;
import com.silenteight.sens.webapp.backend.users.registration.RegisterUserUseCase.Success;
import com.silenteight.sens.webapp.backend.users.registration.UserRegistrationDomainError;
import com.silenteight.sens.webapp.backend.users.rest.dto.CreateUserDto;
import com.silenteight.sens.webapp.backend.users.rest.dto.UserDto;
import com.silenteight.sens.webapp.common.rest.RestConstants;

import io.vavr.control.Either;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(RestConstants.ROOT)
public class UserRestController {

  @NonNull
  private final RegisterUserUseCase registerUserUseCase;

  @NonNull
  private final UserQuery userQuery = pageable -> null;

  @GetMapping("/users")
  public Page<UserDto> users(Pageable pageable) {
    return userQuery.list(pageable);
  }

  @PostMapping("/users")
  public ResponseEntity<Void> create(@Valid @RequestBody CreateUserDto dto) {
    log.debug("Creating new user. {}", dto);

    Either<UserRegistrationDomainError, RegisterUserUseCase.Success> result =
        registerUserUseCase.apply(dto.toCommand());

    return result.map(Success::getUsername)
        .map(UserRestController::buildUserUri)
        .map(uri -> ResponseEntity.created(uri).<Void>build())
        .getOrElseThrow(UserRegistrationException::new);
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
