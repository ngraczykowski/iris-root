package com.silenteight.sens.webapp.role.list;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.role.list.dto.RoleDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

import static com.silenteight.sens.webapp.common.rest.RestConstants.ROOT;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(ROOT)
class ListRolesRestController {

  @NonNull
  private final ListRolesQuery listRolesQuery;

  @GetMapping("/v2/roles")
  @PreAuthorize("isAuthorized('LIST_ROLES')")
  public ResponseEntity<Collection<RoleDto>> list() {
    return ok(listRolesQuery.listAll());
  }
}
