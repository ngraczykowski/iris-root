package com.silenteight.customerbridge.cbs.alertrecord;

import com.silenteight.customerbridge.common.alertrecord.DecisionRecord;

import java.util.Collection;
import java.util.List;

interface DecisionRecordReader extends OracleReader {

  List<DecisionRecord> read(String dbRelationName, Collection<String> systemIds);
}
