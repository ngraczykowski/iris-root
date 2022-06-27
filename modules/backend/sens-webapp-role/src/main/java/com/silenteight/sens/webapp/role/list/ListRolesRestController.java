package com.silenteight.sens.webapp.role.list;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.role.list.dto.RoleDto;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

import static com.silenteight.sens.webapp.role.domain.DomainConstants.ROLE_ENDPOINT_TAG;
import static com.silenteight.sep.auth.authentication.RestConstants.ROOT;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = ROOT, produces = APPLICATION_JSON_VALUE)
@Tag(name = ROLE_ENDPOINT_TAG)
class ListRolesRestController {

  @NonNull
  private final ListRolesQuery listRolesQuery;

  @GetMapping("/v2/roles")
  @PreAuthorize("isAuthorized('LIST_ROLES')")
  public ResponseEntity<Collection<RoleDto>> list() {
    log.info("Listing roles.");
    return ok(listRolesQuery.listAll());
  }
}
