package com.silenteight.warehouse.report.remove;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.time.TimeSource;

import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;

@RequiredArgsConstructor
class ReportsRemovalService {

  @NonNull
  private final TimeSource timeSource;

  @NonNull
  private final Duration durationTime;

  @NonNull
  private final List<ReportsRemoval> reportsRemovalList;

  @Transactional
  public void removeReports() {
    OffsetDateTime dayToRemoveReports = calculateDayToRemoveReports(durationTime);

    reportsRemovalList.forEach(
        removal -> removal.removeOlderThan(dayToRemoveReports));
  }

  private OffsetDateTime calculateDayToRemoveReports(Duration durationTime) {
    OffsetDateTime currentTime = timeSource.offsetDateTime();
    return currentTime.minus(durationTime);
  }
}
