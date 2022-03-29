package com.silenteight.scb.ingest.adapter.incomming.gnsrt.recommendation

import com.silenteight.scb.ingest.adapter.incomming.common.util.InternalBatchIdGenerator
import com.silenteight.scb.outputrecommendation.domain.model.Recommendations
import com.silenteight.scb.outputrecommendation.domain.model.Recommendations.Recommendation

import reactor.test.StepVerifier
import spock.lang.Specification

import java.time.Duration
import java.util.concurrent.Executors
import java.util.concurrent.TimeoutException

class GnsRtInFlightRequestSpec extends Specification {

  def internalBatchId = InternalBatchIdGenerator.generate()

  def executor = Executors.newSingleThreadExecutor()

  def cleanup() {
    executor.shutdown()
  }

  def 'should Mono finish successfully and return Recommendations'() {
    given:
    def recommendations = recommendations()
    def request = new GnsRtInFlightRequest(internalBatchId)

    when:
    def mono = request.mono()
    executor.execute(() -> request.recommendationsReceived(recommendations))

    then:
    StepVerifier.create(mono)
        .expectNext(recommendations)
        .verifyComplete()
  }

  def 'should Mono fail as Batch has failed'() {
    given:
    def request = new GnsRtInFlightRequest(internalBatchId)

    when:
    def mono = request.mono()
    executor.execute(() -> request.batchFailed("some error"))

    then:
    StepVerifier.create(mono)
        .expectError(RuntimeException)
        .verify()
  }

  def 'should Mono fail as it timeouts'() {
    given:
    def request = new GnsRtInFlightRequest(internalBatchId)

    when:
    def mono = request.mono()
        .timeout(Duration.ofSeconds(1))

    then:
    StepVerifier.create(mono)
        .expectError(TimeoutException)
        .verify()
  }

  static recommendations() {
    Recommendations.builder()
        .recommendations([Recommendation.builder().build()])
        .build()
  }
}
