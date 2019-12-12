package com.silenteight.sens.webapp.backend.rest;

import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.backend.RestConstants;
import com.silenteight.sens.webapp.backend.presentation.dto.restriction.EditRestrictionRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping(RestConstants.ROOT)
@PreAuthorize("hasAuthority('USER_MANAGE')")
public class RestrictionRestController {

  private static final String RESTRICTIONS = "/restrictions";
  private static final String RESTRICTION = "/restriction";

  @GetMapping(RESTRICTIONS)
  public ResponseEntity<RestrictionListViewDto> getRestrictions() {
    return ResponseEntity.ok(new RestrictionListViewDto());
  }

  @GetMapping(RESTRICTION + "/{restrictionId}")
  public ResponseEntity<RestrictionDetailsViewDto> getRestrictionDetail(
      @PathVariable long restrictionId) {
    return ResponseEntity.ok(new RestrictionDetailsViewDto());
  }

  @PutMapping(RESTRICTION + "/")
  public ResponseEntity<Void> createRestriction(
      @Valid @RequestBody EditRestrictionRequest request) {

    long restrictionId = 0L;

    return ResponseEntity
        .noContent()
        .location(URI.create(RestConstants.ROOT + RESTRICTION + "/" + restrictionId))
        .build();
  }

  @PostMapping(RESTRICTION + "/{restrictionId}")
  public ResponseEntity<Void> updateRestriction(
      @PathVariable long restrictionId,
      @Valid @RequestBody EditRestrictionRequest request) {
    return ResponseEntity
        .noContent()
        .location(URI.create(RestConstants.ROOT + RESTRICTION + "/" + restrictionId))
        .build();
  }

  @DeleteMapping(RESTRICTION + "/{restrictionId}")
  public ResponseEntity<Void> deleteRestriction(@PathVariable long restrictionId) {
    return ResponseEntity.noContent().build();
  }

  private static class RestrictionDetailsViewDto {

  }

  private static class RestrictionListViewDto {

  }
}
