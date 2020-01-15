package com.silenteight.sens.webapp.backend.rest;

import lombok.AllArgsConstructor;

import com.silenteight.sens.webapp.backend.application.decisiontree.assignments.dto.AssignmentsUpdateDto;
import com.silenteight.sens.webapp.backend.presentation.dto.assignment.AssignmentsDto;
import com.silenteight.sens.webapp.common.rest.RestConstants;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static java.util.Collections.emptyList;

@AllArgsConstructor
@RestController
@RequestMapping(RestConstants.ROOT)
@PreAuthorize("hasAuthority('BATCH_TYPE_MANAGE')")
public class DecisionTreeAssignmentsRestController {

  @GetMapping("/decision-tree/{decisionTreeId}/assignments")
  public ResponseEntity<AssignmentsDto> getAssignments(@PathVariable long decisionTreeId) {
    AssignmentsDto response = new AssignmentsDto(emptyList(), emptyList(), emptyList());

    return ResponseEntity.ok(response);
  }

  @PostMapping("/decision-tree/{decisionTreeId}/assignments")
  public ResponseEntity<AssignmentsDto> updateAssignments(
      @PathVariable long decisionTreeId,
      @RequestBody AssignmentsUpdateDto assignmentsUpdate) {

    return ResponseEntity.ok(new AssignmentsDto(emptyList(), emptyList(), emptyList()));
  }
}
