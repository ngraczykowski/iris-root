package com.silenteight.sens.webapp.backend.rest;

import lombok.AllArgsConstructor;

import com.silenteight.sens.webapp.backend.security.dto.PrincipalDto;
import com.silenteight.sens.webapp.common.rest.RestConstants;
import com.silenteight.sens.webapp.kernel.security.WebappUserDetails;

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

  @GetMapping(path = "/check-auth")
  public ResponseEntity<PrincipalDto> checkAuth(@AuthenticationPrincipal WebappUserDetails user) {
    if (user == null || user.hasNoAuthorities())
      throw new AccessDeniedException("Not authorized");

    return ResponseEntity.ok(new PrincipalDto(user));
  }
}
