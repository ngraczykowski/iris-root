package com.silenteight.sens.webapp.backend.config;

import lombok.Data;

import org.springframework.validation.annotation.Validated;

import java.util.HashMap;
import java.util.Map;

@Data
@Validated
public class TranslationProperties {

  private Map<String, String> agentOutputs = new HashMap<>();
}
