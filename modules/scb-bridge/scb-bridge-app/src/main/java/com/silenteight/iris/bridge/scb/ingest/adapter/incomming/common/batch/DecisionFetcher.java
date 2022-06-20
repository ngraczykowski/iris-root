/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.batch;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.decision.Decision;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface DecisionFetcher {

  List<Decision> fetchDecisions(Connection connection, String id) throws SQLException;
}
