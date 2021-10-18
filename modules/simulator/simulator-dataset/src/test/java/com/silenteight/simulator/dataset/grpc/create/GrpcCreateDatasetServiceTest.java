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
    when(datasetStub.createDataset(makeGrpcRequest())).thenReturn(DATASET);

    // when
    Dataset dataset = underTest.createDataset(makeDomainRequest());

    // then
    assertThat(dataset.getName()).isEqualTo(EXTERNAL_RESOURCE_NAME);
    assertThat(dataset.getAlertCount()).isEqualTo(ALERTS_COUNT);
  }

  private static CreateDatasetRequest makeGrpcRequest() {
    return CreateDatasetRequest.newBuilder()
        .setFilteredAlerts(FILTERED_ALERTS)
        .build();
  }

  private static com.silenteight.simulator.dataset.create.CreateDatasetRequest makeDomainRequest() {
    return com.silenteight.simulator.dataset.create.CreateDatasetRequest.builder()
        .id(ID)
        .datasetName(DATASET_NAME)
        .description(DESCRIPTION)
        .rangeFrom(FROM)
        .rangeTo(TO)
        .labels(LABELS)
        .createdBy(CREATED_BY)
        .build();
  }
}
