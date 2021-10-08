package com.silenteight.hsbc.datasource.extractors.document;

import com.silenteight.hsbc.datasource.feature.document.OtherDocumentQuery;

public class OtherDocumentQueryConfigurer {

  public OtherDocumentQuery.Factory create() {
    return OtherDocumentQueryFacade::new;
  }
}
