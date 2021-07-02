package com.silenteight.warehouse.management.country.get;

import lombok.AllArgsConstructor;

import com.silenteight.warehouse.management.country.CountryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static com.silenteight.warehouse.common.web.rest.RestConstants.ROOT;
import static com.silenteight.warehouse.management.group.common.CountryGroupResource.COUNTRY_GROUPS_URL;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(ROOT)
@AllArgsConstructor
class GetCountriesRestController {

  static final String COUNTRY_GROUP_ID_PARAM = "countryGroupId";
  static final String COUNTRIES_URL =
      COUNTRY_GROUPS_URL + "/{" + COUNTRY_GROUP_ID_PARAM + "}/countries";

  @Autowired
  private CountryService countryService;

  @GetMapping(COUNTRIES_URL)
  @PreAuthorize("isAuthorized('UPDATE_COUNTRIES')")
  public ResponseEntity<List<String>> update(@PathVariable(COUNTRY_GROUP_ID_PARAM) UUID id) {
    return ok(countryService.getCountries(id));
  }
}
