/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertrecord;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertid.AlertId;
import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.domain.CbsHitDetails;

import java.util.Collection;
import java.util.List;

interface CbsHitDetailsReader extends OracleReader {

  List<CbsHitDetails> read(String dbRelationName, Collection<AlertId> alertIds);
}
