package com.silenteight.sens.webapp.backend.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotBlank;

@Data
public class FrontEndProperties {

  private String alertDetailsUrlTemplate;

  private List<DecisionConfig> decisionsConfiguration = new ArrayList<>();

  @Builder
  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  public static class DecisionConfig {

    @NotBlank
    private String key;
    @NotBlank
    private String label;
    private String className;
  }
}
