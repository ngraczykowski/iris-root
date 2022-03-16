package com.silenteight.scb.feeding.domain.model;

import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;

public record FeedUdsCommand(
    Alert alert) {
}
