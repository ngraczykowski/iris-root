package com.silenteight.hsbc.datasource.grpc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.api.country.v1.BatchGetMatchCountryInputsRequest;
import com.silenteight.datasource.api.country.v1.BatchGetMatchCountryInputsResponse;
import com.silenteight.datasource.api.country.v1.CountryFeatureInput;
import com.silenteight.datasource.api.country.v1.CountryInput;
import com.silenteight.datasource.api.country.v1.CountryInputServiceGrpc.CountryInputServiceImplBase;
import com.silenteight.hsbc.datasource.common.DataSourceInputProvider;
import com.silenteight.hsbc.datasource.common.dto.DataSourceInputRequest;
import com.silenteight.hsbc.datasource.dto.country.CountryFeatureInputDto;
import com.silenteight.hsbc.datasource.dto.country.CountryInputDto;
import com.silenteight.hsbc.datasource.dto.country.CountryInputResponse;

import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@GRpcService(interceptors = DatasourceGrpcInterceptor.class)
@RequiredArgsConstructor
class CountryInputGrpcService extends CountryInputServiceImplBase {

  private final DataSourceInputProvider<CountryInputResponse> countryInputProvider;

  @Override
  public void batchGetMatchCountryInputs(
      BatchGetMatchCountryInputsRequest request,
      StreamObserver<BatchGetMatchCountryInputsResponse> responseObserver) {

    var inputRequest = DataSourceInputRequest.builder()
        .features(request.getFeaturesList())
        .matches(request.getMatchesList())
        .build();

    responseObserver.onNext(provideInputResponse(inputRequest));
    responseObserver.onCompleted();
  }

  private BatchGetMatchCountryInputsResponse provideInputResponse(DataSourceInputRequest request) {
    var input = countryInputProvider.provideInput(request);

    return BatchGetMatchCountryInputsResponse.newBuilder()
        .addAllCountryMatches(mapCountryMatches(input.getInputs()))
        .build();
  }

  private List<CountryInput> mapCountryMatches(List<CountryInputDto> inputs) {
    return inputs.stream()
        .map(t -> CountryInput.newBuilder()
            .setMatch(t.getMatch())
            .addAllCountryFeatureInputs(toCountryFeatureInput(t.getFeatureInputs()))
            .build())
        .collect(Collectors.toList());
  }

  private List<CountryFeatureInput> toCountryFeatureInput(List<CountryFeatureInputDto> inputs) {
    return inputs.stream()
        .map(i -> CountryFeatureInput.newBuilder()
            .setFeature(i.getFeature())
            .addAllAlertedPartyCountries(i.getAlertedPartyCountries())
            .addAllWatchlistCountries(i.getWatchlistCountries())
            .build())
        .collect(Collectors.toList());
  }
}
