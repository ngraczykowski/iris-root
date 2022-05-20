package com.silenteight.hsbc.datasource.feature.date;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

interface Extractor {

  DateDeviationsFilter DATE_DEVIATIONS_FILTER = new DateDeviationsFilter();
  DateFilter DATE_FILTER = new DateFilter();
  DateExtractor DATE_EXTRACTOR = new DateExtractor();

  default Stream<String> result(Stream<String>... streams) {
    var mpDobs = collectDobs(streams);
    var detailedDates = getDetailedDateResult(mpDobs);

    return mpDobs.stream()
        .filter(date -> detailedDates.values().stream()
            .noneMatch(date::equals));
  }

  private static Map<String, String> getDetailedDateResult(List<String> mpDobs) {
    var detailedDateFilter = new DetailedDateFilter(mpDobs);
    mpDobs.forEach(detailedDateFilter::test);
    return detailedDateFilter.getDetailDates();
  }

  @SafeVarargs
  private static List<String> collectDobs(Stream<String>... streams) {
    return ((streams.length == 2) ? Stream.concat(streams[0], streams[1]) : streams[0])
        .filter(DATE_DEVIATIONS_FILTER)
        .filter(DATE_FILTER)
        .map(DATE_EXTRACTOR)
        .distinct()
        .collect(Collectors.toList());
  }
}
