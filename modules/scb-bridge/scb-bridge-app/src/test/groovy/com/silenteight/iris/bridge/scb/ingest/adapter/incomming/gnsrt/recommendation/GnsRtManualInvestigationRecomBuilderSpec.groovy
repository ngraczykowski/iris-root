/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.recommendation

import com.silenteight.iris.bridge.scb.outputrecommendation.domain.model.Recommendations.RecommendedAction

import spock.lang.Specification

class GnsRtManualInvestigationRecomBuilderSpec extends Specification {

  static fixtures = new GnsRtFixtures()

  def 'should return manual investigation recommendation'() {
    given:
    def gnsRtRecommendationRequest = fixtures.gnsRtRecommendationRequest

    when:
    def recommendations =
        GnsRtManualInvestigationRecomBuilder
            .prepareManualInvestigationRecommendation(gnsRtRecommendationRequest);

    then:
    with(recommendations.recommendations().get(0)) {
      recommendedAction() == RecommendedAction.ACTION_INVESTIGATE
      recommendedComment() ==
          'S8 recommended action: Manual Investigation\n\nManual Investigation hits:'
    }
  }
}
