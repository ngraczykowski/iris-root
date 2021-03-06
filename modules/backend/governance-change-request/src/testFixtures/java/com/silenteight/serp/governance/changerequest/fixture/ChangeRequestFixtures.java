package com.silenteight.serp.governance.changerequest.fixture;

import com.silenteight.serp.governance.changerequest.domain.dto.ChangeRequestDto;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.stream.Stream;

import static com.silenteight.serp.governance.changerequest.domain.ChangeRequestState.APPROVED;
import static com.silenteight.serp.governance.changerequest.domain.ChangeRequestState.PENDING;
import static com.silenteight.serp.governance.changerequest.domain.DomainConstants.MAX_MODEL_NAME_LENGTH;
import static com.silenteight.serp.governance.changerequest.domain.DomainConstants.MIN_MODEL_NAME_LENGTH;
import static java.util.UUID.fromString;

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
  private static final String MODEL_NAME_WITH_MAX_LENGTH = "a".repeat(MAX_MODEL_NAME_LENGTH);
  private static final String MODEL_NAME_WITH_MIN_LENGTH = "a".repeat(MIN_MODEL_NAME_LENGTH);
  private static final String MODEL_NAME_THAT_EXCEEDED_MAX_LENGTH =
      "a".repeat(MAX_MODEL_NAME_LENGTH + 1);
  private static final String MODEL_NAME_SHORTER_THAN_MIN_LENGTH =
      "a".repeat(MIN_MODEL_NAME_LENGTH - 1);

  public static final ChangeRequestDto PENDING_CHANGE_REQUEST = ChangeRequestDto
      .builder()
      .id(CHANGE_REQUEST_ID)
      .modelName(MODEL_NAME)
      .createdBy(CREATED_BY)
      .createdAt(CREATED_AT)
      .creatorComment(CREATOR_COMMENT)
      .state(PENDING)
      .build();

  public static final ChangeRequestDto APPROVED_CHANGE_REQUEST = ChangeRequestDto
      .builder()
      .id(CHANGE_REQUEST_ID)
      .modelName(MODEL_NAME)
      .createdBy(CREATED_BY)
      .createdAt(CREATED_AT)
      .creatorComment(CREATOR_COMMENT)
      .state(APPROVED)
      .build();

  public static Stream<String> getModelNames() {
    return Stream.of(MODEL_NAME, MODEL_NAME_WITH_MIN_LENGTH, MODEL_NAME_WITH_MAX_LENGTH);
  }

  public static Stream<String> getIncorrectModelNames() {
    return Stream.of(MODEL_NAME_SHORTER_THAN_MIN_LENGTH, MODEL_NAME_THAT_EXCEEDED_MAX_LENGTH);
  }

  private static Stream<String> getIncorrectAttachments() {
    return Stream.of("files/this-is-no-uuid", "name/d293a102-85be-11ec-8c53-5f27abcfa50c");
  }

  public static Stream<String> getForbiddenCharsAsInput() {
    return Stream.of("###", "qwer#", "122*", "zxcv^");
  }
  public static Stream<String> getAllowedCharsAsInput() {
    return Stream.of("aghsd", "ASDfgf", "GHgbd456", "dsadf-?!","@+~&/=");
  }
}
