package com.silenteight.hsbc.datasource.feature.nationalityid;

import com.silenteight.hsbc.bridge.domain.CustomerIndividuals;

import java.util.List;

public interface AlertedPartyDocumentQuery {

  List<String> allDocumentsNumbers();

  interface Factory {

    AlertedPartyDocumentQuery create(CustomerIndividuals customerIndividuals);
  }
}
