package com.silenteight.warehouse.common.opendistro.kibana;

import lombok.NoArgsConstructor;

import com.silenteight.warehouse.common.opendistro.kibana.ReportDefinition.CoreParams;
import com.silenteight.warehouse.common.opendistro.kibana.ReportDefinition.ReportDefinitionDetails;
import com.silenteight.warehouse.common.opendistro.kibana.ReportDefinition.ReportParams;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

import static com.silenteight.warehouse.common.opendistro.kibana.SearchFixture.SEARCH_ID;
import static java.util.Map.of;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class ReportDefinitionFixture {

  private static JsonNode asJsonNode(Map<String, Object> map) {
    return new ObjectMapper().valueToTree(map);
  }

  static final String REPORT_NAME = "all-alerts-report";

  static final ReportDefinitionDetails REPORT_DEFINITION = ReportDefinitionDetails.builder()
      .reportParams(ReportParams.builder()
          .reportName(REPORT_NAME)
          .reportSource("Saved search")
          .description("")
          .coreParams(CoreParams.builder()
              .baseUrl("/app/discover#/view/" + SEARCH_ID)
              .reportFormat("csv")
              .limit(10000)
              .timeDuration("PT262992H")
              .savedSearchId(SEARCH_ID)
              .origin("http://localhost:5601")
              .excel(true)
              .build())
          .build())
      .trigger(asJsonNode(of(
          "trigger_type", "On demand")))
      .delivery(asJsonNode(of(
          "delivery_type", "Kibana user",
          "delivery_params", of(
              "kibana_recipients", List.of()))))
      .timeCreated(1620810024621L)
      .lastUpdated(1620810024621L)
      .status("Active")
      .build();
}
