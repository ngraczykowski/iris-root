package com.silenteight.hsbc.datasource.feature.nationalityid;

import com.silenteight.hsbc.datasource.datamodel.CustomerIndividual;

import java.util.List;

public interface AlertedPartyDocumentQuery {

  List<String> allDocumentsNumbers();

  interface Factory {

    AlertedPartyDocumentQuery create(CustomerIndividual customerIndividual);
  }
}
