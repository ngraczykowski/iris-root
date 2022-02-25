package com.silenteight.bridge.core.registration.domain.model;

import java.util.List;

public record BatchTimedOut(String analysisName, List<String> alertNames) {}
