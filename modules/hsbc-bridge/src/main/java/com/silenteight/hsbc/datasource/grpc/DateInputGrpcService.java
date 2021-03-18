package com.silenteight.hsbc.datasource.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.date.v1.*;
import com.silenteight.hsbc.datasource.date.dto.DateFeatureInputDto;
import com.silenteight.hsbc.datasource.date.dto.DateInputDto;
import com.silenteight.hsbc.datasource.date.dto.DateInputRequest;
import com.silenteight.hsbc.datasource.date.DateInputProvider;
import com.silenteight.hsbc.datasource.date.dto.DateInputResponse;

import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;

import java.util.List;
import java.util.stream.Collectors;

@GRpcService
@RequiredArgsConstructor
class DateInputGrpcService extends DateInputServiceGrpc.DateInputServiceImplBase {

  private final DateInputProvider dateInputProvider;

  @Override
  public void batchGetMatchDateInputs(
      BatchGetMatchDateInputsRequest request,
      StreamObserver<BatchGetMatchDateInputsResponse> responseObserver) {
    var dateInputRequest = createRequest(request);
    responseObserver.onNext(provideInput(dateInputRequest));
    responseObserver.onCompleted();
  }

  private BatchGetMatchDateInputsResponse provideInput(DateInputRequest request) {
    var input = dateInputProvider.provideInput(request);

    return BatchGetMatchDateInputsResponse.newBuilder()
        .addAllDateInputs(toResponse(input))
        .build();
  }

  private List<DateInput> toResponse(DateInputResponse response) {
    return response.getDateInputs().stream()
        .map(this::toDateInput)
        .collect(Collectors.toList());
  }

  private DateInput toDateInput(DateInputDto dateInputDto) {
    return DateInput.newBuilder()
        .setMatch(dateInputDto.getMatch())
        .addAllDateFeatureInputs(toDateFeatureInputs(dateInputDto.getDateFeatureInputs()))
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
        .build();
  }

  private DateInputRequest createRequest(BatchGetMatchDateInputsRequest request) {
    return DateInputRequest.builder()
        .features(request.getFeaturesList())
        .matches(request.getMatchesList())
        .build();
  }
}
