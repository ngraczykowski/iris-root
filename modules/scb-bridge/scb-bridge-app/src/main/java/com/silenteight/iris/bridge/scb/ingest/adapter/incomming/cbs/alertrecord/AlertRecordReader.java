/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertrecord;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.alertrecord.AlertRecord;

import java.util.Collection;
import java.util.List;

interface AlertRecordReader extends OracleReader {

  List<AlertRecord> read(String dbRelationName, Collection<String> systemIds);
}
