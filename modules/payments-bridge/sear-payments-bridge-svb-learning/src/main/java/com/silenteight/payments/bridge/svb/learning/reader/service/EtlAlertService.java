package com.silenteight.payments.bridge.svb.learning.reader.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningAlert;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningCsvRow;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;

import com.google.protobuf.Timestamp;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.silenteight.payments.bridge.common.protobuf.TimestampConverter.fromSqlTimestamp;

@Service
@RequiredArgsConstructor
class EtlAlertService {

  private final EtlMatchService etlMatchService;

  LearningAlert fromCsvRows(List<LearningCsvRow> rows, String batchStamp, String fileName) {
    return LearningAlert.builder()
        .alertId(rows.get(0).getFkcoVSystemId())
        .alertTime(createAlertTime(rows.get(0).getFkcoDFilteredDatetime()))
        .systemId(rows.get(0).getFkcoVSystemId())
        .messageId(rows.get(0).getFkcoVMessageid())
        .fircoAnalystComment(rows.get(0).getFkcoVActionComment())
        .fircoAnalystDecision(rows.get(0).getFkcoStatus())
        .fircoAnalystDecisionTime(rows.get(0).getFkcoDActionDatetime())
        .matches(createMatches(rows))
        .batchStamp(batchStamp)
        .fileName(fileName)
        .build();
  }

  private List<LearningMatch> createMatches(List<LearningCsvRow> rows) {
    List<LearningMatch> matches = new ArrayList<>();
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

  static Timestamp createAlertTime(String time) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
    Date parsedDate;
    try {
      parsedDate = dateFormat.parse(time);
    } catch (ParseException e) {
      throw new IllegalArgumentException(e);
    }
    var timestamp = new java.sql.Timestamp(parsedDate.getTime());
    return fromSqlTimestamp(timestamp);
  }

  private static class DuplicatedMatchIdException extends RuntimeException {

    private static final long serialVersionUID = 8806971682856315265L;

    DuplicatedMatchIdException() {
      super("Duplicated match id");
    }
  }
}
