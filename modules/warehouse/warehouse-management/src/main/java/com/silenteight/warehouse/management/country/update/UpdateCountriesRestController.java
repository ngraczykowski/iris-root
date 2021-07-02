package com.silenteight.warehouse.management.country.update;

import lombok.AllArgsConstructor;

import com.silenteight.warehouse.management.country.CountryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;
import javax.validation.Valid;

import static com.silenteight.warehouse.common.web.rest.RestConstants.ROOT;
import static com.silenteight.warehouse.management.group.common.CountryGroupResource.COUNTRY_GROUPS_URL;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping(ROOT)
@AllArgsConstructor
public class UpdateCountriesRestController {

  static final String COUNTRY_GROUP_ID_PARAM = "countryGroupId";
  static final String COUNTRIES_URL =
      COUNTRY_GROUPS_URL + "/{" + COUNTRY_GROUP_ID_PARAM + "}/countries";

  @Autowired
  private CountryService countryService;

  @PutMapping(COUNTRIES_URL)
  @PreAuthorize("isAuthorized('UPDATE_COUNTRIES')")
  public ResponseEntity<Void> update(
      @PathVariable(COUNTRY_GROUP_ID_PARAM) UUID id,
      @Valid @RequestBody Collection<String> countries) {

    countryService.setCountries(id, countries);
    return status(NO_CONTENT).build();
  }
}
