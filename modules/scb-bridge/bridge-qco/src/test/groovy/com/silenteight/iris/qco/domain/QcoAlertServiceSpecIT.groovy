/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.qco.domain

import com.silenteight.iris.qco.adapter.outgoing.jpa.QcoOverriddenRecommendation
import com.silenteight.iris.qco.adapter.outgoing.jpa.QcoOverriddenRecommendationJpaRepository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(value = [com.silenteight.iris.qco.QcoIntegrationTestConfiguration.class, com
    .silenteight.iris.qco.QcoDbConfiguration.class])
class QcoAlertServiceSpecIT extends com.silenteight.iris.qco.ContainersSpecificationIT {

  @Autowired
  private QcoAlertService qcoAlertService;

  @Autowired
  private QcoOverriddenRecommendationJpaRepository repository

  def "ExtractAndProcessRecommendationAlert"() {
    given:
    def qcoRecommendationAlert = Fixtures.QCO_RECOMMENDATION_ALERT

    when:
    def result = qcoAlertService.extractAndProcessRecommendationAlert(qcoRecommendationAlert)

    then:
    result != null
    Thread.currentThread().join(1_000)
    def allResults = repository.findAll()
    allResults.size() == 1
    QcoOverriddenRecommendation savedEntity = allResults[0]
    savedEntity.getSourceSolution() == Fixtures.SOLUTION
    savedEntity.getTargetSolution() == Fixtures.QCO_SOLUTION
  }
}
