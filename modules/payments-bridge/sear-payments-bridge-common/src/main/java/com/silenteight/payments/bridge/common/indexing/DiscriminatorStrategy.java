/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.payments.bridge.common.indexing;

import lombok.NonNull;

public interface DiscriminatorStrategy {
  String create(
      @NonNull String alertMessageId, @NonNull String systemId, @NonNull String messageId);
}
