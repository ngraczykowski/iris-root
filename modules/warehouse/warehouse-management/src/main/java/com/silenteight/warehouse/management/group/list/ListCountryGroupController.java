package com.silenteight.warehouse.management.group.list;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import com.silenteight.warehouse.management.group.domain.dto.CountryGroupDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

import static com.silenteight.warehouse.common.web.rest.RestConstants.ROOT;
import static com.silenteight.warehouse.management.group.common.CountryGroupResource.COUNTRY_GROUPS_URL;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(ROOT)
@AllArgsConstructor
class ListCountryGroupController {

  @NonNull
  private final ListCountryGroupQuery groupQuery;

  @GetMapping(COUNTRY_GROUPS_URL)
  @PreAuthorize("isAuthorized('LIST_COUNTRY_GROUP')")
  public ResponseEntity<Collection<CountryGroupDto>> listAll() {
    return ok().body(groupQuery.listAll());
  }
}
