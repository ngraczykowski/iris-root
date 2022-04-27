package com.silenteight.qco.adapter.incoming

import com.silenteight.proto.qco.api.v1.QcoRecommendation
import com.silenteight.qco.QcoFacade
import com.silenteight.qco.domain.Fixtures

import io.grpc.stub.StreamObserver
import spock.lang.Specification
import spock.lang.Subject

class QcoRecommendationGrpcServiceSpec extends Specification {

  def observer = Mock(StreamObserver)
  def qcoFacade = Mock(QcoFacade)

  @Subject
  def underTest = new QcoRecommendationGrpcService(qcoFacade)

  def "should process and return qco recommendation"() {
    given:
    def qcoRecommendation = Fixtures.QCO_RECOMMENDATION_PROTO

    when:
    underTest.submitSolutionForQcoAnalysis(qcoRecommendation, observer)

    then:
    1 * qcoFacade.process(_) >> Fixtures.QCO_RECOMMENDATION_ALERT
    1 * observer.onNext(
        {QcoRecommendation qRecommendation ->
          qRecommendation.batchId == Fixtures.BATCH_ID
          qRecommendation.alertId == Fixtures.ALERT_ID
          with(qRecommendation.matchesList.first()) {
            matchId == Fixtures.MATCH_ID
            recommendation == Fixtures.SOLUTION
            comment == Fixtures.COMMENT
            qcoMarked == Fixtures.QCO_MARKED
          }
        })
    1 * observer.onCompleted()
  }
}
