package com.silenteight.sens.webapp.backend.rest;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import com.silenteight.sens.webapp.backend.security.dto.PrincipalDto;
import com.silenteight.sens.webapp.backend.security.dto.PrincipalDtoMapper;
import com.silenteight.sens.webapp.common.rest.RestConstants;
import com.silenteight.sens.webapp.kernel.security.SensUserDetails;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping(RestConstants.ROOT)
public class AuthController {

  private final @NonNull PrincipalDtoMapper principalDtoMapper;

  @GetMapping(path = "/check-auth")
  public ResponseEntity<PrincipalDto> checkAuth(@AuthenticationPrincipal SensUserDetails user) {
    if (user == null || !user.hasAnyAuthorities())
      throw new AccessDeniedException("Not authorized");

    return ResponseEntity.ok(principalDtoMapper.map(user));
  }
}
