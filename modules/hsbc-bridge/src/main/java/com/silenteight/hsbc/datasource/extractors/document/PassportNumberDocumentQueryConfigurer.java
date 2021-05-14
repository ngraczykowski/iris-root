package com.silenteight.hsbc.datasource.extractors.document;

import com.silenteight.hsbc.datasource.feature.passportnumberdocument.PassportNumberDocumentQuery;

public class PassportNumberDocumentQueryConfigurer {

  public PassportNumberDocumentQuery.Factory create() {
    return PassportNumberDocumentQueryFacade::new;
  }
}
