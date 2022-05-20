package com.silenteight.hsbc.datasource.grpc

import com.silenteight.datasource.api.country.v1.BatchGetMatchCountryInputsRequest
import com.silenteight.datasource.api.country.v1.BatchGetMatchCountryInputsResponse
import com.silenteight.hsbc.datasource.common.DataSourceInputProvider
import com.silenteight.hsbc.datasource.common.dto.DataSourceInputRequest
import com.silenteight.hsbc.datasource.dto.country.CountryFeatureInputDto
import com.silenteight.hsbc.datasource.dto.country.CountryInputDto
import com.silenteight.hsbc.datasource.dto.country.CountryInputResponse

import io.grpc.stub.StreamObserver
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class CountryInputGrpcServiceSpec extends Specification {

  def countryInputProvider = Mock(DataSourceInputProvider<CountryInputResponse>)

  @Subject
  def underTest = new CountryInputGrpcService(countryInputProvider)

  @Unroll
  def 'should validate and map countries - #desc'() {
    given:
    def request = BatchGetMatchCountryInputsRequest.newBuilder()
        .addAllFeatures(List.of())
        .addAllMatches(List.of())
        .build()

    def responseObserver = new TestObserver()

    countryInputProvider.provideInput(_ as DataSourceInputRequest) >> CountryInputResponse.builder()
        .inputs(
            List.of(
                CountryInputDto.builder()
                    .match("match")
                    .featureInputs(
                        List.of(
                            CountryFeatureInputDto.builder()
                                .feature("dummy")
                                .alertedPartyCountries(List.of())
                                .watchlistCountries(requestCountries)
                                .build()))
                    .build()))
        .build()

    when:
    underTest.batchGetMatchCountryInputs(request, responseObserver)

    then:
    def result = responseObserver.getResult()
        .getCountryMatches(0)
        .getCountryFeatureInputsList().get(0)
        .getWatchlistCountriesList()

    result.containsAll(expectedCountries)
    result.size() == expectedCountries.size()

    where:
    desc                                        | requestCountries                                                                                                                              | expectedCountries
    "with empty list"                           | []                                                                                                                                            | []
    "with only countries name"                  | ["BONAIRE SINT EUSTATIUS AND SABA", "UNITED ARAB EMIRATES"]                                                                                   | ["BONAIRE SINT EUSTATIUS AND SABA", "UNITED ARAB EMIRATES"]
    "with wrong country code in string"         | ["INDIA", "XX TZ PK"]                                                                                                                         | ["INDIA", "XX TZ PK"]
    "with one string containing country codes"  | ["MALI", "INDIA", "UNITED ARAB EMIRATES", "YEMEN", "PAKISTAN", "IN", "ML IN IN AE IN IN YE PK PK ML IN"]                                      | ["MALI", "INDIA", "UNITED ARAB EMIRATES", "YEMEN", "PAKISTAN", "IN", "ML", "AE", "YE", "PK"]
    "with two strings containing country codes" | ["MALI", "INDIA", "UNITED ARAB EMIRATES", "YEMEN", "PAKISTAN", "IN", "ML IN IN AE IN IN YE PK PK ML IN", "LU IN IN AE IN IN TV PK PK ML IN"]  | ["MALI", "INDIA", "UNITED ARAB EMIRATES", "YEMEN", "PAKISTAN", "IN", "ML", "AE", "YE", "PK", "LU", "TV"]

  }

  private class TestObserver implements StreamObserver<BatchGetMatchCountryInputsResponse> {

    BatchGetMatchCountryInputsResponse result

    @Override
    void onNext(BatchGetMatchCountryInputsResponse value) {
      result = value
    }

    @Override
    void onError(Throwable t) {

    }

    @Override
    void onCompleted() {

    }
  }
}
