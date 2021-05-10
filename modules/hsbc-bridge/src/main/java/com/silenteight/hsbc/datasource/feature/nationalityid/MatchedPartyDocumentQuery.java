package com.silenteight.hsbc.datasource.feature.nationalityid;

import com.silenteight.hsbc.datasource.datamodel.IndividualComposite;

import java.util.List;

public interface MatchedPartyDocumentQuery {

  List<String> allDocumentsNumbers();

  interface Factory {

    MatchedPartyDocumentQuery create(IndividualComposite individualComposite);
  }
}
