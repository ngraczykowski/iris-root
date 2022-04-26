package com.silenteight.scb.outputrecommendation.adapter.outgoing;

import lombok.experimental.UtilityClass;

import com.silenteight.scb.ingest.adapter.incomming.cbs.alertid.AlertId;
import com.silenteight.scb.outputrecommendation.domain.model.CbsAlertRecommendation;

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
