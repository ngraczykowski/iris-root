package com.silenteight.hsbc.datasource.extractors.document;

import com.silenteight.hsbc.datasource.feature.document.PassportNumberDocumentQuery;

public class PassportNumberDocumentQueryConfigurer {

  public PassportNumberDocumentQuery.Factory create() {
    return PassportNumberDocumentQueryFacade::new;
  }
}
