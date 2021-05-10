package com.silenteight.hsbc.datasource.extractors.document;

import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.datasource.datamodel.CustomerIndividual;
import com.silenteight.hsbc.datasource.datamodel.IndividualComposite;
import com.silenteight.hsbc.datasource.feature.nationalityid.AlertedPartyDocumentQuery;
import com.silenteight.hsbc.datasource.feature.nationalityid.MatchedPartyDocumentQuery;

import java.util.List;

@RequiredArgsConstructor
public class DocumentQueryConfigurer {

  private static final DocumentExtractor documentExtractor = new DocumentExtractor();

  public AlertedPartyDocumentQuery.Factory alertedPartyDocumentQuery() {
    return customerIndividual -> new AlertedPartyDocumentQueryFacade(
        customerIndividual, documentExtractor);
  }

  public MatchedPartyDocumentQuery.Factory matchPartyDocumentQuery() {
    return individualComposite -> new MatchedPartyDocumentQueryFacade(
        individualComposite, documentExtractor);
  }

  @RequiredArgsConstructor
  private static class AlertedPartyDocumentQueryFacade implements AlertedPartyDocumentQuery {

    private final CustomerIndividual customerIndividual;
    private final DocumentExtractor documentExtractor;

    @Override
    public List<String> allDocumentsNumbers() {
      var document = documentExtractor.convertAlertedPartyDocumentNumbers(customerIndividual);

      return document.getAllDocumentsNumbers();
    }
  }

  @RequiredArgsConstructor
  private static class MatchedPartyDocumentQueryFacade implements MatchedPartyDocumentQuery {

    private final IndividualComposite individualComposite;
    private final DocumentExtractor documentExtractor;

    @Override
    public List<String> allDocumentsNumbers() {
      var document = documentExtractor.convertMatchedPartyDocumentNumbers(individualComposite);

      return document.getAllDocumentsNumbers();
    }
  }
}
