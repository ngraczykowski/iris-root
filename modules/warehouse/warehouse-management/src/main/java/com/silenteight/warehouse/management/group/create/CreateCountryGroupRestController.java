package com.silenteight.warehouse.management.group.create;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import com.silenteight.warehouse.management.group.domain.dto.CountryGroupDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.silenteight.warehouse.common.web.rest.RestConstants.ROOT;
import static com.silenteight.warehouse.management.group.common.CountryGroupResource.COUNTRY_GROUPS_URL;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping(ROOT)
@AllArgsConstructor
class CreateCountryGroupRestController {

  @NonNull
  private final CreateCountryGroupUseCase useCase;

  @PostMapping(COUNTRY_GROUPS_URL)
  @PreAuthorize("isAuthorized('CREATE_COUNTRY_GROUP')")
  public ResponseEntity<Void> create(@RequestBody @Valid CountryGroupDto countryGroupDto) {

    useCase.activate(countryGroupDto);
    return status(CREATED).build();
  }
}
