package com.silenteight.warehouse.management.group.create;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

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

@Slf4j
@RestController
@RequestMapping(ROOT)
@AllArgsConstructor
public class CreateCountryGroupRestController {

  @NonNull
  private final CreateCountryGroupUseCase useCase;

  @PostMapping(COUNTRY_GROUPS_URL)
  @PreAuthorize("isAuthorized('CREATE_COUNTRY_GROUP')")
  public ResponseEntity<Void> create(@RequestBody @Valid CountryGroupDto countryGroupDto) {
    log.info(
        "Create country group request received, countryGroupId={}", countryGroupDto.getId());

    useCase.activate(countryGroupDto);

    log.debug(
        "Create country group request processed for countryGroupId={}", countryGroupDto.getId());
    return status(CREATED).build();
  }
}
