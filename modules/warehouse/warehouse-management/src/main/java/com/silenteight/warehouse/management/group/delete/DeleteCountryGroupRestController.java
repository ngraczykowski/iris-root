package com.silenteight.warehouse.management.group.delete;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.silenteight.warehouse.common.web.rest.RestConstants.ROOT;
import static com.silenteight.warehouse.management.group.common.CountryGroupResource.COUNTRY_GROUP_URL;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.ResponseEntity.status;

@Slf4j
@RestController
@RequestMapping(ROOT)
@AllArgsConstructor
public class DeleteCountryGroupRestController {

  @NonNull
  private final DeleteCountryGroupUseCase useCase;

  @DeleteMapping(COUNTRY_GROUP_URL)
  @PreAuthorize("isAuthorized('DELETE_COUNTRY_GROUP')")
  public ResponseEntity<Void> delete(@PathVariable("id") UUID id) {
    log.info("Delete country group request received, countryGroupId={}", id);
    useCase.activate(id);
    log.debug("Delete country group request processed for countryGroupId={}", id);
    return status(NO_CONTENT).build();
  }
}
