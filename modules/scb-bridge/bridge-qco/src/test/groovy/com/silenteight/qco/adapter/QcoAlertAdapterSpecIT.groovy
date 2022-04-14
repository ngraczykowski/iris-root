package com.silenteight.qco.adapter

import com.silenteight.qco.ContainersSpecificationIT
import com.silenteight.qco.QcoDbConfiguration
import com.silenteight.qco.QcoIntegrationTestConfiguration
import com.silenteight.qco.adapter.incoming.QcoAlertAdapter
import com.silenteight.qco.adapter.outgoing.jpa.QcoOverriddenRecommendation
import com.silenteight.qco.adapter.outgoing.jpa.QcoOverriddenRecommendationJpaRepository
import com.silenteight.qco.domain.model.QcoRecommendationAlert

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(value = [QcoIntegrationTestConfiguration.class, QcoDbConfiguration.class])
class QcoAlertAdapterSpecIT extends ContainersSpecificationIT {

  @Autowired
  private QcoAlertAdapter qcoAlertAdapter;

  @Autowired
  private QcoOverriddenRecommendationJpaRepository repository

  def "ExtractAndProcessRecommendationAlert"() {
    given:
    when:
    def result = qcoAlertAdapter.extractAndProcessRecommendationAlert(
        QcoRecommendationAlert.builder().build())

    then:
    result != null
    Thread.currentThread().join(1_000)
    def allResults = repository.findAll()
    allResults.size() == 1
    QcoOverriddenRecommendation savedEntity = allResults[0]
    savedEntity.getSourceSolution() == 'FALSE:POSITIVE'
    savedEntity.getTargetSolution() == 'Manual:Investigation'
  }
}
