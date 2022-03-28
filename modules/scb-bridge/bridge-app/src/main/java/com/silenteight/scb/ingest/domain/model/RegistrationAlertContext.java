package com.silenteight.scb.ingest.domain.model;

import com.silenteight.scb.ingest.domain.model.Batch.Priority;

public record RegistrationAlertContext(Priority priority, AlertSource alertSource) {
}
