/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.qco.adapter.incoming

import com.silenteight.proto.qco.api.v1.QcoRecommendation

import io.grpc.stub.StreamObserver
import spock.lang.Specification
import spock.lang.Subject

class QcoRecommendationGrpcServiceSpec extends Specification {

  def observer = Mock(StreamObserver)
  def qcoFacade = Mock(com.silenteight.iris.qco.QcoFacade)

  @Subject
  def underTest = new QcoRecommendationGrpcService(qcoFacade)

  def "should process and return qco recommendation"() {
    given:
    def qcoRecommendation = com.silenteight.iris.qco.domain.Fixtures.QCO_RECOMMENDATION_PROTO

    when:
    underTest.submitSolutionForQcoAnalysis(qcoRecommendation, observer)

    then:
    1 * qcoFacade.process(_) >> com.silenteight.iris.qco.domain.Fixtures.QCO_RECOMMENDATION_ALERT
    1 * observer.onNext(
        {QcoRecommendation qRecommendation ->
          qRecommendation.batchId == com.silenteight.iris.qco.domain.Fixtures.BATCH_ID
          qRecommendation.alertId == com.silenteight.iris.qco.domain.Fixtures.ALERT_ID
          with(qRecommendation.matchesList.first()) {
            matchId == com.silenteight.iris.qco.domain.Fixtures.MATCH_ID
            recommendation == com.silenteight.iris.qco.domain.Fixtures.SOLUTION
            comment == com.silenteight.iris.qco.domain.Fixtures.COMMENT
            qcoMarked == com.silenteight.iris.qco.domain.Fixtures.QCO_MARKED
          }
        })
    1 * observer.onCompleted()
  }
}
