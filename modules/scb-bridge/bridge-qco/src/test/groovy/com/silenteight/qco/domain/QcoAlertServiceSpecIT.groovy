package com.silenteight.qco.domain

import com.silenteight.qco.ContainersSpecificationIT
import com.silenteight.qco.QcoDbConfiguration
import com.silenteight.qco.QcoIntegrationTestConfiguration
import com.silenteight.qco.adapter.outgoing.jpa.QcoOverriddenRecommendation
import com.silenteight.qco.adapter.outgoing.jpa.QcoOverriddenRecommendationJpaRepository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(value = [QcoIntegrationTestConfiguration.class, QcoDbConfiguration.class])
class QcoAlertServiceSpecIT extends ContainersSpecificationIT {

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
