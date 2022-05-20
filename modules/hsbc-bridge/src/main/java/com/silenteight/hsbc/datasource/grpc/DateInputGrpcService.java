package com.silenteight.hsbc.datasource.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.date.v1.*;
import com.silenteight.datasource.api.date.v1.DateFeatureInput.EntityType;
import com.silenteight.datasource.api.date.v1.DateFeatureInput.SeverityMode;
import com.silenteight.hsbc.datasource.common.DataSourceInputProvider;
import com.silenteight.hsbc.datasource.common.dto.DataSourceInputRequest;
import com.silenteight.hsbc.datasource.dto.date.DateFeatureInputDto;
import com.silenteight.hsbc.datasource.dto.date.DateInputDto;
import com.silenteight.hsbc.datasource.dto.date.DateInputResponse;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService(interceptors = DatasourceGrpcInterceptor.class)
@RequiredArgsConstructor
class DateInputGrpcService extends DateInputServiceGrpc.DateInputServiceImplBase {

  private final DataSourceInputProvider<DateInputResponse> dateInputProvider;

  @Override
  public void batchGetMatchDateInputs(
      BatchGetMatchDateInputsRequest request,
      StreamObserver<BatchGetMatchDateInputsResponse> responseObserver) {

    var inputRequest = createRequest(request);

    responseObserver.onNext(provideInput(inputRequest));
    responseObserver.onCompleted();
  }

  private BatchGetMatchDateInputsResponse provideInput(DataSourceInputRequest request) {
    var input = dateInputProvider.provideInput(request);

    return BatchGetMatchDateInputsResponse.newBuilder()
        .addAllDateInputs(toResponse(input))
        .build();
  }

  private List<DateInput> toResponse(DateInputResponse response) {
    return response.getInputs().stream()
        .map(this::toDateInput)
        .collect(Collectors.toList());
  }

  private DateInput toDateInput(DateInputDto dateInputDto) {
    return DateInput.newBuilder()
        .setMatch(dateInputDto.getMatch())
        .addAllDateFeatureInputs(toDateFeatureInputs(dateInputDto.getFeatureInputs()))
        .build();
  }

  private List<DateFeatureInput> toDateFeatureInputs(List<DateFeatureInputDto> inputs) {
    return inputs.stream().map(this::toDateFeatureInput).collect(Collectors.toList());
  }

  private DateFeatureInput toDateFeatureInput(DateFeatureInputDto dateFeatureInputDto) {
    return DateFeatureInput.newBuilder()
        .setFeature(dateFeatureInputDto.getFeature())
        .addAllAlertedPartyDates(dateFeatureInputDto.getAlertedPartyDates())
        .addAllWatchlistDates(dateFeatureInputDto.getWatchlistDates())
        .setAlertedPartyType(EntityType.valueOf(dateFeatureInputDto.getAlertedPartyType().name()))
        .setMode(SeverityMode.valueOf(dateFeatureInputDto.getMode().name()))
        .build();
  }

  private DataSourceInputRequest createRequest(BatchGetMatchDateInputsRequest request) {
    return DataSourceInputRequest.builder()
        .features(request.getFeaturesList())
        .matches(request.getMatchesList())
        .build();
  }
}
