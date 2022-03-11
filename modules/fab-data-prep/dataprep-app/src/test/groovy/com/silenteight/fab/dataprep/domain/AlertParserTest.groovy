package com.silenteight.fab.dataprep.domain

import com.silenteight.proto.fab.api.v1.AlertMessageDetails
import com.silenteight.proto.fab.api.v1.AlertMessageStored

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
    def alertMessageDetails = AlertMessageDetails.newBuilder().setMessageName("alertId")
        .setPayload(Fixtures.MESSAGE).build();
    def alertMessageStored = AlertMessageStored.newBuilder().setBatchName("batchId")
        .setMessageName("alertId").build();

    when:
    def extractedAlert = underTest.parse(alertMessageStored, alertMessageDetails)

    then:
    extractedAlert.getParsedMessageData().getGender() == "M";
    extractedAlert.matches.size() == 1
    extractedAlert.matches.values().each  {it ->
      def parser = new JsonSlurper();
      assert parser.parseText(it.getPayload().toString()) == parser.parseText(Fixtures.HIT)
    }
  }

}
