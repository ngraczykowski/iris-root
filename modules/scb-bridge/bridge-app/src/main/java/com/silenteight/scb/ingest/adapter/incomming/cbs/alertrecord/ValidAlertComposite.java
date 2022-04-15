package com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord;

import lombok.Value;

import com.silenteight.scb.ingest.adapter.incomming.cbs.alertid.AlertId;
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;

import java.util.List;

@Value
public class ValidAlertComposite {

  AlertId alertId;
  List<Alert> alerts;
}
