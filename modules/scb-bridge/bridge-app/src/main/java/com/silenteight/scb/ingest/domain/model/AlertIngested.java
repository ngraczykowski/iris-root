package com.silenteight.scb.ingest.domain.model;

import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert;

public record AlertIngested(
    Alert alert) {
}
