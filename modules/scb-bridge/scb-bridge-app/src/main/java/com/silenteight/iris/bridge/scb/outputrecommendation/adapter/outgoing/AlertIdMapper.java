/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.outputrecommendation.adapter.outgoing;

import lombok.experimental.UtilityClass;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertid.AlertId;
import com.silenteight.iris.bridge.scb.outputrecommendation.domain.model.CbsAlertRecommendation;

import java.util.List;

@UtilityClass
public class AlertIdMapper {

  List<AlertId> mapFromAlertRecommendations(List<CbsAlertRecommendation> alertRecommendations) {
    return alertRecommendations.stream()
        .map(alertRecommendation -> new AlertId(
            alertRecommendation.getAlertExternalId(),
            alertRecommendation.getBatchId()))
        .distinct()
        .toList();
  }
}
