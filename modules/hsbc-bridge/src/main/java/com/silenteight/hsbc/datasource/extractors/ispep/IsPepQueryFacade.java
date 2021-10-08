package com.silenteight.hsbc.datasource.extractors.ispep;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.feature.ispep.IsPepQuery;

import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
class IsPepQueryFacade implements IsPepQuery {

  private final MatchData matchData;
  private final IsPepServiceClient isPepServiceClient;

  @Override
  public Stream<String> apIndividualExtractEdqLobCountryCode() {
    var customerIndividuals = matchData.getCustomerIndividuals();
    return new CustomerIndividualIsPepExtractor(customerIndividuals)
        .extract();
  }

  @Override
  public GetModelFieldNamesResponseDto provideRequiredModelFieldNames() {
    var bankRegion = new GetModelFieldNamesRequestDto("GLOBAL");
    return isPepServiceClient.getModelFieldNames(bankRegion);
  }

  @Override
  public List<IsPepResponseDto> verifyIsPep(
      List<String> apFields, GetModelFieldNamesResponseDto modelFieldNamesResponse) {
    return new VerifyIsPepResponseExtractor(matchData, isPepServiceClient).extract(
        apFields, modelFieldNamesResponse);
  }
}
