package com.silenteight.hsbc.datasource.feature.dob;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

interface Extractor {

  DobDeviationsFilter DOB_DEVIATIONS_FILTER = new DobDeviationsFilter();
  DobDateFilter DOB_DATE_FILTER = new DobDateFilter();
  DateExtractor DATE_EXTRACTOR = new DateExtractor();

  default Stream<String> result(Stream<String>... streams) {
    var mpDobs = collectDobs(streams);
    var result = getDetailedDateResult(mpDobs);

    return result.isEmpty() ? mpDobs.stream() : result.stream();
  }

  private static List<String> getDetailedDateResult(List<String> mpDobs) {
    return mpDobs.stream()
        .filter(new DetailedDateFilter(mpDobs))
        .distinct()
        .collect(toList());
  }

  @SafeVarargs
  private static List<String> collectDobs(Stream<String>... streams) {
    return ((streams.length == 2) ? Stream.concat(streams[0], streams[1]) : streams[0])
        .filter(DOB_DEVIATIONS_FILTER)
        .filter(DOB_DATE_FILTER)
        .map(DATE_EXTRACTOR)
        .distinct()
        .collect(toList());
  }
}
