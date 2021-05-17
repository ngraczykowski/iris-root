package com.silenteight.serp.governance.changerequest.fixture;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.serp.governance.changerequest.domain.ChangeRequestState;
import com.silenteight.serp.governance.changerequest.domain.dto.ChangeRequestDto;

import java.time.OffsetDateTime;
import java.util.UUID;

import static java.util.UUID.fromString;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ChangeRequestFixtures {

  public static final UUID CHANGE_REQUEST_ID = fromString("d6fb8ae1-ab37-4622-935a-706ea6c53800");
  public static final String MODEL_NAME = "solving-models/b4708d8c-4832-6fde-8dc0-d17b4708d8ca";
  public static final String CREATED_BY = "asmith";
  public static final OffsetDateTime CREATED_AT = OffsetDateTime.now();
  public static final String CREATOR_COMMENT = "New Change Request";
  public static final String APPROVED_BY = "jdoe";
  public static final String APPROVER_COMMENT = "Change Request approved";
  public static final String REJECTED_BY = "pallen";
  public static final String REJECTOR_COMMENT = "Change Request rejected";
  public static final String CANCELLED_BY = "rtaylor";

  public static final ChangeRequestDto PENDING_CHANGE_REQUEST = ChangeRequestDto
      .builder()
      .id(CHANGE_REQUEST_ID)
      .modelName(MODEL_NAME)
      .createdBy(CREATED_BY)
      .createdAt(CREATED_AT)
      .creatorComment(CREATOR_COMMENT)
      .state(ChangeRequestState.PENDING)
      .build();
}
