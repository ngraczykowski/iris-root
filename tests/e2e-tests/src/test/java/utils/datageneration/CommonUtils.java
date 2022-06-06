package utils.datageneration;

import lombok.SneakyThrows;

import lombok.experimental.UtilityClass;

import org.apache.commons.text.StringSubstitutor;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Random;

@UtilityClass
public class CommonUtils {
  Random random = new Random();

  @SneakyThrows
  public static String getJsonTemplate(String jsonName) {
    return new String(Files.readAllBytes(
        Paths.get(String.format("src/test/resources/s8api/%s.json", jsonName))));
  }

  public static String getRandomValue(String... allowedValues) {
    if (allowedValues.length < 1)
      return "";

    int element = random.nextInt(allowedValues.length);
    return allowedValues[element];
  }

  public static String getDateTimeNow() {
    return OffsetDateTime
        .now()
        .atZoneSameInstant(ZoneOffset.UTC)
        .format(DateTimeFormatter.ISO_DATE_TIME);
  }

  public static String getOnlyDateWithOffset(int daysOffset) {
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

  public static String templateObjectOfName(String templateName, Map<String, Object> parameters) {
    return templateObject(getJsonTemplate(templateName), parameters);
  }

  @SneakyThrows
  public static String template(String template, Map<String, String> parameters) {
    return StringSubstitutor.replace(template, parameters);
  }

  @SneakyThrows
  public static String templateObject(String template, Map<String, Object> parameters) {
    return StringSubstitutor.replace(template, parameters);
  }
}
