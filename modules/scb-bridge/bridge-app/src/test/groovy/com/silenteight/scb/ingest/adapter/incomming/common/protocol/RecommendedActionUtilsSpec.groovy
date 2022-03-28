package com.silenteight.scb.ingest.adapter.incomming.common.protocol

import com.silenteight.scb.outputrecommendation.domain.model.Recommendations.RecommendedAction

import spock.lang.Specification

class RecommendedActionUtilsSpec extends Specification {

  def 'should extract value for all possible enum values'() {

    expect:
    RecommendedActionUtils.nameWithoutPrefix(action as RecommendedAction).length() > 1

    where:
    action << RecommendedAction.values()

  }
}
