package com.silenteight.serp.governance.changerequest.fixture;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

import static java.util.UUID.fromString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ChangeRequestFixtures {

  public static final UUID CHANGE_REQUEST_ID = fromString("d6fb8ae1-ab37-4622-935a-706ea6c53800");
  public static final String MODEL_NAME = "solving-models/b4708d8c-4832-6fde-8dc0-d17b4708d8ca";
  public static final String CREATED_BY = "asmith";
  public static final String CREATOR_COMMENT = "This is creator comment";
}
