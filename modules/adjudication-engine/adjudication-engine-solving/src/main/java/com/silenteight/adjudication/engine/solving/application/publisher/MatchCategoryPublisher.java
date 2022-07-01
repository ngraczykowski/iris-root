/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.application.publisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.engine.solving.application.publisher.port.MatchCategoryPublisherPort;
import com.silenteight.adjudication.engine.solving.data.MatchCategoryDataAccess;
import com.silenteight.adjudication.engine.solving.domain.MatchCategory;

import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchCategoryPublisher implements MatchCategoryPublisherPort {

  private final TaskExecutor inMemorySolvingExecutor;
  private final MatchCategoryDataAccess matchCategoryDataAccess;

  @Override
  public void resolve(MatchCategory matchCategory) {
    log.debug("Store match category: {}", matchCategory);
    inMemorySolvingExecutor.execute(() -> matchCategoryDataAccess.store(matchCategory));
  }
}
