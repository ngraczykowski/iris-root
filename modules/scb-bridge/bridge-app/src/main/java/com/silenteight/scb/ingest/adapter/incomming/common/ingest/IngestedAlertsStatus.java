package com.silenteight.scb.ingest.adapter.incomming.common.ingest;

import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;

import java.util.List;

public record IngestedAlertsStatus(
    List<Alert> success,
    List<Alert> failed) {

}
