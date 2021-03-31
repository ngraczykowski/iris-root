package com.silenteight.simulator.dataset.create;

import com.silenteight.adjudication.api.v1.Dataset;
import com.silenteight.simulator.dataset.domain.DatasetMetadataService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.simulator.dataset.create.CreateDatasetFixtures.CREATE_DATASET_REQUEST;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateDatasetUseCaseTest {

  private static final String RESOURCE_NAME = "datasets/1234";
  private static final String USERNAME = "asmith";

  @InjectMocks
  private CreateDatasetUseCase underTest;

  @Mock
  private CreateDatasetService createDatasetService;

  @Mock
  private DatasetMetadataService datasetMetadataService;

  @Test
  void createDataset() {
    // given
    long alertsCount = 5L;
    Dataset dataset = makeDataset(RESOURCE_NAME, alertsCount);
    when(createDatasetService.createDataset(CREATE_DATASET_REQUEST)).thenReturn(dataset);

    // when
    underTest.activate(CREATE_DATASET_REQUEST, USERNAME);

    // then
    verify(datasetMetadataService).createMetadata(CREATE_DATASET_REQUEST, dataset, USERNAME);
  }

  private static Dataset makeDataset(String datasetName, long alertCount) {
    return Dataset.newBuilder()
        .setName(datasetName)
        .setAlertCount(alertCount)
        .build();
  }
}
