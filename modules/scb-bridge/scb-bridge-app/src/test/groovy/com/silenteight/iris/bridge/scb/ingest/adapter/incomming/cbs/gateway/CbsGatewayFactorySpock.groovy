/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.gateway

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.config.FetcherConfiguration

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
