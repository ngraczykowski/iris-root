package com.silenteight.warehouse.indexer.query.streaming;

import lombok.RequiredArgsConstructor;

import org.elasticsearch.search.SearchHit;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import static com.silenteight.commons.CSVUtils.getCSVRecordWithDefaultDelimiter;
import static java.util.Arrays.stream;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.StringUtils.EMPTY;

@RequiredArgsConstructor
class DataResponseParser {

  String parseLabels(List<String> fieldNames) {
    return getCSVRecordWithDefaultDelimiter(fieldNames.toArray(String[]::new));
  }

  List<String> parseValues(SearchHit[] searchHits, List<String> fieldNames) {
    return stream(searchHits)
        .map(SearchHit::getSourceAsMap)
        .map(DataResponseParser::parse)
        .filter(not(Map::isEmpty))
        .map(row -> getLine(row, fieldNames))
        .collect(toList());
  }

  private static Map<String, String> parse(Map<String, Object> hit) {
    return hit
        .entrySet()
        .stream()
        .collect(toMap(Entry::getKey, v -> Objects.toString(v.getValue(), EMPTY)));
  }

  private String getLine(Map<String, String> row, List<String> fieldNames) {
    String[] line = fieldNames
        .stream()
        .map(row::get)
        .toArray(String[]::new);

    return getCSVRecordWithDefaultDelimiter(line);
  }
}
