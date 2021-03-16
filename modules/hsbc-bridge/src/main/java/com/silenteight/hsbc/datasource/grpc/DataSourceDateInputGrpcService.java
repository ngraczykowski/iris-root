package com.silenteight.hsbc.datasource.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.date.v1.*;
import com.silenteight.hsbc.datasource.date.dto.DateInputRequest;
import com.silenteight.hsbc.datasource.date.DateInputProvider;
import com.silenteight.hsbc.datasource.date.dto.DateInputResponse;

import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;

import java.util.List;

import static java.util.stream.Collectors.toList;

@GRpcService
@RequiredArgsConstructor
class DataSourceDateInputGrpcService extends DateInputServiceGrpc.DateInputServiceImplBase {

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

  //TODO fill it
  private List<DateInput> toResponse(DateInputResponse input) {
    return List.of(
        DateInput.newBuilder()
            .setMatch("1")
            .addDateFeatureInputs(DateFeatureInput.newBuilder()
                .setFeature("aaa")
                .addAlertedPartyDates("1234")
                .addWatchlistDates("321")
                .build())
            .build()
    );
  }

  private DateInputRequest createRequest(BatchGetMatchDateInputsRequest request) {
    var matchIds = request.getMatchesList().stream().map(Long::valueOf).collect(toList());

    return DateInputRequest.builder()
        .matchIds(matchIds)
        .build();
  }
}
