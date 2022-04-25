package com.silenteight.scb.ingest.adapter.incomming.common.ingest

import com.silenteight.scb.ingest.adapter.incomming.common.model.alert.Alert
import com.silenteight.scb.ingest.adapter.incomming.common.recommendation.ScbRecommendation

import static com.silenteight.scb.ingest.domain.Fixtures.alert

class Fixtures {

  static recommendation(Alert alert) {
    def recommendation = new ScbRecommendation()
    recommendation.systemId = alert.details().getSystemId()
    recommendation.discriminator = alert.id().discriminator()
    recommendation.alertName = 'alertName/' + alert.details().getSystemId()
    recommendation
  }

  static alerts() {
    [
        alert(1),
        alert(2)
    ]
  }
}
