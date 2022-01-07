package com.silenteight.sens.webapp.user.roles.list;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.user.roles.list.dto.PermissionDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.UUID;

import static com.silenteight.sens.webapp.common.rest.RestConstants.ROOT;
import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.ROLE_MANAGEMENT;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(ROOT)
class ListPermissionsRestController {

  @NonNull
  private final ListPermissionQuery listPermissionQuery;

  @GetMapping("/permissions")
  @PreAuthorize("isAuthorized('LIST_PERMISSIONS')")
  public ResponseEntity<Collection<PermissionDto>> listAll() {
    log.info(ROLE_MANAGEMENT, "Listing all permissions");
    return ok(listPermissionQuery.listAll());
  }

  @GetMapping("/v2/roles/{id}/permissions")
  @PreAuthorize("isAuthorized('LIST_PERMISSIONS')")
  public ResponseEntity<Collection<PermissionDto>> listPermissionsAssignedToRole(
      @PathVariable UUID id) {

    log.info(ROLE_MANAGEMENT, "Listing permissions assigned to role roleId={}", id);
    return ok(listPermissionQuery.listPermissionsByRoleId(id));
  }
}
