package com.silenteight.hsbc.datasource.grpc;

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
import com.silenteight.hsbc.datasource.feature.country.ValidCountriesProvider;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@GrpcService(interceptors = DatasourceGrpcInterceptor.class)
class CountryInputGrpcService extends CountryInputServiceImplBase {

  private final DataSourceInputProvider<CountryInputResponse> countryInputProvider;
  private final ValidCountriesProvider countriesProvider;

  CountryInputGrpcService(DataSourceInputProvider<CountryInputResponse> countryInputProvider) {
    this.countryInputProvider = countryInputProvider;
    this.countriesProvider = new ValidCountriesProvider();
  }

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
            .addAllWatchlistCountries(
                countriesProvider.validateAndMapCountries((i.getWatchlistCountries())))
            .build())
        .collect(Collectors.toList());
  }
}
