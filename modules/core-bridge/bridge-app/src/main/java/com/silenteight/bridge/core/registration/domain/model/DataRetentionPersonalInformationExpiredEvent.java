package com.silenteight.bridge.core.registration.domain.model;

import java.util.List;

public record DataRetentionPersonalInformationExpiredEvent(List<AlertToRetention> alerts) {}
