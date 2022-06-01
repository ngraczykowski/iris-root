package utils.datageneration.namescreening;

import lombok.SneakyThrows;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import utils.datageneration.CommonUtils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.String.join;
import static java.lang.String.valueOf;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;

public class BatchGenerationService {

  private final ObjectMapper objectMapper = new ObjectMapper();
  private final Random random = new Random();
  private static final String ALERT_DATE_PATTERN = "dd-LLL-yy";
  private final CommonUtils commonUtils = new CommonUtils();

  public Batch generateBatchWithSize(int batchSize, String discriminator) {
    String batchId = generateBatchId();
    List<AlertDataSource> alertDataSource = generateData(batchSize, batchId, discriminator);
    return generateBatch(batchId, alertDataSource, commonUtils.getDateTimeNow());
  }

  public Batch generateBatchFrom(Batch batch, String newDiscriminator) {
    return generateBatch(
        batch.getId(),
        batch.getAlertDataSources().stream()
            .map(x -> x.toBuilder().flagKey(newDiscriminator).build())
            .toList(),
        commonUtils.getDateTimeNow());
  }

  public String generateBatchId() {
    String timestamp =
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy-hh-mm-ss.SSSSSSS"));
    String randomValue = valueOf(random.nextInt(1000));

    return join("_QA-batch", randomValue, timestamp);
  }

  public List<AlertDataSource> generateData(
      Integer batchSize, String batchId, String discriminator) {

    return IntStream.range(0, batchSize)
        .mapToObj(i -> generateDataSingleAlert(i, batchId, discriminator))
        .collect(Collectors.toList());
  }

  private AlertDataSource generateDataSingleAlert(
      Integer alertNumber, String batchId, String flagKey) {
    String alertId =
        String.join("_", "QA-Alert", batchId, alertNumber.toString() + randomUUID().toString());
    String alertDate = LocalDateTime.now().format(ofPattern(ALERT_DATE_PATTERN)).toUpperCase();
    String caseId = randomUUID().toString();
    String currentState =
        commonUtils.getRandomValue(
            "True Match Exit Completed", "False Positive", "False Positive", "Level 1 Review");

    return AlertDataSource.builder()
        .alertId(alertId)
        .flagKey(flagKey)
        .alertDate(alertDate)
        .caseId(caseId)
        .currentState(currentState)
        .build();
  }

  public Batch generateBatch(
      String batchId, List<AlertDataSource> alertDataSources, String startDate) {
    return Batch.builder()
        .id(batchId)
        .status("NEW")
        .payload(generatePayload(alertDataSources))
        .alertDataSources(alertDataSources)
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
            .map(alertDataMap -> commonUtils.template(getRandomAlertTemplate(), alertDataMap))
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
    // Intentionally left empty.
  }
}
