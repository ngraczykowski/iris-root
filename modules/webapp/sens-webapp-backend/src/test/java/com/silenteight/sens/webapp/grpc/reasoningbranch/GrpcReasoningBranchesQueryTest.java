package com.silenteight.sens.webapp.grpc.reasoningbranch;

import com.silenteight.proto.serp.v1.api.BranchGovernanceGrpc.BranchGovernanceBlockingStub;
import com.silenteight.proto.serp.v1.api.ListReasoningBranchesResponse;
import com.silenteight.proto.serp.v1.common.Page;
import com.silenteight.proto.serp.v1.governance.ReasoningBranchId;
import com.silenteight.proto.serp.v1.governance.ReasoningBranchSummary;
import com.silenteight.proto.serp.v1.governance.ReasoningBranchSummary.Builder;
import com.silenteight.sens.webapp.backend.deprecated.reasoningbranch.rest.BranchDto;
import com.silenteight.sens.webapp.backend.reasoningbranch.list.dto.ReasoningBranchDto;
import com.silenteight.sens.webapp.backend.reasoningbranch.list.dto.ReasoningBranchFilterDto;
import com.silenteight.sens.webapp.backend.reasoningbranch.list.dto.ReasoningBranchIdDto;
import com.silenteight.sens.webapp.backend.reasoningbranch.list.dto.ReasoningBranchesPageDto;
import com.silenteight.sens.webapp.backend.reasoningbranch.report.BranchWithFeaturesDto;
import com.silenteight.sens.webapp.backend.reasoningbranch.report.exception.DecisionTreeNotFoundException;
import com.silenteight.sens.webapp.backend.reasoningbranch.validate.dto.BranchIdAndSignatureDto;
import com.silenteight.sens.webapp.backend.support.Paging;
import com.silenteight.sens.webapp.grpc.GrpcCommunicationException;

import com.google.protobuf.ByteString;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static com.silenteight.proto.serp.v1.recommendation.BranchSolution.BRANCH_FALSE_POSITIVE;
import static com.silenteight.protocol.utils.MoreTimestamps.toTimestamp;
import static com.silenteight.sens.webapp.grpc.GrpcFixtures.NOT_FOUND_RUNTIME_EXCEPTION;
import static com.silenteight.sens.webapp.grpc.GrpcFixtures.OTHER_STATUS_RUNTIME_EXCEPTION;
import static com.silenteight.sens.webapp.grpc.reasoningbranch.GrpcReasoningBranchesQueryTestFixtures.*;
import static com.silenteight.sens.webapp.grpc.util.ByteStringTestUtils.randomSignature;
import static com.silenteight.sep.base.common.protocol.ByteStringUtils.toBase64String;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class GrpcReasoningBranchesQueryTest {

  @Mock
  private BranchGovernanceBlockingStub branchStub;

  private GrpcReasoningBranchesQuery underTest;

  @BeforeEach
  void setUp() {
    underTest = new GrpcReasoningBranchesQuery(branchStub);
  }

  @Test
  void returnsEmptyList_whenGrpcThrowsNotFoundStatusException() {
    given(branchStub.listReasoningBranches(any())).willThrow(NOT_FOUND_RUNTIME_EXCEPTION);

    List<BranchDto> actual =
        underTest.findBranchByTreeIdAndBranchIds(DECISION_TREE_ID, REASONING_BRANCH_IDS);

    assertThat(actual).isEmpty();
  }

  @Test
  void throwsGrpcCommunicationException_whenGrpcThrowsOtherThanNotFoundStatusException() {
    given(branchStub.listReasoningBranches(any())).willThrow(OTHER_STATUS_RUNTIME_EXCEPTION);

    ThrowingCallable when = () ->
        underTest.findBranchByTreeIdAndBranchIds(DECISION_TREE_ID, REASONING_BRANCH_IDS);

    assertThatThrownBy(when).isInstanceOf(GrpcCommunicationException.class);
  }

  @Test
  void returnsCorrectBranchDetails_whenRequestingEnabledBranch() {
    given(branchStub.listReasoningBranches(LIST_REASONING_BRANCHES_REQUEST)).willReturn(
        LIST_REASONING_BRANCHES_RESPONSE);

    List<BranchDto> actual =
        underTest.findBranchByTreeIdAndBranchIds(DECISION_TREE_ID, REASONING_BRANCH_IDS);

    assertThat(actual).hasSize(1);
    assertThat(actual)
        .extracting(BranchDto::getReasoningBranchId)
        .containsOnly(EXISTING_REASONING_BRANCH_ID);
  }

  @Test
  void returnsListOfBranches_whenRequestingByTreeId() {
    long decisionTreeId = 2;
    long reasoningBranchId = 3;
    Instant updatedAt = Instant.parse("2019-10-28T15:35:24.00Z");
    String featureValue1 = "featureValue1";
    String featureValue2 = "featureValue2";

    given(branchStub.listReasoningBranches(
        argThat(
            request -> request.getDecisionTreeFilter().getDecisionTreeIds(0) == decisionTreeId)))
        .willReturn(
            reasoningBranchesResponseWith(
                ReasoningBranchSummary
                    .newBuilder()
                    .setReasoningBranchId(reasoningBranchId(decisionTreeId, reasoningBranchId))
                    .setUpdatedAt(toTimestamp(updatedAt))
                    .setSolution(BRANCH_FALSE_POSITIVE)
                    .setEnabled(true)
                    .addFeatureValue(featureValue1)
                    .addFeatureValue(featureValue2)
            ));

    List<BranchWithFeaturesDto> actual = underTest.findByTreeId(decisionTreeId);
    assertThat(actual).hasSize(1);
    assertThat(actual.get(0))
        .satisfies(dto -> {
          assertThat(dto.getReasoningBranchId()).isEqualTo(reasoningBranchId);
          assertThat(dto.getUpdatedAt()).isEqualTo(updatedAt);
          assertThat(dto.getAiSolution()).isEqualTo("FALSE_POSITIVE");
          assertThat(dto.isActive()).isEqualTo(true);
          assertThat(dto.getFeatureValues()).containsExactly(featureValue1, featureValue2);
        });
  }

  @Test
  void throwsException_whenGrpcThrowsNotFoundStatusException_requestingByTreeId() {
    given(branchStub.listReasoningBranches(any())).willThrow(NOT_FOUND_RUNTIME_EXCEPTION);

    assertThatThrownBy(() -> underTest.findByTreeId(DECISION_TREE_ID))
        .isInstanceOf(DecisionTreeNotFoundException.class);
  }

  @Test
  void throwsGrpcException_whenGrpcThrowsOtherThanNotFoundStatusException_requestingByTreeId() {
    given(branchStub.listReasoningBranches(any())).willThrow(OTHER_STATUS_RUNTIME_EXCEPTION);

    assertThatThrownBy(() -> underTest.findByTreeId(DECISION_TREE_ID))
        .isInstanceOf(GrpcCommunicationException.class);
  }

  @Test
  void returnsListOfBranchIds_whenRequestingByTreeIdAndBranchIds() {
    long decisionTreeId = 4L;
    long reasoningBranchId = 5L;
    ByteString featureVectorSignature = randomSignature();

    given(branchStub.listReasoningBranches(
        argThat(
            request -> request.getDecisionTreeFilter().getDecisionTreeIds(0) == decisionTreeId)))
        .willReturn(
            reasoningBranchesResponseWith(
                ReasoningBranchSummary
                    .newBuilder()
                    .setReasoningBranchId(reasoningBranchId(decisionTreeId, reasoningBranchId))
                    .setFeatureVectorSignature(featureVectorSignature),
                ReasoningBranchSummary
                    .newBuilder()
                    .setReasoningBranchId(reasoningBranchId(decisionTreeId, 1L))
                    .setFeatureVectorSignature(randomSignature())
            ));

    String encodedFeatureVectorSignatureSignature = toBase64String(featureVectorSignature);

    List<BranchIdAndSignatureDto> branchIdAndSignatures =
        underTest.findIdsByTreeIdAndBranchIds(
            decisionTreeId, List.of(reasoningBranchId));

    assertThat(branchIdAndSignatures).hasSize(1);
    assertThat(branchIdAndSignatures.get(0).getBranchId()).isEqualTo(reasoningBranchId);
    assertThat(branchIdAndSignatures.get(0).getFeatureVectorSignature()).isEqualTo(
        encodedFeatureVectorSignatureSignature);
  }

  @Test
  void returnsListOfBranchIds_whenRequestingByTreeIdAndSignature() {
    long decisionTreeId = 4L;
    long reasoningBranchId = 5L;
    ByteString featureVectorSignature = randomSignature();

    given(branchStub.listReasoningBranches(
        argThat(
            request -> request.getDecisionTreeFilter().getDecisionTreeIds(0) == decisionTreeId)))
        .willReturn(
            reasoningBranchesResponseWith(
                ReasoningBranchSummary
                    .newBuilder()
                    .setReasoningBranchId(reasoningBranchId(decisionTreeId, reasoningBranchId))
                    .setFeatureVectorSignature(featureVectorSignature),
                ReasoningBranchSummary
                    .newBuilder()
                    .setReasoningBranchId(reasoningBranchId(decisionTreeId, 1L))
                    .setFeatureVectorSignature(randomSignature())
            ));

    String encodedFeatureVectorSignatureSignature = toBase64String(featureVectorSignature);

    List<BranchIdAndSignatureDto> branchIdAndSignatures =
        underTest.findIdsByTreeIdAndFeatureVectorSignatures(
            decisionTreeId, List.of(encodedFeatureVectorSignatureSignature));

    assertThat(branchIdAndSignatures).hasSize(1);
    assertThat(branchIdAndSignatures.get(0).getBranchId()).isEqualTo(reasoningBranchId);
    assertThat(branchIdAndSignatures.get(0).getFeatureVectorSignature()).isEqualTo(
        encodedFeatureVectorSignatureSignature);
  }

  private static ListReasoningBranchesResponse reasoningBranchesResponseWith(
      Builder... reasoningBranches) {
    return ListReasoningBranchesResponse
        .newBuilder()
        .addAllReasoningBranch(stream(reasoningBranches).map(Builder::build).collect(toList()))
        .setPage(Page.newBuilder().setTotalElements(reasoningBranches.length).build())
        .build();
  }

  private static ReasoningBranchId reasoningBranchId(long decisionTreeId, long featureVector) {
    return ReasoningBranchId
        .newBuilder()
        .setDecisionTreeId(decisionTreeId)
        .setFeatureVectorId(featureVector)
        .build();
  }

  @Test
  void returnsPageOfReasoningBranches_whenRequestingByFilterAndPaging() {
    int pageSize = 20;
    long decisionTreeId1 = 4L;
    long featureVectorId1 = 5L;
    long decisionTreeId2 = 8L;
    long featureVectorId2 = 9L;
    ByteString featureVectorSignature = randomSignature();

    given(branchStub.listReasoningBranches(
        argThat(
            request -> request.getPagination().getPageSize() == pageSize)))
        .willReturn(
            reasoningBranchesResponseWith(
                ReasoningBranchSummary
                    .newBuilder()
                    .setReasoningBranchId(reasoningBranchId(decisionTreeId1, featureVectorId1))
                    .setFeatureVectorSignature(featureVectorSignature),
                ReasoningBranchSummary
                    .newBuilder()
                    .setReasoningBranchId(reasoningBranchId(decisionTreeId2, featureVectorId2))
                    .setFeatureVectorSignature(randomSignature())
            ));

    ReasoningBranchesPageDto page =
        underTest.list(new ReasoningBranchFilterDto(null, null), new Paging(0, pageSize));

    assertThat(page.getTotal()).isEqualTo(2);
    assertThat(page.getBranches())
        .extracting(ReasoningBranchDto::getReasoningBranchId)
        .containsExactly(
            new ReasoningBranchIdDto(decisionTreeId1, featureVectorId1),
            new ReasoningBranchIdDto(decisionTreeId2, featureVectorId2));
  }
}
