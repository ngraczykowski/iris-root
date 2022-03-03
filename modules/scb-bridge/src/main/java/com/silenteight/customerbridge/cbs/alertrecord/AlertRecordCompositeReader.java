package com.silenteight.customerbridge.cbs.alertrecord;

import com.silenteight.customerbridge.cbs.alertid.AlertId;

import java.util.List;

public interface AlertRecordCompositeReader {

  AlertRecordCompositeCollection readWithCbsHitDetails(
      String dbRelationName, String cbsHitDetailsDbRelationName, List<AlertId> alertIds);

  AlertRecordCompositeCollection read(String dbRelationName, List<AlertId> alertIds);
}
