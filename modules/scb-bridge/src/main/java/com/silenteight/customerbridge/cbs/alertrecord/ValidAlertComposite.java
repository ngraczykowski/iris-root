package com.silenteight.customerbridge.cbs.alertrecord;

import lombok.Value;

import com.silenteight.customerbridge.cbs.alertid.AlertId;
import com.silenteight.proto.serp.v1.alert.Alert;

import java.util.List;

@Value
class ValidAlertComposite {

  AlertId alertId;
  List<Alert> alerts;
}
