package com.silenteight.sens.webapp.backend.rest;

import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.backend.presentation.dto.user.dto.ModifyUserDto;
import com.silenteight.sens.webapp.common.rest.RestConstants;
import com.silenteight.sens.webapp.users.user.dto.UserDetailedView;
import com.silenteight.sens.webapp.users.user.dto.UserResponseView;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static java.util.Collections.emptyList;

@RequiredArgsConstructor
@RestController
@RequestMapping(RestConstants.ROOT)
@PreAuthorize("hasAuthority('USER_MANAGE')")
public class UserRestController {

  @GetMapping("/users")
  @PreAuthorize("hasAuthority('USER_VIEW')")
  public ResponseEntity<UserResponseView> getUsers(Pageable pageable) {
    UserResponseView response = UserResponseView
        .builder()
        .total(0)
        .results(emptyList())
        .build();
    return ResponseEntity.ok(response);
  }

  @GetMapping("/user/{id}")
  public ResponseEntity<UserDetailedView> getUser(@PathVariable long id) {
    UserDetailedView response = null;
    return ResponseEntity.ok(response);
  }

  @PostMapping("/user/{id}")
  public ResponseEntity<Void> modify(
      @PathVariable long id, @Valid @RequestBody ModifyUserDto dto) {

    return ResponseEntity.accepted().build();
  }
}
