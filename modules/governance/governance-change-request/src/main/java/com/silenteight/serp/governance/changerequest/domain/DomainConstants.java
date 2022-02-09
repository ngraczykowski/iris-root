package com.silenteight.serp.governance.changerequest.domain;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class DomainConstants {

  public static final int MIN_MODEL_NAME_LENGTH = 3;
  public static final int MAX_MODEL_NAME_LENGTH = 64;
  public static final String ATTACHMENTS_REGEXP =
      "^files/[0-9a-f]{8}-[0-9a-f]{4}-4[0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$";
  public static final String INVALID_ATTACHMENT_UUID_MSG = "should be in format files/uuid";
}
