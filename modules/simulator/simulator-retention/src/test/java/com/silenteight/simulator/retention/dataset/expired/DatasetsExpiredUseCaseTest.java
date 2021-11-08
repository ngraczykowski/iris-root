package com.silenteight.simulator.retention.dataset.expired;

import com.silenteight.dataretention.api.v1.AnalysisExpired;
import com.silenteight.simulator.dataset.domain.DatasetMetadataService;
import com.silenteight.simulator.dataset.domain.DatasetQuery;
import com.silenteight.simulator.management.list.ListSimulationsQuery;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.simulator.retention.dataset.expired.DatasetExpiredFixtures.ANALYSIS_NAMES;
import static com.silenteight.simulator.retention.dataset.expired.DatasetExpiredFixtures.DATASETS_EXPIRED_MESSAGE;
import static com.silenteight.simulator.retention.dataset.expired.DatasetExpiredFixtures.EXTERNAL_RESOURCE_NAMES;
import static com.silenteight.simulator.retention.dataset.expired.DatasetExpiredFixtures.RESOURCE_NAMES;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DatasetsExpiredUseCaseTest {

  @InjectMocks
  private DatasetsExpiredUseCase underTest;

  @Mock
  private DatasetMetadataService datasetMetadataService;
  @Mock
  private DatasetQuery datasetQuery;
  @Mock
  private ListSimulationsQuery listSimulationsQuery;

  @Test
  void shouldExpireDatasets() {
    //given
    when(datasetQuery.getDatasetNames(EXTERNAL_RESOURCE_NAMES)).thenReturn(RESOURCE_NAMES);
    when(listSimulationsQuery.getAnalysisNames(RESOURCE_NAMES)).thenReturn(ANALYSIS_NAMES);

    // when
    AnalysisExpired response = underTest.handle(DATASETS_EXPIRED_MESSAGE);

    // then
    verify(datasetMetadataService).expire(EXTERNAL_RESOURCE_NAMES);
    assertThat(response.getAnalysisList()).isEqualTo(ANALYSIS_NAMES);
  }
}
