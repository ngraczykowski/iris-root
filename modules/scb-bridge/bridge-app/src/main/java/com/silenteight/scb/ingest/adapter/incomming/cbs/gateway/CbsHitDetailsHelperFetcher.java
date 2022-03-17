package com.silenteight.scb.ingest.adapter.incomming.cbs.gateway;

import com.silenteight.scb.ingest.adapter.incomming.cbs.domain.CbsHitDetails;

import java.sql.Connection;
import java.util.List;

public interface CbsHitDetailsHelperFetcher {

  List<CbsHitDetails> fetch(Connection connection, String systemId, String batchId);
}
