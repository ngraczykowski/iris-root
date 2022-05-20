package com.silenteight.hsbc.datasource.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.nationalid.v1.BatchGetMatchNationalIdInputsRequest;
import com.silenteight.datasource.api.nationalid.v1.BatchGetMatchNationalIdInputsResponse;
import com.silenteight.datasource.api.nationalid.v1.NationalIdFeatureInput;
import com.silenteight.datasource.api.nationalid.v1.NationalIdInput;
import com.silenteight.datasource.api.nationalid.v1.NationalIdInputServiceGrpc.NationalIdInputServiceImplBase;
import com.silenteight.hsbc.datasource.common.DataSourceInputProvider;
import com.silenteight.hsbc.datasource.common.dto.DataSourceInputRequest;
import com.silenteight.hsbc.datasource.dto.nationalid.NationalIdFeatureInputDto;
import com.silenteight.hsbc.datasource.dto.nationalid.NationalIdInputResponse;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService(interceptors = DatasourceGrpcInterceptor.class)
@RequiredArgsConstructor
class NationalIdInputGrpcService extends NationalIdInputServiceImplBase {

  private final DataSourceInputProvider<NationalIdInputResponse> nationalIdInputProvider;

  @Override
  public void batchGetMatchNationalIdInputs(
      BatchGetMatchNationalIdInputsRequest request,
      StreamObserver<BatchGetMatchNationalIdInputsResponse> responseObserver) {

    var inputRequest = DataSourceInputRequest.builder()
        .features(request.getFeaturesList())
        .matches(request.getMatchesList())
        .build();

    responseObserver.onNext(provideInput(inputRequest));
    responseObserver.onCompleted();
  }

  private BatchGetMatchNationalIdInputsResponse provideInput(DataSourceInputRequest request) {
    var inputs = nationalIdInputProvider.provideInput(request);

    return BatchGetMatchNationalIdInputsResponse.newBuilder()
        .addAllNationalIdInputs(mapNationalIdInputs(inputs))
        .build();
  }

  private List<NationalIdInput> mapNationalIdInputs(NationalIdInputResponse inputs) {
    return inputs.getInputs().stream()
        .map(i -> NationalIdInput.newBuilder()
            .setMatch(i.getMatch())
            .addAllNationalIdFeatureInputs(mapFeatureInputs(i.getFeatureInputs()))
            .build())
        .collect(Collectors.toList());
  }

  private List<NationalIdFeatureInput> mapFeatureInputs(List<NationalIdFeatureInputDto> inputs) {
    return inputs.stream()
        .map(i -> NationalIdFeatureInput.newBuilder()
            .setFeature(i.getFeature())
            .addAllAlertedPartyCountries(i.getAlertedPartyCountries())
            .addAllWatchlistCountries(i.getWatchlistCountries())
            .addAllAlertedPartyDocumentNumbers(i.getAlertedPartyDocumentNumbers())
            .addAllWatchlistDocumentNumbers(i.getWatchlistDocumentNumbers())
            .build())
        .collect(Collectors.toList());
  }
}
