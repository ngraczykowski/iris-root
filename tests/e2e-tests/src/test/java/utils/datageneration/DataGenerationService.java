package utils.datageneration;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.String.join;
import static java.lang.String.valueOf;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.UUID.randomUUID;

public class DataGenerationService {

  private static final String ALERT_DATE_PATTERN = "dd-LLL-yy";
  private final BatchGenerationService batchGenerationService = new BatchGenerationService();
  private final Random random = new Random();

  public String generateBatchId() {
    String timestamp =
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy-hh-mm-ss.SSSSSSS"));
    String randomValue = valueOf(random.nextInt(1000));

    return join("_QA-batch", randomValue, timestamp);
  }

  public List<AlertDataSource> generateData(Integer batchSize, String batchId) {

    return IntStream.range(0, batchSize)
        .mapToObj(i -> generateDataSingleAlert(i, batchId))
        .collect(Collectors.toList());
  }

  private AlertDataSource generateDataSingleAlert(Integer alertNumber, String batchId) {
    String alertId = String.join("_", "QA-Alert", batchId, alertNumber.toString());
    String flagKey = randomUUID().toString();
    String alertDate = LocalDateTime.now().format(ofPattern(ALERT_DATE_PATTERN)).toUpperCase();
    String caseId = randomUUID().toString();
    String currentState =
        getRandomValue(
            "True Match Exit Completed", "False Positive", "False Positive", "Level 1 Review");

    return AlertDataSource.builder()
        .alertId(alertId)
        .flagKey(flagKey)
        .alertDate(alertDate)
        .caseId(caseId)
        .currentState(currentState)
        .build();
  }

  private String getRandomValue(String... allowedValues) {
    if (allowedValues.length < 1) return "";

    int element = random.nextInt(allowedValues.length);
    return allowedValues[element];
  }

  public String getDateTimeNow() {
    return OffsetDateTime.now()
        .atZoneSameInstant(ZoneOffset.UTC)
        .format(DateTimeFormatter.ISO_DATE_TIME);
  }

  public Batch generateBatchWithSize(int batchSize) {
    String batchId = generateBatchId();
    List<AlertDataSource> alertDataSource = generateData(batchSize, batchId);
    return batchGenerationService.generateBatch(batchId, alertDataSource, getDateTimeNow());
  }
}
