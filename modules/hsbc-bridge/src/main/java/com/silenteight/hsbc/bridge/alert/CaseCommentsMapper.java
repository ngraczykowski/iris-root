package com.silenteight.hsbc.bridge.alert;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.json.external.model.CaseComment;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

import static java.time.LocalDateTime.parse;
import static java.time.ZoneOffset.UTC;
import static java.util.Comparator.comparingLong;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@RequiredArgsConstructor
@Slf4j
class CaseCommentsMapper {

  private final DateTimeFormatter dateTimeFormatter;
  private static final String COMMENT_KEY = "lastCaseComment";
  private static final String DATE_KEY = "lastCaseCommentDateTime";

  Map<String, String> getLastCaseCommentWithDate(List<CaseComment> caseComments) {
    return caseComments.stream()
        .filter(c -> isNotEmpty(c.getCommentDateTime()))
        .sorted(comparingLong((k) -> toDate(k.getCommentDateTime())))
        .map(c -> Map.of(COMMENT_KEY, c.getCaseComment(), DATE_KEY, c.getCommentDateTime()))
        .filter(c -> isNotEmpty(c.get(COMMENT_KEY)))
        .findFirst()
        .orElse(Map.of(COMMENT_KEY, "", DATE_KEY, ""));
  }

  private long toDate(@NonNull String commentDateTime) {
    try {
      return parse(commentDateTime.toUpperCase(), dateTimeFormatter).toEpochSecond(UTC);
    } catch (DateTimeParseException ex) {
      log.error("Cannot parse case comment date = {}", commentDateTime, ex);
      return Long.MIN_VALUE;
    }
  }
}
