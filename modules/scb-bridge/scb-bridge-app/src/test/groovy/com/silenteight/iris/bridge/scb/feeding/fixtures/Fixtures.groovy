/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.feeding.fixtures

class Fixtures {

  static def ALERT_ID = com
      .silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.ObjectId.builder()
      .sourceId("source-id")
      .build()

  static def ALERT_DETAILS = com
      .silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.alert.AlertDetails.builder()
      .batchId("batch-id")
      .systemId("systemId")
      .alertName("alertName")
      .build()

  static def MATCH_ID = com
      .silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.ObjectId.builder()
      .sourceId('matchId')
      .build()

  static def MATCH_DETAILS = com
      .silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.match.MatchDetails.builder()
      .matchName("matchName")
      .build()

  static def MATCH = com
      .silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.match.Match
      .builder()
      .details(MATCH_DETAILS)
      .id(MATCH_ID)
      .build()

  static def LEARNING_ALERT = com
      .silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.alert.Alert.builder()
      .id(ALERT_ID)
      .details(ALERT_DETAILS)
      .matches(Collections.singletonList(MATCH))
      .flags(com
          .silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.alert.Alert.Flag.LEARN.value)
      .build();

  static def RECOMMENDATION_ALERT = com
      .silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.alert.Alert.builder()
      .id(ALERT_ID)
      .details(ALERT_DETAILS)
      .matches(Collections.singletonList(MATCH))
      .flags(com
          .silenteight
          .iris.bridge.scb.ingest.adapter.incomming.common.model.alert.Alert.Flag.RECOMMEND.value)
      .build();
}
