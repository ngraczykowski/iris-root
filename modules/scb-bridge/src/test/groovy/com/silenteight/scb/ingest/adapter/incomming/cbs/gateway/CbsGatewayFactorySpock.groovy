package com.silenteight.scb.ingest.adapter.incomming.cbs.gateway

import com.silenteight.scb.ingest.adapter.incomming.common.config.FetcherConfiguration

import spock.lang.Specification

class CbsGatewayFactorySpock extends Specification {

  def someQueryTimeout = 300

  def "should use correct cbsHitDetailsHelperFetcher implementation"() {
    given:
    def configuration = new FetcherConfiguration(viewName, someQueryTimeout)

    when:
    def fetcher = CbsGatewayFactory.getHitDetailsHelperFetcher(configuration)

    then:
    expectedImplClass in fetcher.getClass()

    where:
    viewName | expectedImplClass
    null     | EmptyCbsHitDetailsHelperFetcher.class
    ''       | EmptyCbsHitDetailsHelperFetcher.class
    'test'   | HitDetailsHelperFetcher.class
  }
}
