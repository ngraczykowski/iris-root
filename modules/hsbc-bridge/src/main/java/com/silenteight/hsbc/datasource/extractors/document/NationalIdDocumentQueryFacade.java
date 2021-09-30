package com.silenteight.hsbc.datasource.extractors.document;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.extractors.country.NationalityCountryQueryFacade;
import com.silenteight.hsbc.datasource.feature.nationaliddocument.NationalIdDocumentQuery;

import java.util.stream.Stream;

import static java.util.stream.Stream.concat;

@RequiredArgsConstructor
class NationalIdDocumentQueryFacade implements NationalIdDocumentQuery {

  private final MatchData matchData;
  private static final DocumentExtractor DOCUMENT_EXTRACTOR = new DocumentExtractor();

  @Override
  public Stream<String> apNationalIds() {
    return extractApDocuments().getNationalIds().stream();
  }

  @Override
  public Stream<String> mpNationalIds() {
    return extractMpDocuments().getNationalIds().stream();
  }

  @Override
  public Stream<String> apCountries() {
    var countryFacade = new NationalityCountryQueryFacade(matchData);
    var apIdDocumentCountry = countryFacade.apLine4DocumentCountry();
    var apFieldsCountries = countryFacade.apFieldsIndividualCountries();

    return concat(apFieldsCountries, apIdDocumentCountry);
  }

  @Override
  public Stream<String> mpCountries() {
    var countryFacade = new NationalityCountryQueryFacade(matchData);
    var mpCountries = countryFacade.getWatchlistIndividualsNationalityCountry();
    var mpDocumentCountries = extractMpDocuments().getAllCountries().stream();

    return Stream.concat(mpCountries, mpDocumentCountries);
  }

  private Document extractApDocuments() {
    return DOCUMENT_EXTRACTOR.convertAlertedPartyDocumentNumbers(matchData.getCustomerIndividual());
  }

  private Document extractMpDocuments() {
    return DOCUMENT_EXTRACTOR.convertMatchedPartyDocumentNumbers(matchData);
  }
}
