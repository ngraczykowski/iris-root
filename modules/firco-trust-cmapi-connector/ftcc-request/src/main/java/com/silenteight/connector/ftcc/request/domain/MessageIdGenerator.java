package com.silenteight.connector.ftcc.request.domain;

import java.util.UUID;

import static java.util.UUID.randomUUID;

class MessageIdGenerator {

  UUID generate() {
    return randomUUID();
  }
}
