/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertrecord;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.alertrecord.DecisionRecord;

import java.util.Collection;
import java.util.List;

interface DecisionRecordReader extends OracleReader {

  List<DecisionRecord> read(String dbRelationName, Collection<String> systemIds);
}
