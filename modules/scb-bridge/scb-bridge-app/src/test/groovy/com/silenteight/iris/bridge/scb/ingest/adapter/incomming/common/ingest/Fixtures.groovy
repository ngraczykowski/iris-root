/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.ingest

import static com.silenteight.iris.bridge.scb.ingest.domain.Fixtures.alert

class Fixtures {

  static recommendation(com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.model.alert.Alert alert) {
    def recommendation = new com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.recommendation.ScbRecommendation()
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
