package com.silenteight.simulator.dataset.grpc.create;

import com.silenteight.adjudication.api.v1.CreateDatasetRequest;
import com.silenteight.adjudication.api.v1.Dataset;
import com.silenteight.adjudication.api.v1.DatasetServiceGrpc.DatasetServiceBlockingStub;
import com.silenteight.adjudication.api.v1.FilteredAlerts;
import com.silenteight.adjudication.api.v1.FilteredAlerts.MatchQuantity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

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
    Dataset dataset = underTest.createDataset(makeDomainRequest(true));

    // then
    assertThat(dataset.getName()).isEqualTo(EXTERNAL_RESOURCE_NAME);
    assertThat(dataset.getAlertCount()).isEqualTo(ALERTS_COUNT);
  }

  @ParameterizedTest
  @MethodSource("provideDomainRequests")
  void shouldMakeGrpcRequest(
      com.silenteight.simulator.dataset.create.CreateDatasetRequest createDatasetRequest,
      MatchQuantity expectedMatchQuantity) {

    //when
    underTest.createDataset(createDatasetRequest);

    //then
    var logCaptor = ArgumentCaptor.forClass(CreateDatasetRequest.class);

    verify(datasetStub, times(1)).createDataset(logCaptor.capture());
    CreateDatasetRequest value = logCaptor.getValue();
    FilteredAlerts filteredAlerts = value.getFilteredAlerts();
    MatchQuantity matchQuantity = filteredAlerts.getMatchQuantity();
    assertThat(matchQuantity).isEqualTo(expectedMatchQuantity);
  }

  private static CreateDatasetRequest makeGrpcRequest() {
    return CreateDatasetRequest.newBuilder()
        .setFilteredAlerts(FILTERED_ALERTS)
        .build();
  }

  private static com.silenteight.simulator.dataset.create.CreateDatasetRequest makeDomainRequest(
      Boolean useMultiHitAlerts) {
    return com.silenteight.simulator.dataset.create.CreateDatasetRequest.builder()
        .id(ID_1)
        .datasetName(DATASET_NAME)
        .description(DESCRIPTION)
        .rangeFrom(FROM)
        .rangeTo(TO)
        .labels(LABELS)
        .createdBy(CREATED_BY)
        .useMultiHitAlerts(useMultiHitAlerts)
        .build();
  }

  private static Stream<Arguments> provideDomainRequests() {
    return Stream.of(
        Arguments.of(makeDomainRequest(true), MatchQuantity.ALL),
        Arguments.of(makeDomainRequest(false), MatchQuantity.SINGLE));
  }
}
