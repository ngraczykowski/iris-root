package com.silenteight.sens.webapp.backend.rest;

import lombok.AllArgsConstructor;

import com.silenteight.sens.webapp.backend.RestConstants;
import com.silenteight.sens.webapp.backend.application.decisiontree.assignments.dto.AssignmentsUpdateDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping(RestConstants.ROOT)
@PreAuthorize("hasAuthority('BATCH_TYPE_MANAGE')")
public class BatchTypeRestController {

  @PostMapping("/batch-types/update")
  public ResponseEntity<Void> updateBatchTypesInDecisionTree(
      @RequestParam long decisionTreeId, @RequestBody AssignmentsUpdateDto requestDto) {

    return ResponseEntity.ok().build();
  }
}
