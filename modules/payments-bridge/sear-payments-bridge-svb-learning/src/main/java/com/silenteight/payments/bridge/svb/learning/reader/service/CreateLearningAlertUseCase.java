package com.silenteight.payments.bridge.svb.learning.reader.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningAlert;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningCsvRow;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
class CreateLearningAlertUseCase {

  private final CreateLearningMatchUseCase createLearningMatchUseCase;

  LearningAlert fromCsvRows(List<LearningCsvRow> rows) {
    return LearningAlert.builder()
        .alertId(rows.get(0).getFkcoVSystemId())
        .matches(createMatches(rows))
        .build();
  }

  private List<LearningMatch> createMatches(List<LearningCsvRow> rows) {
    List<LearningMatch> matches = new ArrayList<>();
    var matchRows = new HashMap<String, List<LearningCsvRow>>();

    for (var row : rows) {
      var matchId = row.getFkcoVListFmmId();

      if (matchRows.containsKey(matchId)) {
        var list = matchRows.get(matchId);
        list.add(row);
        continue;
      }

      matchRows.put(matchId, new ArrayList<>(List.of(row)));
    }

    for (var key : matchRows.keySet()) {
      matches.add(createLearningMatchUseCase.fromLearningRows(matchRows.get(key)));
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

  private static class DuplicatedMatchIdException extends RuntimeException {

    private static final long serialVersionUID = 8806971682856315265L;

    DuplicatedMatchIdException() {
      super("Duplicated match id");
    }
  }
}
