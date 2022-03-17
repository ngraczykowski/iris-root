package com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord;

import com.silenteight.scb.ingest.adapter.incomming.cbs.alertid.AlertId;

import java.util.List;

public interface AlertRecordCompositeReader {

  AlertRecordCompositeCollection readWithCbsHitDetails(
      String dbRelationName, String cbsHitDetailsDbRelationName, List<AlertId> alertIds);

  AlertRecordCompositeCollection read(String dbRelationName, List<AlertId> alertIds);
}
