package com.silenteight.payments.bridge.warehouse.index.model;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.silenteight.data.api.v2.Alert;
import com.silenteight.payments.bridge.warehouse.index.model.payload.IndexPayload;
import com.silenteight.payments.common.protobuf.ObjectMapperStructBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.Struct;
import com.google.protobuf.util.Values;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class IndexedAlertBuilderFactory {

  public static final String ACCESS_PERMISSION_TAG_FIELD = "access_permission_tag";
  public static final String DEFAULT_ACCESS_PERMISSION_TAG = "US";

  @NonNull
  private final ObjectMapper objectMapper;

  @Setter
  private String accessPermissionTag = DEFAULT_ACCESS_PERMISSION_TAG;

  public Builder newBuilder() {
    return new Builder(createStructBuilder());
  }

  private ObjectMapperStructBuilder createStructBuilder() {
    var prototype = Struct.newBuilder()
        .putFields(ACCESS_PERMISSION_TAG_FIELD, Values.of(accessPermissionTag))
        .build();

    return new ObjectMapperStructBuilder(objectMapper, prototype);
  }

  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class Builder {

    private final Alert.Builder messageBuilder = Alert.newBuilder();
    private final ObjectMapperStructBuilder payloadBuilder;

    public Builder setName(String value) {
      messageBuilder.setName(value);
      return this;
    }

    public Builder setDiscriminator(String value) {
      messageBuilder.setDiscriminator(value);
      return this;
    }

    public Builder addPayload(IndexPayload payload) {
      payloadBuilder.add(payload);
      return this;
    }

    public Alert build() {
      var payload = payloadBuilder.build();

      var accessPermissionTag = payload.getFieldsOrDefault(
          ACCESS_PERMISSION_TAG_FIELD,
          Values.of(DEFAULT_ACCESS_PERMISSION_TAG)).getStringValue();

      return messageBuilder
          .clone()
          .setAccessPermissionTag(accessPermissionTag)
          .setPayload(payload)
          .build();
    }
  }
}
