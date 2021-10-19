package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.bulk.rest.BatchAcceptedResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/async/batch/s8")
@RequiredArgsConstructor
class BulkReProcessController {

  private final CreateSolvingBulkUseCase createSolvingBulkUseCase;

  @PostMapping("/reRecommend")
  public ResponseEntity<BatchAcceptedResponse> reRecommend(@RequestBody List<String> alerts) {
    var batchId = createSolvingBulkUseCase.createBulkWithAlerts(alerts);
    return ResponseEntity.ok(new BatchAcceptedResponse().batchId(batchId));
  }
}
