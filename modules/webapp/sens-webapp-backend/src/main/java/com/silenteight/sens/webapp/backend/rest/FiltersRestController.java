package com.silenteight.sens.webapp.backend.rest;

import lombok.AllArgsConstructor;

import com.silenteight.sens.webapp.backend.RestConstants;
import com.silenteight.sens.webapp.backend.application.filters.save.dto.CreateFilterDto;
import com.silenteight.sens.webapp.backend.presentation.dto.filters.FilterDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import javax.validation.Valid;

import static java.util.Collections.emptyList;

@AllArgsConstructor
@RestController
@RequestMapping(RestConstants.ROOT)
@PreAuthorize("hasAuthority('DECISION_TREE_LIST')")
public class FiltersRestController {

  @GetMapping("/filters")
  public ResponseEntity<List<FilterDto>> getAll() {
    List<FilterDto> response = emptyList();
    return ResponseEntity.ok(response);
  }

  @PostMapping("/filters")
  public ResponseEntity<Void> createFilter(
      @Valid @RequestBody CreateFilterDto requestDto) {
    return ResponseEntity.noContent().build();
  }

  @DeleteMapping("/filters/{id}")
  public ResponseEntity<Void> deleteFilter(@PathVariable long id) {
    return ResponseEntity.noContent().build();
  }
}
