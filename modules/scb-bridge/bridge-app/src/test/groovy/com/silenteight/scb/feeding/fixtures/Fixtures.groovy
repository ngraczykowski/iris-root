package com.silenteight.scb.feeding.fixtures

import com.silenteight.scb.ingest.adapter.incomming.common.model.ObjectId
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert.Flag
import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.AlertDetails
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.Match
import com.silenteight.scb.ingest.adapter.incomming.common.model.match.MatchDetails

class Fixtures {

  static def ALERT_ID = ObjectId.builder()
      .sourceId("source-id")
      .build()

  static def ALERT_DETAILS = AlertDetails.builder()
      .batchId("batch-id")
      .systemId("systemId")
      .alertName("alertName")
      .build()

  static def MATCH_ID = ObjectId.builder()
      .sourceId('matchId')
      .build()

  static def MATCH_DETAILS = MatchDetails.builder()
      .matchName("matchName")
      .build()

  static def MATCH = Match
      .builder()
      .details(MATCH_DETAILS)
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
