package com.silenteight.fab.dataprep.domain

import com.silenteight.proto.fab.api.v1.AlertDetails
import com.silenteight.proto.fab.api.v1.MessageAlertStored

import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import spock.lang.Specification

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class AlertParserTest extends Specification {

  @Autowired
  private AlertParser underTest;

  def "AlertDetails should be parsed"() {
    given:
    def alertDetails = AlertDetails.newBuilder().setAlertId("alertId")
        .setPayload(Fixtures.MESSAGE).build();
    def messageAlertStored = MessageAlertStored.newBuilder().setBatchId("batchId")
        .setAlertId("alertId").build();

    when:
    def extractedAlert = underTest.parse(messageAlertStored, alertDetails)

    then:
    extractedAlert.getParsedMessageData().getGender() == "M";
    extractedAlert.matches.size() == 1
    extractedAlert.matches.values().each  {it ->
      def parser = new JsonSlurper();
      assert parser.parseText(it.getPayload().toString()) == parser.parseText(Fixtures.HIT)
    }
  }

}
