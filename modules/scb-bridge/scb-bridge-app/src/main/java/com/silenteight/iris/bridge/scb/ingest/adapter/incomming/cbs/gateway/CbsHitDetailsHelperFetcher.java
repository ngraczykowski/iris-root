/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.gateway;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.domain.CbsHitDetails;

import java.sql.Connection;
import java.util.List;

public interface CbsHitDetailsHelperFetcher {

  List<CbsHitDetails> fetch(Connection connection, String systemId, String batchId);
}
