package com.silenteight.serp.governance.changerequest.domain;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class DomainConstants {

  public static final int MIN_MODEL_NAME_LENGTH = 3;
  public static final int MAX_MODEL_NAME_LENGTH = 64;
}
