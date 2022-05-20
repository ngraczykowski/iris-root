package com.silenteight.adjudication.engine.app.scheduling;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.features.matchfeaturevalue.DeleteMatchFeatureValuesUseCase;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class DeleteMatchFeatureValueJob {

  private final DeleteMatchFeatureValuesUseCase deleteMatchFeatureValuesUseCase;

  private final List<String> features;

  @SchedulerLock(lockAtMostFor = "PT30M",
      name = "DeleteMatchFeatureValueJob.deleteMatchFeatureValues")
  @Scheduled(cron = "${ae.match-feature-value.not-cached.scheduler:* */10 * * * ?}")
  public void deleteMatchFeatureValues() {
    if (features != null && !features.isEmpty()) {
      deleteMatchFeatureValuesUseCase.deleteMatchFeatureValues(features);
    }
  }
}
