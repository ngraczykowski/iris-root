package com.silenteight.hsbc.datasource.feature.date;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@RequiredArgsConstructor
class DetailedDateFilter implements Predicate<String> {

  private static final Pattern PATTERN = Pattern.compile("[^0-9A-Za-z]");

  private static final Map<String, String> MONTHS = Map.ofEntries(
      Map.entry("jan", "01"),
      Map.entry("feb", "02"),
      Map.entry("mar", "03"),
      Map.entry("apr", "04"),
      Map.entry("may", "05"),
      Map.entry("jun", "06"),
      Map.entry("jul", "07"),
      Map.entry("aug", "08"),
      Map.entry("sep", "09"),
      Map.entry("oct", "10"),
      Map.entry("nov", "11"),
      Map.entry("dec", "12")
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
    if (StringUtils.isNotBlank(date)) {
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
