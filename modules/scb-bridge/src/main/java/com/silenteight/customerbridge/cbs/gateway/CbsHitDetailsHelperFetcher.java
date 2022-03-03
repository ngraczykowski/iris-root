package com.silenteight.customerbridge.cbs.gateway;

import com.silenteight.customerbridge.cbs.domain.CbsHitDetails;

import java.sql.Connection;
import java.util.List;

public interface CbsHitDetailsHelperFetcher {

  List<CbsHitDetails> fetch(Connection connection, String systemId, String batchId);
}
