package com.silenteight.hsbc.datasource.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.event.v1.BatchGetMatchEventInputsRequest;
import com.silenteight.datasource.api.event.v1.BatchGetMatchEventInputsResponse;
import com.silenteight.datasource.api.event.v1.EventFeatureInput;
import com.silenteight.datasource.api.event.v1.EventInput;
import com.silenteight.datasource.api.event.v1.EventInputServiceGrpc.EventInputServiceImplBase;
import com.silenteight.hsbc.datasource.common.DataSourceInputProvider;
import com.silenteight.hsbc.datasource.common.dto.DataSourceInputRequest;
import com.silenteight.hsbc.datasource.dto.event.EventFeatureInputDto;
import com.silenteight.hsbc.datasource.dto.event.EventInputDto;
import com.silenteight.hsbc.datasource.dto.event.EventInputResponse;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService(interceptors = DatasourceGrpcInterceptor.class)
@RequiredArgsConstructor
class EventInputGrpcService extends EventInputServiceImplBase {

  private final DataSourceInputProvider<EventInputResponse> eventInputProvider;

  @Override
  public void batchGetMatchEventInputs(
      BatchGetMatchEventInputsRequest request,
      StreamObserver<BatchGetMatchEventInputsResponse> responseObserver) {

    var inputRequest = DataSourceInputRequest.builder()
        .features(request.getFeaturesList())
        .matches(request.getMatchesList())
        .build();

    responseObserver.onNext(provideInput(inputRequest));
    responseObserver.onCompleted();
  }

  private BatchGetMatchEventInputsResponse provideInput(DataSourceInputRequest request) {
    var input = eventInputProvider.provideInput(request);

    return BatchGetMatchEventInputsResponse.newBuilder()
        .addAllEventInputs(mapInputs(input.getInputs()))
        .build();
  }

  private List<EventInput> mapInputs(List<EventInputDto> inputs) {
    return inputs.stream()
        .map(i -> EventInput.newBuilder()
            .setMatch(i.getMatch())
            .addAllEventFeatureInputs(mapFeatureInputs(i.getFeatureInputs()))
            .build())
        .collect(Collectors.toList());
  }

  private List<EventFeatureInput> mapFeatureInputs(List<EventFeatureInputDto> inputs) {
    return inputs.stream()
        .map(i -> EventFeatureInput.newBuilder()
            .setFeature(i.getFeature())
            .addAllAlertedPartyDates(i.getAlertedPartyDates())
            .addAllWatchlistEvents(i.getWatchlistEvents())
            .build())
        .collect(Collectors.toList());
  }
}
