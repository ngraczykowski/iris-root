/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.recommendation

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.util.InternalBatchIdGenerator
import com.silenteight.iris.bridge.scb.outputrecommendation.domain.model.Recommendations
import com.silenteight.iris.bridge.scb.outputrecommendation.domain.model.Recommendations.Recommendation

import groovy.util.logging.Slf4j
import spock.lang.Specification
import spock.lang.Subject

@Slf4j
class GnsRtInFlightRequestsRegistrySpec extends Specification {

  @Subject
  def registry = new GnsRtInFlightRequestsRegistry()

  def internalBatchId = InternalBatchIdGenerator.generate()

  def inFlightRequest = Mock(GnsRtInFlightRequest)

  void setup() {
    inFlightRequest.getInternalBatchId() >> internalBatchId
  }

  def 'should not register twice the same GnsRtInFlightRequest'() {
    given:
    registry.register(inFlightRequest)

    when:
    registry.register(inFlightRequest)

    then:
    def e = thrown(IllegalStateException)
    e.message == "Can't put as key: ${internalBatchId} is already used"
  }

  def 'should register and unregister GnsRtInFlightRequest'() {
    when:
    registry.register(inFlightRequest)

    then:
    registry.pending.size() == 1

    when:
    registry.unregister(inFlightRequest)

    then:
    registry.pending.isEmpty()
  }

  def 'should finish gently when recommendationsReceived with unknown internalBatchId'() {
    when:
    registry.recommendationsReceived(internalBatchId, recommendations())

    then:
    0 * inFlightRequest.recommendationsReceived(_)
  }

  def 'should GnsRtInFlightRequest.recommendationsReceived be invoked on recommendationsReceived'() {
    given:
    def recommendations = recommendations()

    when:
    registry.register(inFlightRequest)
    registry.recommendationsReceived(internalBatchId, recommendations)

    then:
    1 * inFlightRequest.recommendationsReceived(recommendations)
  }

  def 'should finish gently when batchFailed with unknown batchId'() {
    when:
    registry.batchFailed(internalBatchId, "err")

    then:
    0 * inFlightRequest.batchFailed("err")
  }

  def 'should GnsRtInFlightRequest.batchFailed be invoked on batchFailed'() {
    when:
    registry.register(inFlightRequest)
    registry.batchFailed(internalBatchId, "err")

    then:
    1 * inFlightRequest.batchFailed("err")
  }

  static recommendations() {
    Recommendations.builder()
        .recommendations([Recommendation.builder().build()])
        .build()
  }
}
