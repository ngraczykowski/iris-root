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
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;

import static java.util.Comparator.comparing;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties(EtlAlertServiceProperties.class)
class EtlAlertService {

  private static final DateTimeFormatter DATE_FORMAT =
      DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

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
        .status(latestDecision.getFkcoStatus())
        .comment(latestDecision.getFkcoVActionComment())
        .actionDateTime(getOffsetDateTime(latestDecision.getFkcoDActionDatetime()))
        .build();
  }

  private Optional<LearningCsvRow> findLatestDecision(List<LearningCsvRow> rows) {
    return rows.stream().max(comparing(r -> getOffsetDateTime(r.getFkcoDActionDatetime())));
  }

  private List<LearningMatch> createMatches(List<LearningCsvRow> rows) {
    var matches = new ArrayList<LearningMatch>();
    var hits = new HashMap<String, List<LearningCsvRow>>();

    for (var row : rows) {
      var matchId = row.getFkcoVListFmmId();

      if (hits.containsKey(matchId)) {
        var list = hits.get(matchId);
        list.add(row);
        continue;
      }

      hits.put(matchId, new ArrayList<>(List.of(row)));
    }

    for (var key : hits.keySet()) {
      matches.add(etlMatchService.fromLearningRows(hits.get(key)));
    }

    assertNoDuplicateMatchIds(matches);

    return matches;
  }

  static void assertNoDuplicateMatchIds(List<LearningMatch> matches) {
    var ids = new ArrayList<String>();
    for (var match : matches) {
      if (ids.contains(match.getMatchId()))
        throw new DuplicatedMatchIdException();

      ids.add(match.getMatchId());
    }
  }

  @Nonnull
  private OffsetDateTime getOffsetDateTime(String time) {
    return LocalDateTime
        .parse(time, DATE_FORMAT)
        .atZone(ZoneId.of(properties.getTimeZone()))
        .toOffsetDateTime();
  }

  private static class DuplicatedMatchIdException extends RuntimeException {

    private static final long serialVersionUID = 8806971682856315265L;

    DuplicatedMatchIdException() {
      super("Duplicated Match ID");
    }
  }
}
