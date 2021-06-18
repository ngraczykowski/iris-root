package com.silenteight.warehouse.management.group.get;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import com.silenteight.warehouse.management.group.domain.dto.CountryGroupDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.silenteight.warehouse.common.web.rest.RestConstants.ROOT;
import static com.silenteight.warehouse.management.group.common.CountryGroupResource.COUNTRY_GROUP_URL;
import static com.silenteight.warehouse.management.group.common.CountryGroupResource.ID_PARAM;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(ROOT)
@AllArgsConstructor
class GetCountryGroupRestController {

  @NonNull
  private final GetCountryGroupQuery groupQuery;

  @GetMapping(COUNTRY_GROUP_URL)
  @PreAuthorize("isAuthorized('GET_COUNTRY_GROUP')")
  public ResponseEntity<CountryGroupDto> get(@PathVariable(ID_PARAM) UUID id) {
    return ok().body(groupQuery.get(id));
  }
}
