/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.application.publisher.port;

import com.silenteight.adjudication.engine.solving.domain.MatchCategory;

public interface MatchCategoryPublisherPort {
  void resolve(MatchCategory matchCategory);
}
