package com.silenteight.serp.governance.vector.list;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.common.web.rest.Paging;
import com.silenteight.serp.governance.vector.domain.dto.FeatureVectorsDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;
import static java.lang.String.valueOf;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class ListVectorsRestController {

  private static final String LIST_VECTORS_URL = "/v1/vectors";
  private static final String HEADER_TOTAL_COUNT = "X-Total-Count";
  private static final String HEADER_TOTAL_COUNT_ALL = "X-Total-Count-All";
  private static final String PAGE_INDEX = "pageIndex";
  private static final String PAGE_SIZE = "pageSize";

  @NonNull
  private final ListVectorsQuery listVectorsQuery;
  @NonNull
  private final FindFeatureVectorsSolvedByStepUseCase findFeatureVectorsSolvedByStepUseCase;
  @NonNull
  private final FindFeatureVectorsSolvedByDefaultPolicyUseCase
      findFeatureVectorsSolvedByDefaultPolicyUseCase;

  @GetMapping(value = LIST_VECTORS_URL, params = { PAGE_INDEX, PAGE_SIZE })
  @PreAuthorize("isAuthorized('LIST_VECTORS')")
  public ResponseEntity<FeatureVectorsDto> list(
      @RequestParam @Min(0) int pageIndex,
      @RequestParam @Min(1) int pageSize) {

    return ok()
        .header(HEADER_TOTAL_COUNT, valueOf(listVectorsQuery.count()))
        .body(findFeatureVectorsSolvedByDefaultPolicyUseCase.activate(
            new Paging(pageIndex, pageSize)));
  }

  @GetMapping(value = LIST_VECTORS_URL, params = { "stepName", PAGE_INDEX, PAGE_SIZE })
  @PreAuthorize("isAuthorized('LIST_STEP_FEATURE_VECTORS')")
  public ResponseEntity<FeatureVectorsDto> listForStep(
      @RequestParam(required = false) String stepName,
      @RequestParam @Min(0) int pageIndex,
      @RequestParam @Min(1) int pageSize) {

    String count = valueOf(listVectorsQuery.count());

    return ok()
        .header(HEADER_TOTAL_COUNT_ALL, count)
        .header(HEADER_TOTAL_COUNT, count)
        .body(findFeatureVectorsSolvedByStepUseCase.activate(
            stepName, new Paging(pageIndex, pageSize)));
  }
}
