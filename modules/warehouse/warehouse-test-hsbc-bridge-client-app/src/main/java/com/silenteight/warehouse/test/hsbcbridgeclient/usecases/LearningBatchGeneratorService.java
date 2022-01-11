package com.silenteight.warehouse.test.hsbcbridgeclient.usecases;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import com.silenteight.warehouse.test.hsbcbridgeclient.datageneration.AlertDataSource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class LearningBatchGeneratorService {

  private final ObjectMapper objectMapper;

  private final Resource alertTemplate;

  private final TemplateService templateService;

  @SneakyThrows
  public JsonNode generateBatch(List<AlertDataSource> alertsDataSource) {

    List<ObjectNode> alerts = alertsDataSource.stream()
        .map(alertDataSource -> objectMapper.convertValue(alertDataSource, Map.class))
        .map(alertDataMap -> templateService.template(alertTemplate, alertDataMap))
        .collect(Collectors.toList());

    return objectMapper.createArrayNode()
        .addAll(alerts);
  }
}
