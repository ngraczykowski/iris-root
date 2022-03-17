package com.silenteight.scb.ingest.adapter.incomming.common.indicator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.of;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class RecordDetailsCleaner {

  static String clean(String record, char separator) {
    return of(record.split(String.valueOf(separator)))
        .map(String::trim)
        .collect(joining())
        .trim();
  }
}
