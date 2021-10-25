package com.silenteight.payments.bridge.warehouse.index.model;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import com.silenteight.data.api.v2.Alert;
import com.silenteight.data.api.v2.Match;
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

  public AlertBuilder newBuilder() {
    return new AlertBuilder(createStructBuilder());
  }

  private ObjectMapperStructBuilder createStructBuilder() {
    var prototype = Struct.newBuilder()
        .putFields(ACCESS_PERMISSION_TAG_FIELD, Values.of(accessPermissionTag))
        .build();

    return new ObjectMapperStructBuilder(objectMapper, prototype);
  }

  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  public final class AlertBuilder {

    private final Alert.Builder messageBuilder = Alert.newBuilder();
    private final ObjectMapperStructBuilder payloadBuilder;

    public AlertBuilder setName(String value) {
      messageBuilder.setName(value);
      return this;
    }

    public AlertBuilder setDiscriminator(String value) {
      messageBuilder.setDiscriminator(value);
      return this;
    }

    public AlertBuilder addPayload(IndexPayload payload) {
      payloadBuilder.add(payload);
      return this;
    }

    public MatchBuilder newMatch() {
      return new MatchBuilder(this, new ObjectMapperStructBuilder(objectMapper));
    }

    void addMatch(Match match) {
      messageBuilder.addMatches(match);
    }

    public Alert build() {
      return messageBuilder
          .clone()
          .setAccessPermissionTag(accessPermissionTag)
          .setPayload(payloadBuilder.build())
          .build();
    }
  }

  @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
  public static final class MatchBuilder {

    @NonNull
    private final AlertBuilder parentBuilder;

    private final Match.Builder messageBuilder = Match.newBuilder();
    private final ObjectMapperStructBuilder payloadBuilder;

    public MatchBuilder setName(String value) {
      messageBuilder.setName(value);
      return this;
    }

    public MatchBuilder setDiscriminator(String value) {
      messageBuilder.setDiscriminator(value);
      return this;
    }

    public MatchBuilder addPayload(IndexPayload payload) {
      payloadBuilder.add(payload);
      return this;
    }

    public void finish() {
      var payload = payloadBuilder.build();

      parentBuilder.addMatch(messageBuilder
          .clone()
          .setPayload(payload)
          .build()
      );
    }
  }
}
