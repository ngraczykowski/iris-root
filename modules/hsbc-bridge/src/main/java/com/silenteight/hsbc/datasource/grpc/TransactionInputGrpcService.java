package com.silenteight.hsbc.datasource.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.transaction.v1.BatchGetMatchTransactionInputsRequest;
import com.silenteight.datasource.api.transaction.v1.BatchGetMatchTransactionInputsResponse;
import com.silenteight.datasource.api.transaction.v1.TransactionFeatureInput;
import com.silenteight.datasource.api.transaction.v1.TransactionInput;
import com.silenteight.datasource.api.transaction.v1.TransactionInputServiceGrpc.TransactionInputServiceImplBase;
import com.silenteight.hsbc.datasource.common.DataSourceInputProvider;
import com.silenteight.hsbc.datasource.common.dto.DataSourceInputRequest;
import com.silenteight.hsbc.datasource.dto.transaction.TransactionFeatureInputDto;
import com.silenteight.hsbc.datasource.dto.transaction.TransactionInputDto;
import com.silenteight.hsbc.datasource.dto.transaction.TransactionInputResponse;

import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;

import java.util.List;
import java.util.stream.Collectors;

@GRpcService
@RequiredArgsConstructor
class TransactionInputGrpcService extends TransactionInputServiceImplBase {

  private final DataSourceInputProvider<TransactionInputResponse> transactionInputProvider;

  @Override
  public void batchGetMatchTransactionInputs(
      BatchGetMatchTransactionInputsRequest request,
      StreamObserver<BatchGetMatchTransactionInputsResponse> responseObserver) {
    responseObserver.onNext(provideInput(DataSourceInputRequest.builder()
        .features(request.getFeaturesList())
        .matches(request.getMatchesList())
        .build()));
    responseObserver.onCompleted();
  }

  private BatchGetMatchTransactionInputsResponse provideInput(DataSourceInputRequest request) {
    var inputs = transactionInputProvider.provideInput(request);

    return BatchGetMatchTransactionInputsResponse.newBuilder()
        .addAllTransactionInputs(mapTransactionInputs(inputs.getInputs()))
        .build();
  }

  private List<TransactionInput> mapTransactionInputs(List<TransactionInputDto> inputs) {
    return inputs.stream()
        .map(i -> TransactionInput.newBuilder()
            .setMatch(i.getMatch())
            .addAllTransactionFeatureInputs(mapFeatureInputs(i.getTransactionFeatureInputs()))
            .build())
        .collect(Collectors.toList());
  }

  private List<TransactionFeatureInput> mapFeatureInputs(List<TransactionFeatureInputDto> inputs) {
    return inputs.stream()
        .map(i -> TransactionFeatureInput.newBuilder()
            .setFeature(i.getFeature())
            .addAllTransactionMessages(i.getTransactionMessages())
            // add more
            .build())
        .collect(Collectors.toList());
  }
}
