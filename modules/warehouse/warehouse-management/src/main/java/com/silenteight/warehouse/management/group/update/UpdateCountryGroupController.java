package com.silenteight.warehouse.management.group.update;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import com.silenteight.warehouse.management.group.update.dto.UpdateCountryGroupRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import javax.validation.Valid;

import static com.silenteight.warehouse.common.web.rest.RestConstants.ROOT;
import static com.silenteight.warehouse.management.group.common.CountryGroupResource.COUNTRY_GROUPS_URL;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping(ROOT)
@AllArgsConstructor
class UpdateCountryGroupController {

  static final String ID_PARAM = "id";
  static final String COUNTRY_GROUP_URL = COUNTRY_GROUPS_URL + "/{" + ID_PARAM + "}";

  @NonNull
  private final UpdateCountryGroupUseCase useCase;

  @PatchMapping(COUNTRY_GROUP_URL)
  @PreAuthorize("isAuthorized('UPDATE_COUNTRY_GROUP')")
  public ResponseEntity<Void> update(
      @PathVariable UUID id,
      @Valid @RequestBody UpdateCountryGroupRequest updateCountryGroupRequest) {

    useCase.activate(id, updateCountryGroupRequest);
    return status(NO_CONTENT).build();
  }
}
