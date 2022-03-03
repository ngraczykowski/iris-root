package com.silenteight.customerbridge.common.recommendation.alertinfo;

import lombok.Value;

import com.silenteight.proto.serp.scb.v1.ScbAlertInfo;
import com.silenteight.proto.serp.v1.reporter.AlertInfo;

//TODO(iwnek) make it package private
@Value
public class AlertInfoMapResult {

  AlertInfo alertInfo;
  ScbAlertInfo scbAlertInfo;
}
