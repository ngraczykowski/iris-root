package com.silenteight.hsbc.datasource.extractors.document;

import com.silenteight.hsbc.datasource.feature.nationaliddocument.NationalIdDocumentQuery;

public class NationalIdDocumentQueryConfigurer {

  public NationalIdDocumentQuery.Factory create() {
    return NationalIdDocumentQueryFacade::new;
  }
}
