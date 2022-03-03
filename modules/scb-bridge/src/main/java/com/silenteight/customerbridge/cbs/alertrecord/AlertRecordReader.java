package com.silenteight.customerbridge.cbs.alertrecord;

import com.silenteight.customerbridge.common.alertrecord.AlertRecord;

import java.util.Collection;
import java.util.List;

interface AlertRecordReader extends OracleReader {

  List<AlertRecord> read(String dbRelationName, Collection<String> systemIds);
}
