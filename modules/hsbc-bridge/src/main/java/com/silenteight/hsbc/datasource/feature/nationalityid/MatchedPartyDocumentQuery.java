package com.silenteight.hsbc.datasource.feature.nationalityid;

import com.silenteight.hsbc.bridge.domain.IndividualComposite;

import java.util.List;

public interface MatchedPartyDocumentQuery {

  List<String> allDocumentsNumbers();

  interface Factory {

    MatchedPartyDocumentQuery create(IndividualComposite individualComposite);
  }
}
