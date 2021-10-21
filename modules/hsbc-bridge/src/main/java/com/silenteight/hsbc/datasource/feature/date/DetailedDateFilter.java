package com.silenteight.hsbc.datasource.feature.date;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@RequiredArgsConstructor
class DetailedDateFilter implements Predicate<String> {

  private static final Pattern PATTERN = Pattern.compile("[^0-9A-Za-z]");

  private static final Map<String, String> MONTHS = ofEntries(
      entry("jan", "01"),
      entry("feb", "02"),
      entry("mar", "03"),
      entry("apr", "04"),
      entry("may", "05"),
      entry("jun", "06"),
      entry("jul", "07"),
      entry("aug", "08"),
      entry("sep", "09"),
      entry("oct", "10"),
      entry("nov", "11"),
      entry("dec", "12")
  );

  @Getter
  private final Map<String, String> detailDates = new HashMap<>();

  private final List<String> dates;

  @Override
  public boolean test(String date) {
    return dates.stream().anyMatch(d -> isDetailed(date, d));
  }

  private boolean isDetailed(String date, String otherDate) {
    date = normalize(date);
    otherDate = normalize(otherDate);

    var isDetailed = date.length() > otherDate.length() && date.contains(otherDate);

    fillDetailDates(date, otherDate, isDetailed);

    return isDetailed;
  }

  private void fillDetailDates(String date, String otherDate, boolean isDetailed) {
    if (isDetailed) {
      detailDates.put(date, otherDate);
    }
  }

  private static String normalize(String date) {
    var normalizedDate = date;
    if (isNotBlank(date)) {
      normalizedDate = PATTERN.matcher(date.toLowerCase()).replaceAll("");
      for (var entry : MONTHS.entrySet()) {
        var result = normalizedDate.replaceFirst(entry.getKey(), entry.getValue());
        if (!normalizedDate.equals(result)) {
          return result;
        }
      }
    }
    return normalizedDate;
  }
}
