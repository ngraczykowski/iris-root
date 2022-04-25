package com.silenteight.scb.reports.adapter.outgoing

import com.silenteight.data.api.v2.ProductionDataIndexRequest
import com.silenteight.scb.reports.ReportFixtures
import com.silenteight.scb.reports.infrastructure.amqp.ReportsOutgoingConfigurationProperties

import org.springframework.amqp.rabbit.core.RabbitTemplate
import spock.lang.Specification
import spock.lang.Subject

class ReportsSenderServiceAdapterSpec extends Specification {

  def exchangeName = 'some exchange name'
  def routingKey = 'some routing key'

  def rabbitTemplate = Mock(RabbitTemplate)
  def properties = new ReportsOutgoingConfigurationProperties(exchangeName, routingKey)

  @Subject
  def underTest = new ReportsSenderServiceAdapter(rabbitTemplate, properties)

  def 'should send reports to warehouse'() {
    when:
    underTest.send([ReportFixtures.REPORT_ONE])

    then:
    1 * rabbitTemplate.convertAndSend(
        exchangeName, routingKey, {ProductionDataIndexRequest it ->
      it.analysisName == ''
      it.alertsCount == 1
      it.alertsList == [ReportFixtures.WAREHOUSE_ALERT]
    })

  }
}
