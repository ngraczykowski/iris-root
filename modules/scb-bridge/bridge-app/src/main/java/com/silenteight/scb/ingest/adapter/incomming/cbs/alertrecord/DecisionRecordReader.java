package com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord;

import com.silenteight.scb.ingest.adapter.incomming.common.alertrecord.DecisionRecord;

import java.util.Collection;
import java.util.List;

interface DecisionRecordReader extends OracleReader {

  List<DecisionRecord> read(String dbRelationName, Collection<String> systemIds);
}
