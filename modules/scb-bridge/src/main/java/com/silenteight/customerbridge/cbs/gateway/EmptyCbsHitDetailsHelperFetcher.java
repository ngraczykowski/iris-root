package com.silenteight.customerbridge.cbs.gateway;

import com.silenteight.customerbridge.cbs.domain.CbsHitDetails;

import java.sql.Connection;
import java.util.List;

import static java.util.Collections.emptyList;

class EmptyCbsHitDetailsHelperFetcher implements CbsHitDetailsHelperFetcher {

  @Override
  public List<CbsHitDetails> fetch(
      Connection connection, String systemId, String batchId) {
    return emptyList();
  }
}
