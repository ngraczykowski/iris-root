package com.silenteight.serp.governance.policy.featurevector;

import com.silenteight.serp.governance.analytics.featurevector.FeatureVectorService;
import com.silenteight.serp.governance.policy.featurevector.dto.FeatureVectorsColumnsDto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindFeatureVectorsColumnsUseCaseTest {

  private static final List FEATURE_NAMES =
      List.of("dateAgent", "documentAgent", "nameAgent", "nationalityAgent");

  @Mock
  private FeatureVectorService featureVectorService;

  @InjectMocks
  private FindFeatureVectorsColumnsUseCase underTest;

  @Test
  void findFeatureVectorsColumns() {
    // given
    when(featureVectorService.getUniqueFeatureNames()).thenReturn(FEATURE_NAMES);

    // when
    FeatureVectorsColumnsDto response = underTest.activate();

    // then
    assertThat(response.getColumns()).isNotEmpty();
    assertThat(response.getColumns()).isEqualTo(FEATURE_NAMES);
  }
}
