package com.silenteight.payments.bridge.svb.learning.reader.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.svb.learning.reader.domain.AnalystDecision;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningAlert;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningAlert.LearningAlertBuilder;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningCsvRow;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;

import com.google.common.base.Preconditions;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;

import static java.util.Comparator.comparing;

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

    for (var row : rows) {
      var match = etlMatchService.fromLearningRows(row);
      if (matches.stream().noneMatch(m -> m.getMatchId().equals(match.getMatchId())))
        matches.add(match);
    }

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
