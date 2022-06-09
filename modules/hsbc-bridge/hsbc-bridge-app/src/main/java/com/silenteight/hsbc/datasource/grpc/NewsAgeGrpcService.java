package com.silenteight.hsbc.datasource.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.api.watchlist.v2.*;
import com.silenteight.hsbc.datasource.common.DataSourceInputProvider;
import com.silenteight.hsbc.datasource.common.dto.DataSourceInputRequest;
import com.silenteight.hsbc.datasource.dto.newsage.NewsAgeFeatureInputDto;
import com.silenteight.hsbc.datasource.dto.newsage.NewsAgeInputDto;
import com.silenteight.hsbc.datasource.dto.newsage.NewsAgeInputResponse;
import com.silenteight.hsbc.datasource.dto.newsage.NewsAgeWatchlistItemDto;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.stream.Collectors;

@GrpcService(interceptors = DatasourceGrpcInterceptor.class)
@RequiredArgsConstructor
class NewsAgeGrpcService extends NewsAgeInputServiceGrpc.NewsAgeInputServiceImplBase {

  private final DataSourceInputProvider<NewsAgeInputResponse> inputProvider;

  @Override
  public void batchGetMatchNewsAgeInputs(
      BatchGetMatchNewsAgeInputsRequest request,
      StreamObserver<BatchGetMatchNewsAgeInputsResponse> responseObserver) {

    var inputRequest = DataSourceInputRequest.builder()
        .features(request.getFeaturesList())
        .matches(request.getMatchesList())
        .build();

    responseObserver.onNext(provideInput(inputRequest));
    responseObserver.onCompleted();
  }

  private BatchGetMatchNewsAgeInputsResponse provideInput(DataSourceInputRequest request) {
    var inputs = inputProvider.provideInput(request);

    return BatchGetMatchNewsAgeInputsResponse.newBuilder()
        .addAllNewsAgeInputs(mapNewsAgeInputs(inputs.getInputs()))
        .build();
  }

  private List<NewsAgeInput> mapNewsAgeInputs(List<NewsAgeInputDto> inputs) {
    return inputs.stream()
        .map(input -> NewsAgeInput.newBuilder()
            .setMatch(input.getMatch())
            .setNewsAgeFeatureInput(mapNewsAgeFeatureInput(input.getNewsAgeFeatureInput()))
            .build())
        .collect(Collectors.toList());
  }

  private NewsAgeFeatureInput mapNewsAgeFeatureInput(NewsAgeFeatureInputDto input) {
    return NewsAgeFeatureInput.newBuilder()
        .setFeature(input.getFeature())
        .setWatchlistItem(mapWatchlistItem(input.getWatchlistItem()))
        .build();
  }

  private NewsAgeWatchlistItem mapWatchlistItem(NewsAgeWatchlistItemDto input) {
    return NewsAgeWatchlistItem.newBuilder()
        .setId(input.getId())
        .setType(input.getType())
        .setFurtherInformation(input.getFurtherInformation())
        .build();
  }
}
