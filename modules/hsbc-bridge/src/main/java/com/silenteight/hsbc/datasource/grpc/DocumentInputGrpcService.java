package com.silenteight.hsbc.datasource.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.document.v1.BatchGetMatchDocumentInputsRequest;
import com.silenteight.datasource.api.document.v1.BatchGetMatchDocumentInputsResponse;
import com.silenteight.datasource.api.document.v1.DocumentFeatureInput;
import com.silenteight.datasource.api.document.v1.DocumentInput;
import com.silenteight.datasource.api.document.v1.DocumentInputServiceGrpc.DocumentInputServiceImplBase;
import com.silenteight.hsbc.datasource.common.DataSourceInputProvider;
import com.silenteight.hsbc.datasource.common.dto.DataSourceInputRequest;
import com.silenteight.hsbc.datasource.dto.document.DocumentFeatureInputDto;
import com.silenteight.hsbc.datasource.dto.document.DocumentInputDto;
import com.silenteight.hsbc.datasource.dto.document.DocumentInputResponse;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService(interceptors = DatasourceGrpcInterceptor.class)
@RequiredArgsConstructor
class DocumentInputGrpcService extends DocumentInputServiceImplBase {

  private final DataSourceInputProvider<DocumentInputResponse> documentInputProvider;

  @Override
  public void batchGetMatchDocumentInputs(
      BatchGetMatchDocumentInputsRequest request,
      StreamObserver<BatchGetMatchDocumentInputsResponse> responseObserver) {

    var inputRequest = DataSourceInputRequest.builder()
        .features(request.getFeaturesList())
        .matches(request.getMatchesList())
        .build();

    responseObserver.onNext(toResponse(inputRequest));
    responseObserver.onCompleted();
  }

  private BatchGetMatchDocumentInputsResponse toResponse(DataSourceInputRequest request) {
    var input = documentInputProvider.provideInput(request);

    return BatchGetMatchDocumentInputsResponse.newBuilder()
        .addAllDocumentInputs(map(input.getInputs()))
        .build();
  }

  private List<DocumentInput> map(List<DocumentInputDto> inputs) {
    return inputs.stream()
        .map(i -> DocumentInput.newBuilder()
            .setMatch(i.getMatch())
            .addAllDocumentFeaturesInput(mapFeatureInputs(i.getFeatureInputs()))
            .build())
        .collect(Collectors.toList());
  }

  private List<DocumentFeatureInput> mapFeatureInputs(List<DocumentFeatureInputDto> inputs) {
    return inputs.stream()
        .map(i -> DocumentFeatureInput.newBuilder()
            .setFeature(i.getFeature())
            .addAllAlertedPartyDocuments(i.getAlertedPartyDocuments())
            .addAllWatchlistDocuments(i.getWatchlistDocuments())
            .build())
        .collect(Collectors.toList());
  }
}
