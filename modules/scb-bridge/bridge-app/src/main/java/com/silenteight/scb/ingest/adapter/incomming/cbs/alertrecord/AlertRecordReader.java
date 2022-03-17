package com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord;

import com.silenteight.scb.ingest.adapter.incomming.common.alertrecord.AlertRecord;

import java.util.Collection;
import java.util.List;

interface AlertRecordReader extends OracleReader {

  List<AlertRecord> read(String dbRelationName, Collection<String> systemIds);
}
