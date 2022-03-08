package com.silenteight.scb.ingest.adapter.incomming.common.batch;

import com.silenteight.proto.serp.v1.alert.Decision;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface DecisionFetcher {

  List<Decision> fetchDecisions(Connection connection, String id) throws SQLException;
}
