package com.silenteight.hsbc.datasource.extractors.document;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.MatchData;
import com.silenteight.hsbc.datasource.feature.passportnumberdocument.PassportNumberDocumentQuery;

import java.util.stream.Stream;

@RequiredArgsConstructor
public class PassportNumberDocumentQueryFacade implements PassportNumberDocumentQuery {

  private final MatchData matchData;
  private static final DocumentExtractor documentExtractor = new DocumentExtractor();

  @Override
  public Stream<String> apPassportNumbers() {
    return extractApDocuments().getPassportNumbers().stream();
  }

  @Override
  public Stream<String> mpPassportNumbers() {
    return extractMpDocuments().getPassportNumbers().stream();
  }

  private Document extractApDocuments() {
    return documentExtractor.convertAlertedPartyDocumentNumbers(matchData.getCustomerIndividual());
  }

  private Document extractMpDocuments() {
    return documentExtractor.convertMatchedPartyDocumentNumbers(matchData);
  }
}
