package com.silenteight.sens.webapp.grpc.circuitbreaker;

import com.silenteight.proto.serp.v1.api.DiscrepancyCircuitBreakerGrpc.DiscrepancyCircuitBreakerBlockingStub;
import com.silenteight.proto.serp.v1.api.DiscrepantBranch;
import com.silenteight.proto.serp.v1.api.DiscrepantBranchId;
import com.silenteight.proto.serp.v1.api.ListDiscrepantBranchesResponse;
import com.silenteight.sens.webapp.backend.circuitbreaker.DiscrepantBranchDto;
import com.silenteight.sens.webapp.grpc.GrpcCommunicationException;

import com.google.protobuf.Empty;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static com.silenteight.protocol.utils.MoreTimestamps.toTimestamp;
import static com.silenteight.sens.webapp.grpc.GrpcFixtures.OTHER_STATUS_RUNTIME_EXCEPTION;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GrpcDiscrepancyCircuitBreakerQueryTest {

  @Mock
  private DiscrepancyCircuitBreakerBlockingStub discrepancyBlockingStub;

  @InjectMocks
  private GrpcDiscrepancyCircuitBreakerQuery query;

  @Test
  void returnsListOfDiscrepantBranches() {
    long decisionTreeId1 = 1L;
    long featureVectorId1 = 12L;
    Instant detectedAt1 = Instant.now().minusMillis(1000);
    long decisionTreeId2 = 2L;
    long featureVectorId2 = 13L;
    Instant detectedAt2 = Instant.now();

    when(discrepancyBlockingStub.listDiscrepantBranches(any(Empty.class)))
        .thenReturn(
            discrepantBranchesResponseWith(
                discrepantBranchWith(
                    branchIdOf(decisionTreeId1, featureVectorId1), detectedAt1),
                discrepantBranchWith(
                    branchIdOf(decisionTreeId2, featureVectorId2), detectedAt2)));

    List<DiscrepantBranchDto> discrepantBranches = query.listDiscrepantBranches();

    assertThat(discrepantBranches).hasSize(2);

    DiscrepantBranchDto discrepantBranch1 = discrepantBranches.get(0);
    assertThat(discrepantBranch1.getBranchId().getDecisionTreeId())
        .isEqualTo(decisionTreeId1);
    assertThat(discrepantBranch1.getDetectedAt()).isEqualTo(detectedAt1);

    DiscrepantBranchDto discrepantBranch2 = discrepantBranches.get(1);
    assertThat(discrepantBranch2.getBranchId().getDecisionTreeId())
        .isEqualTo(decisionTreeId2);
    assertThat(discrepantBranch2.getDetectedAt()).isEqualTo(detectedAt2);
  }

  @Test
  void throwsGrpcException_whenGrpcThrowsException() {
    when(discrepancyBlockingStub.listDiscrepantBranches(any(Empty.class)))
        .thenThrow(OTHER_STATUS_RUNTIME_EXCEPTION);

    ThrowingCallable featureNamesCall = () -> query.listDiscrepantBranches();

    assertThatThrownBy(featureNamesCall).isInstanceOf(GrpcCommunicationException.class);
  }

  private ListDiscrepantBranchesResponse discrepantBranchesResponseWith(
      DiscrepantBranch discrepantBranch1, DiscrepantBranch discrepantBranch2) {
    return ListDiscrepantBranchesResponse.newBuilder()
        .addDiscrepantBranches(discrepantBranch1)
        .addDiscrepantBranches(discrepantBranch2)
        .build();
  }

  private DiscrepantBranch discrepantBranchWith(
      DiscrepantBranchId branchId1, Instant detectedAt1) {
    return DiscrepantBranch
        .newBuilder()
        .setDiscrepantBranchId(branchId1)
        .setDetectedAt(toTimestamp(detectedAt1))
        .build();
  }

  private DiscrepantBranchId branchIdOf(long decisionTreeId, long featureVectorId) {
    return DiscrepantBranchId
        .newBuilder()
        .setDecisionTreeId(decisionTreeId)
        .setFeatureVectorId(featureVectorId)
        .build();
  }
}
