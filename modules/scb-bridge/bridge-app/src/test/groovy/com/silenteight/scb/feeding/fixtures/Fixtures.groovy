package com.silenteight.scb.feeding.fixtures

import com.silenteight.scb.ingest.adapter.incomming.common.model.ObjectId
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert.Flag
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.AlertDetails
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.Match

class Fixtures {

  static def ALERT_ID = ObjectId.builder()
      .sourceId("source-id")
      .build();

  static def ALERT_DETAILS = AlertDetails.builder()
      .batchId("batch-id")
      .build();

  static def MATCH_ID = ObjectId.builder()
      .sourceId('matchId')
      .build()

  static def MATCH = Match
      .builder()
      .id(MATCH_ID)
      .build()

  static def LEARNING_ALERT = Alert.builder()
      .id(ALERT_ID)
      .details(ALERT_DETAILS)
      .matches(Collections.singletonList(MATCH))
      .flags(Flag.LEARN.value)
      .build();

  static def RECOMMENDATION_ALERT = Alert.builder()
      .id(ALERT_ID)
      .details(ALERT_DETAILS)
      .matches(Collections.singletonList(MATCH))
      .flags(Flag.RECOMMEND.value)
      .build();
}
