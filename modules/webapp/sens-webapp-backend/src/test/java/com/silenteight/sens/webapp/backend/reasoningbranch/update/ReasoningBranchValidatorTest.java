package com.silenteight.sens.webapp.backend.reasoningbranch.update;

import com.silenteight.sens.webapp.backend.reasoningbranch.BranchesNotFoundException;
import com.silenteight.sens.webapp.backend.reasoningbranch.rest.BranchDto;
import com.silenteight.sens.webapp.backend.reasoningbranch.rest.ReasoningBranchesQuery;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReasoningBranchValidatorTest {

  @Mock
  private ReasoningBranchesQuery reasoningBranchesQuery;

  @InjectMocks
  private ReasoningBranchValidator validator;

  @Test
  void validatesOkIfAllBranchesFound() {
    long treeId = 12L;
    List<Long> branchIds = List.of(3L, 4L);
    when(reasoningBranchesQuery.findByTreeIdAndBranchIds(treeId, branchIds))
        .thenReturn(List.of(
            branchDtoWithId(3L), branchDtoWithId(4L)));

    validator.validate(treeId, branchIds);
  }

  @Test
  void throwsExceptionIfValidatedBranchesNotOnTheReturnedList() {
    long treeId = 14L;
    List<Long> branchIds = List.of(3L, 4L, 5L, 6L);
    when(reasoningBranchesQuery.findByTreeIdAndBranchIds(treeId, branchIds))
        .thenReturn(List.of(branchDtoWithId(5L), branchDtoWithId(3L)));

    ThrowingCallable validatorCall = () -> validator.validate(treeId, branchIds);

    assertThatThrownBy(validatorCall)
        .isInstanceOf(BranchesNotFoundException.class)
        .hasFieldOrPropertyWithValue("nonExistingBranchIds", List.of(4L, 6L));
  }

  private BranchDto branchDtoWithId(long reasoningBranchId) {
    return BranchDto.builder().reasoningBranchId(reasoningBranchId).aiSolution("FP").build();
  }
}
