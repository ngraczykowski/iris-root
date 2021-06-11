package com.silenteight.warehouse.indexer.alert;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class AlertsAttributesListDto {

  private List<AlertAttributes> alerts;

  @Data
  @Builder
  public static class AlertAttributes {

    Map<String, String> attributes;
  }
}
