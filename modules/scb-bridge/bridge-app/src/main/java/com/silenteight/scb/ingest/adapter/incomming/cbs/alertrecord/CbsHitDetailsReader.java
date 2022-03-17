package com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord;

import com.silenteight.scb.ingest.adapter.incomming.cbs.alertid.AlertId;
import com.silenteight.scb.ingest.adapter.incomming.cbs.domain.CbsHitDetails;

import java.util.Collection;
import java.util.List;

interface CbsHitDetailsReader extends OracleReader {

  List<CbsHitDetails> read(String dbRelationName, Collection<AlertId> alertIds);
}
