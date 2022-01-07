package com.silenteight.sens.webapp.user.roles.details;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.user.roles.list.dto.RoleDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.silenteight.sens.webapp.common.rest.RestConstants.ROOT;
import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.ROLE_MANAGEMENT;
import static org.springframework.http.ResponseEntity.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(ROOT)
class RoleDetailsRestController {

  @NonNull
  private final RoleDetailsQuery roleDetailsQuery;

  @GetMapping("/v2/roles/{id}")
  @PreAuthorize("isAuthorized('LIST_ROLES')")
  public ResponseEntity<RoleDto> details(@PathVariable UUID id) {
    log.info(ROLE_MANAGEMENT, "Getting details for role roleId={}", id);
    return ok(roleDetailsQuery.details(id));
  }
}
