package com.silenteight.adjudication.engine.solving.application.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.solving.application.process.port.CategoryResolveProcessPort;
import com.silenteight.adjudication.engine.solving.application.publisher.port.CategoryResolvePublisherPort;

import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
class CategoryResolvePublisher implements CategoryResolvePublisherPort {

  private final TaskExecutor inMemorySolvingExecutor;
  private final CategoryResolveProcessPort categoryResolveProcess;


  public void resolve(final Long alertId) {
    log.debug("Resolve category value for alert: {}", alertId);
    inMemorySolvingExecutor.execute(() -> categoryResolveProcess.resolveCategoryValues(alertId));
  }
}
