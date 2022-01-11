package com.silenteight.warehouse.test.hsbcbridgeclient.usecases;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import com.silenteight.warehouse.test.hsbcbridgeclient.datageneration.AlertDataSource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class IngestBatchGeneratorService {

  public static final String BATCH_ID = "batchId";
  public static final String ALERTS = "alerts";

  private final ObjectMapper objectMapper;

  private final Resource batchTemplate;

  private final Resource alertTemplate;

  private final TemplateService templateService;

  @SneakyThrows
  public JsonNode generateBatch(
      String batchName, List<AlertDataSource> alertsDataSource) {

    List<ObjectNode> alerts = alertsDataSource.stream()
        .map(alertDataSource -> objectMapper.convertValue(alertDataSource, Map.class))
        .map(alertDataMap -> templateService.template(alertTemplate, alertDataMap))
        .collect(Collectors.toList());

    ObjectNode batch = templateService.template(batchTemplate, Map.of(BATCH_ID, batchName));

    ArrayNode alertArray = (ArrayNode) batch.get(ALERTS);
    alertArray.addAll(alerts);
    return batch;
  }
}
