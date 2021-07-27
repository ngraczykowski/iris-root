package com.silenteight.simulator.dataset.grpc.create;

import com.silenteight.adjudication.api.v1.CreateDatasetRequest;
import com.silenteight.adjudication.api.v1.Dataset;
import com.silenteight.adjudication.api.v1.DatasetServiceGrpc.DatasetServiceBlockingStub;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.silenteight.simulator.dataset.fixture.DatasetFixtures.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GrpcCreateDatasetServiceTest {

  @InjectMocks
  private GrpcCreateDatasetService underTest;

  @Mock
  private DatasetServiceBlockingStub datasetStub;

  @Test
  void shouldCreateDataset() {
    // given
    when(datasetStub.createDataset(makeCreateDatasetRequest())).thenReturn(DATASET);

    // when
    Dataset dataset = underTest.createDataset(CREATE_DATASET_REQUEST);

    // then
    assertThat(dataset.getName()).isEqualTo(EXTERNAL_RESOURCE_NAME);
    assertThat(dataset.getAlertCount()).isEqualTo(ALERTS_COUNT);
  }

  private static CreateDatasetRequest makeCreateDatasetRequest() {
    return CreateDatasetRequest.newBuilder()
        .setFilteredAlerts(FILTERED_ALERTS)
        .build();
  }
}
