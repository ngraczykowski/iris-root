package com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord;

import com.silenteight.proto.serp.scb.v1.ScbAlertIdContext;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertid.AlertId;

import java.util.List;

public interface AlertRecordCompositeReader {

  AlertRecordCompositeCollection read(ScbAlertIdContext context, List<AlertId> alertIds);
}
