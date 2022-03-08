package com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord;

import lombok.Value;

import com.silenteight.proto.serp.v1.alert.Alert;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertid.AlertId;

import java.util.List;

@Value
class ValidAlertComposite {

  AlertId alertId;
  List<Alert> alerts;
}
