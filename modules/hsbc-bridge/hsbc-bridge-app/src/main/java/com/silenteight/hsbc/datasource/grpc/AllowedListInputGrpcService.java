package com.silenteight.hsbc.datasource.grpc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.api.allowlist.v1.AllowListFeatureInput;
import com.silenteight.datasource.api.allowlist.v1.AllowListInput;
import com.silenteight.datasource.api.allowlist.v1.AllowListInputServiceGrpc.AllowListInputServiceImplBase;
import com.silenteight.datasource.api.allowlist.v1.BatchGetMatchAllowListInputsRequest;
import com.silenteight.datasource.api.allowlist.v1.BatchGetMatchAllowListInputsResponse;
import com.silenteight.hsbc.datasource.common.DataSourceInputProvider;
import com.silenteight.hsbc.datasource.common.dto.DataSourceInputRequest;
import com.silenteight.hsbc.datasource.dto.allowedlist.AllowListFeatureInputDto;
import com.silenteight.hsbc.datasource.dto.allowedlist.AllowListInputDto;
import com.silenteight.hsbc.datasource.dto.allowedlist.AllowedListInputResponse;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@GrpcService(interceptors = DatasourceGrpcInterceptor.class)
@RequiredArgsConstructor
class AllowedListInputGrpcService extends AllowListInputServiceImplBase {

  private final DataSourceInputProvider<AllowedListInputResponse> allowedListInputProvider;

  @Override
  public void batchGetMatchAllowListInputs(
      BatchGetMatchAllowListInputsRequest request,
      StreamObserver<BatchGetMatchAllowListInputsResponse> responseObserver) {
    var inputRequest = DataSourceInputRequest.builder()
        .features(request.getFeaturesList())
        .matches(request.getMatchesList())
        .build();

    responseObserver.onNext(provideInputResponse(inputRequest));
    responseObserver.onCompleted();
  }

  private BatchGetMatchAllowListInputsResponse provideInputResponse(DataSourceInputRequest request) {
    var input = allowedListInputProvider.provideInput(request);

    return BatchGetMatchAllowListInputsResponse.newBuilder()
        .addAllAllowListInputs(mapAllowedListMatches(input.getInputs()))
        .build();
  }

  private List<AllowListInput> mapAllowedListMatches(List<AllowListInputDto> inputs) {
    return inputs.stream()
        .map(t -> AllowListInput.newBuilder()
            .setMatch(t.getMatch())
            .addAllAllowListFeatureInputs(toAllowListFeatureInput(t.getFeatureInputs()))
            .build())
        .collect(Collectors.toList());
  }

  private List<AllowListFeatureInput> toAllowListFeatureInput(List<AllowListFeatureInputDto> inputs) {
    return inputs.stream()
        .map(i -> AllowListFeatureInput.newBuilder()
            .setFeature(i.getFeature())
            .addAllAllowListName(i.getAllowListNames())
            .addAllCharacteristicsValues(i.getCharacteristicsValues())
            .build())
        .collect(Collectors.toList());
  }
}
