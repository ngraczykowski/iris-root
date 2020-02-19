package com.silenteight.sens.webapp.grpc.reasoningbranch;

import com.silenteight.sens.webapp.backend.reasoningbranch.rest.BranchDetailsDto;

import org.assertj.core.api.AbstractAssert;

import static org.assertj.core.api.Assertions.*;

class BranchDetailsDtoAssert extends AbstractAssert<BranchDetailsDtoAssert, BranchDetailsDto> {

  private BranchDetailsDtoAssert(BranchDetailsDto branchDetailsDto) {
    super(branchDetailsDto, BranchDetailsDtoAssert.class);
  }

  static BranchDetailsDtoAssert assertThatDetails(BranchDetailsDto detailsDto) {
    return new BranchDetailsDtoAssert(detailsDto);
  }


  BranchDetailsDtoAssert hasBranchId(long branchId) {
    assertThat(actual.getReasoningBranchId()).isEqualTo(branchId);

    return this;
  }

  BranchDetailsDtoAssert hasAiSolution(String aiSolution) {
    assertThat(actual.getAiSolution()).isEqualTo(aiSolution);

    return this;
  }

  BranchDetailsDtoAssert hasEnabledSetTo(boolean enabled) {
    assertThat(actual.isActive()).isEqualTo(enabled);

    return this;
  }
}
