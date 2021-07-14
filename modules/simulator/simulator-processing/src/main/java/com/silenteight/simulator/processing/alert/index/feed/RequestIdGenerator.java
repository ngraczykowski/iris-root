package com.silenteight.simulator.processing.alert.index.feed;

import static java.util.UUID.randomUUID;

class RequestIdGenerator {

  String generate() {
    return randomUUID().toString();
  }
}
