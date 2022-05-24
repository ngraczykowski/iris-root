package utils.datageneration;

import lombok.SneakyThrows;

import org.apache.commons.text.StringSubstitutor;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Random;

public class CommonUtils {
  Random random = new Random();

  @SneakyThrows
  public String getJsonTemplate(String jsonName) {
    return new String(Files.readAllBytes(
        Paths.get(String.format("src/test/resources/s8api/%s.json", jsonName))));
  }

  public String getRandomValue(String... allowedValues) {
    if (allowedValues.length < 1)
      return "";

    int element = random.nextInt(allowedValues.length);
    return allowedValues[element];
  }

  public String getDateTimeNow() {
    return OffsetDateTime
        .now()
        .atZoneSameInstant(ZoneOffset.UTC)
        .format(DateTimeFormatter.ISO_DATE_TIME);
  }

  public String getOnlyDateWithOffset(int daysOffset) {
    return OffsetDateTime
        .now()
        .withHour(0)
        .withMinute(0)
        .withSecond(0)
        .withNano(0)
        .plusDays(daysOffset)
        .atZoneSameInstant(ZoneOffset.UTC)
        .format(DateTimeFormatter.ISO_DATE_TIME);
  }

  @SneakyThrows
  public String template(String template, Map<String, String> parameters) {
    return StringSubstitutor.replace(template, parameters);
  }

  @SneakyThrows
  public String templateObject(String template, Map<String, Object> parameters) {
    return StringSubstitutor.replace(template, parameters);
  }
}
