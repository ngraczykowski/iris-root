package com.silenteight.sens.webapp.keycloak.usermanagement.query.lastlogintime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.keycloak.representations.idm.EventRepresentation;

import java.time.OffsetDateTime;

import static java.util.Optional.ofNullable;

@Getter
class KeycloakLoginEvent {

  private final EventRepresentation representation;
  private final OffsetDateTime time;

  KeycloakLoginEvent(EventRepresentation representation, OffsetDateTime time) {
    this.representation = representation;
    this.time = time;
  }

  static KeycloakLoginEventBuilder builder() {
    return new KeycloakLoginEventBuilder();
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  static class KeycloakLoginEventBuilder {

    private String userId;
    private OffsetDateTime time;

    KeycloakLoginEventBuilder time(String time) {
      this.time = OffsetDateTime.parse(time);
      return this;
    }

    KeycloakLoginEventBuilder userId(String username) {
      this.userId = username;
      return this;
    }

    KeycloakLoginEvent build() {
      if (time == null)
        throw new IllegalArgumentException("Provide time");

      EventRepresentation eventRepresentation = new EventRepresentation();
      eventRepresentation.setTime(time.toInstant().toEpochMilli());

      ofNullable(userId).ifPresent(eventRepresentation::setUserId);

      return new KeycloakLoginEvent(eventRepresentation, time);
    }
  }
}
