package com.silenteight.serp.governance.branch;

import com.silenteight.proto.serp.v1.recommendation.BranchSolution;
import com.silenteight.serp.governance.bulkchange.BulkBranchChangeApplied;
import com.silenteight.serp.governance.bulkchange.ReasoningBranchIdToApply;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BulkChangeEventListenerTest {

  @Mock
  private BranchService branchService;

  @InjectMocks
  private BulkChangeEventListener bulkChangeEventListener;

  @Test
  void shouldHandleBulkBranchChangeApplied() {
    // given
    UUID correlationId = UUID.randomUUID();

    // and
    BulkBranchChangeApplied event = BulkBranchChangeApplied.builder()
        .correlationId(correlationId)
        .enablementChange(true)
        .solutionChange(BranchSolution.BRANCH_FALSE_POSITIVE)
        .reasoningBranchIds(List.of(
            new ReasoningBranchIdToApply(1L, 2L),
            new ReasoningBranchIdToApply(3L, 4L)
        ))
        .build();

    // when
    bulkChangeEventListener.bulkChange(event);

    // and
    List<ConfigureBranchRequest> configureBranchRequests = List.of(
        ConfigureBranchRequest.builder()
            .correlationId(correlationId)
            .enabled(true)
            .solution(BranchSolution.BRANCH_FALSE_POSITIVE)
            .decisionTreeId(1L)
            .featureVectorId(2L)
            .build(),

        ConfigureBranchRequest.builder()
            .correlationId(correlationId)
            .enabled(true)
            .solution(BranchSolution.BRANCH_FALSE_POSITIVE)
            .decisionTreeId(3L)
            .featureVectorId(4L)
            .build()
    );

    // then
    verify(branchService).bulkUpdateOrCreateBranches(configureBranchRequests);
  }
}
