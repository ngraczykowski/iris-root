package com.silenteight.payments.bridge.firco.core.alertmessage.service;

import com.silenteight.payments.bridge.firco.core.alertmessage.model.AlertMessageStatus;
import com.silenteight.payments.bridge.firco.dto.common.StatusInfoDto;
import com.silenteight.payments.bridge.firco.dto.output.AlertDecisionMessageDto;

class AlertDecisionMapper {

  private static final String COMMENT = "MANUAL_INVESTIGATION";

  AlertDecisionMessageDto mapToAlertDecision(AlertMessageEntity entity, AlertMessageStatus status) {
    var decision = new AlertDecisionMessageDto();
    decision.setUnit(entity.getUnit());
    decision.setBusinessUnit(entity.getBusinessUnit());
    decision.setMessageId(entity.getMessageId());
    decision.setStatus(mapToStatusInfo(status));
    decision.setComment(COMMENT);
    return decision;
  }

  private StatusInfoDto mapToStatusInfo(AlertMessageStatus status) {
    var statusInfo = new StatusInfoDto();
    statusInfo.setName(status.name());
    return statusInfo;
  }
}
