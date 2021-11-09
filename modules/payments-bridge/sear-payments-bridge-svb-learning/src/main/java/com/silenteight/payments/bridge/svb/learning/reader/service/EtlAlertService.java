package com.silenteight.payments.bridge.svb.learning.reader.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.svb.learning.reader.domain.*;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningAlert.LearningAlertBuilder;

import com.google.common.base.Preconditions;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(EtlAlertServiceProperties.class)
class EtlAlertService {

  private static final DateTimeFormatter DATE_FORMAT =
      DateTimeFormatter.ofPattern("[dd/MM/yyyy HH:mm:ss][dd/MM/yyyy H:mm:ss]");

  private final EtlMatchService etlMatchService;

  private final EtlAlertServiceProperties properties;

  LearningAlertBuilder fromCsvRows(List<LearningCsvRow> rows) {
    Preconditions.checkArgument(!rows.isEmpty(), "rows must not be empty");

    var firstRow = rows.get(0);

    return LearningAlert.builder()
        .alertId(firstRow.getFkcoVSystemId())
        .alertTime(getOffsetDateTime(firstRow.getFkcoDFilteredDatetime()))
        .systemId(firstRow.getFkcoVSystemId())
        .messageId(firstRow.getFkcoVMessageid())
        .analystDecision(makeAnalystDecision(rows))
        .matches(createMatches(rows));
  }

  private AnalystDecision makeAnalystDecision(List<LearningCsvRow> rows) {
    var firstRow = rows.get(0);
    var latestDecision = findLatestDecision(rows).orElse(firstRow);

    return AnalystDecision.builder()
        .status(latestDecision.getFkcoVStatusName())
        .comment(latestDecision.getFkcoVActionComment())
        .actionDateTime(getOffsetDateTime(latestDecision.getFkcoDActionDatetime()))
        .build();
  }

  private Optional<LearningCsvRow> findLatestDecision(List<LearningCsvRow> rows) {
    return rows.stream().max(comparing(r -> getOffsetDateTime(r.getFkcoDActionDatetime())));
  }

  private List<LearningMatch> createMatches(List<LearningCsvRow> rows) {
    var matches = new ArrayList<LearningMatch>();
    var matchRows = new HashMap<String, List<LearningCsvRow>>();

    for (var row : rows) {
      var matchId = row.getMatchId();
      if (matchRows.containsKey(matchId)) {
        matchRows.get(matchId).add(row);
        continue;
      }
      var list = new ArrayList<LearningCsvRow>();
      list.add(row);
      matchRows.put(matchId, list);
    }

    matchRows.forEach((id, mrows) -> {
      var comparableRows = mrows
          .stream()
          .map(mr -> ComparableRow
              .builder()
              .learningCsvRow(mr)
              .actionDateTime(getOffsetDateTime(mr.getFkcoDActionDatetime()))
              .build()).sorted().collect(toList());
      matches.add(etlMatchService.fromLearningRows(comparableRows.get(0).getLearningCsvRow()));
    });

    return matches;
  }

  @Nonnull
  private OffsetDateTime getOffsetDateTime(String time) {
    return LocalDateTime
        .parse(time, DATE_FORMAT)
        .atZone(ZoneId.of(properties.getTimeZone()))
        .toOffsetDateTime();
  }
}
