/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.qco.domain

import com.silenteight.iris.qco.ContainersSpecificationIT
import com.silenteight.iris.qco.QcoDbConfiguration
import com.silenteight.iris.qco.QcoIntegrationTestConfiguration
import com.silenteight.iris.qco.adapter.outgoing.jpa.QcoOverriddenRecommendation
import com.silenteight.iris.qco.adapter.outgoing.jpa.QcoOverriddenRecommendationJpaRepository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.aspectj.AspectJAsyncConfiguration
import org.springframework.test.context.ActiveProfiles
import org.testcontainers.shaded.org.awaitility.Awaitility

@ActiveProfiles("test")
@DataJpaTest
@EnableAsync
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import(value = [QcoIntegrationTestConfiguration, QcoDbConfiguration, AspectJAsyncConfiguration])
class QcoAlertServiceSpecIT extends ContainersSpecificationIT {

  @Autowired
  private QcoAlertService qcoAlertService

  @Autowired
  private QcoOverriddenRecommendationJpaRepository repository

  def "ExtractAndProcessRecommendationAlert"() {
    given:
    def qcoRecommendationAlert = Fixtures.QCO_RECOMMENDATION_ALERT

    when:
    def result = qcoAlertService.extractAndProcessRecommendationAlert(qcoRecommendationAlert)

    then:
    result != null
    Awaitility.await().until(() -> repository.count() > 0)
    def allResults = repository.findAll()
    allResults.size() == 1
    QcoOverriddenRecommendation savedEntity = allResults[0]
    savedEntity.getSourceSolution() == Fixtures.SOLUTION
    savedEntity.getTargetSolution() == Fixtures.QCO_SOLUTION
  }
}
