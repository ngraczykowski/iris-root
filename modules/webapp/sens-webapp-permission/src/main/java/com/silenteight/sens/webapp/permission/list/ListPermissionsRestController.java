package com.silenteight.sens.webapp.permission.list;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.permission.list.dto.PermissionDto;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

import static com.silenteight.sens.webapp.common.rest.RestConstants.ROOT;
import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.ROLE_MANAGEMENT;
import static com.silenteight.sens.webapp.permission.domain.DomainConstants.PERMISSION_ENDPOINT_TAG;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = ROOT, produces = APPLICATION_JSON_VALUE)
@Tag(name = PERMISSION_ENDPOINT_TAG)
class ListPermissionsRestController {

  @NonNull
  private final ListPermissionQuery listPermissionQuery;

  @GetMapping("/permissions")
  @PreAuthorize("isAuthorized('LIST_PERMISSIONS')")
  public ResponseEntity<Collection<PermissionDto>> listAll() {
    log.info(ROLE_MANAGEMENT, "Listing all permissions");
    return ok(listPermissionQuery.listAll());
  }
}
