package com.silenteight.payments.bridge.svb.learning.reader.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningAlert;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningCsvRow;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
class CreateAlertUseCase {

  private final CreateMatchUseCase createMatchUseCase;

  LearningAlert fromCsvRows(List<LearningCsvRow> rows) {
    return LearningAlert.builder()
        .alertId(Long.parseLong(rows.get(0).getFkcoId()))
        .matches(createMatches(rows))
        .build();
  }

  private List<LearningMatch> createMatches(List<LearningCsvRow> rows) {
    String currentWatchList = rows.get(0).getFkcoVListFmmId();
    List<LearningMatch> matches = new ArrayList<>();
    List<LearningCsvRow> matchRows = new ArrayList<>();

    for (var row : rows) {
      if (isSameWatchListEntity(currentWatchList, row) || matchRows.size() == 0) {
        matchRows.add(row);
        continue;
      }

      currentWatchList = row.getFkcoVListFmmId();
      matches.add(createMatchUseCase.fromLearningRows(matchRows));
      matchRows.clear();
      matchRows.add(row);
    }

    return matches;
  }

  private static boolean isSameWatchListEntity(String currentId, LearningCsvRow row) {
    return row.getFkcoVListFmmId().equals(currentId);
  }
}
