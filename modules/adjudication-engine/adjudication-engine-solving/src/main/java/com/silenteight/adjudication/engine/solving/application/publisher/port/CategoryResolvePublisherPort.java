/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.application.publisher.port;

public interface CategoryResolvePublisherPort {

  void resolve(final Long alertId);
}
