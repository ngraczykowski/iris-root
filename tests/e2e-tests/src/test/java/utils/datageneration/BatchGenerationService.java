package utils.datageneration;

import lombok.SneakyThrows;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static java.util.stream.Collectors.toList;

public class BatchGenerationService {

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final TemplateService templateService = new TemplateService();
  private final Random random = new Random();

  public Batch generateBatch(
      String batchId, List<AlertDataSource> alertDataSources, String startDate) {
    return Batch.builder()
        .id(batchId)
        .status("NEW")
        .payload(generatePayload(alertDataSources))
        .generationStartTime(startDate)
        .build();
  }

  @SneakyThrows
  private String generatePayload(List<AlertDataSource> alertsDataSource) {
    List<ObjectNode> alerts =
        alertsDataSource.stream()
            .map(
                alertDataSource ->
                    objectMapper.convertValue(alertDataSource, new AlertDataTypeRef()))
            .map(alertDataMap -> templateService.template(getRandomAlertTemplate(), alertDataMap))
            .map(this::asObjectNode)
            .collect(toList());

    ArrayNode jsonNodes = objectMapper.createArrayNode().addAll(alerts);

    return objectMapper.writeValueAsString(jsonNodes);
  }

  @SneakyThrows
  private String getRandomAlertTemplate() {
    return new String(
        Files.readAllBytes(
            Paths.get(
                String.format(
                    "src/test/resources/alertTemplates/alertTemplate%d.json",
                    random.nextInt(3) + 1))));
  }

  @SneakyThrows
  private ObjectNode asObjectNode(String body) {
    return (ObjectNode) objectMapper.readTree(body);
  }

  private static class AlertDataTypeRef extends TypeReference<Map<String, String>> {
    // Intentinally left empty.
  }
}
