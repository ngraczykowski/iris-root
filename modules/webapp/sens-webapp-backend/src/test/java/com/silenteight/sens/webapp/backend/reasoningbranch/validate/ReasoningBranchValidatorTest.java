package com.silenteight.sens.webapp.backend.reasoningbranch.validate;

import com.silenteight.sens.webapp.backend.reasoningbranch.BranchIdsNotFoundException;
import com.silenteight.sens.webapp.backend.reasoningbranch.FeatureVectorSignaturesNotFoundException;
import com.silenteight.sens.webapp.backend.reasoningbranch.rest.BranchDto;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReasoningBranchValidatorTest {

  @Mock
  private ReasoningBranchesValidateQuery reasoningBranchesQuery;

  @InjectMocks
  private ReasoningBranchValidator validator;

  @Test
  void validatesOkAndReturnsIdsIfAllBranchIdsFound() {
    long treeId = 12L;
    List<Long> branchIds = List.of(3L, 4L);
    when(reasoningBranchesQuery.findIdsByTreeIdAndBranchIds(treeId, branchIds))
        .thenReturn(List.of(
            new BranchIdAndSignatureDto(3L, "signA"),
            new BranchIdAndSignatureDto(4L, "signB")));

    Map<Long, String> result = validator.validate(treeId, branchIds, null);
    assertThat(result.get(3L)).isEqualTo("signA");
    assertThat(result.get(4L)).isEqualTo("signB");
  }

  @Test
  void throwsExceptionIfValidatedBranchIdsNotOnTheReturnedList() {
    long treeId = 14L;
    List<Long> branchIds = List.of(3L, 4L, 5L, 6L);
    when(reasoningBranchesQuery.findIdsByTreeIdAndBranchIds(treeId, branchIds))
        .thenReturn(List.of(
            new BranchIdAndSignatureDto(5L, "a"),
            new BranchIdAndSignatureDto(3L, "b")));

    ThrowingCallable validatorCall = () -> validator.validate(treeId, branchIds, null);

    assertThatThrownBy(validatorCall)
        .isInstanceOf(BranchIdsNotFoundException.class)
        .hasFieldOrPropertyWithValue("nonExistingBranchIds", List.of(4L, 6L));
  }

  @Test
  void validatesOkAndReturnsIdsIfAllFeatureVectorSignaturesFound() {
    long treeId = 12L;
    List<String> featureVectorSignatures = List.of("abcd", "efg");
    when(reasoningBranchesQuery.findIdsByTreeIdAndFeatureVectorSignatures(treeId,
        featureVectorSignatures))
        .thenReturn(List.of(
            new BranchIdAndSignatureDto(2L, "abcd"),
            new BranchIdAndSignatureDto(3L, "efg")));

    Map<Long, String> result = validator.validate(treeId, null, featureVectorSignatures);
    assertThat(result.get(2L)).isEqualTo("abcd");
    assertThat(result.get(3L)).isEqualTo("efg");

  }

  @Test
  void throwsExceptionIfValidatedBranchesSignaturesNotOnTheReturnedList() {
    long treeId = 14L;
    List<String> featureVectorSignatures = List.of("abcd", "ijk", "efg");
    when(reasoningBranchesQuery.findIdsByTreeIdAndFeatureVectorSignatures(treeId,
        featureVectorSignatures))
        .thenReturn(List.of(
            new BranchIdAndSignatureDto(1L, "ijk")));

    FeatureVectorSignaturesNotFoundException thrown = assertThrows(
        FeatureVectorSignaturesNotFoundException.class,
        () -> validator.validate(treeId, null, featureVectorSignatures)
    );

    assertThat(thrown.getNonExistingFeatureVectorSignatures()).containsExactlyInAnyOrder(
        "abcd", "efg");
  }

  private BranchDto branchDtoWithId(long reasoningBranchId) {
    return BranchDto.builder().reasoningBranchId(reasoningBranchId).aiSolution("FP").build();
  }
}
