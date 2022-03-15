package com.silenteight.fab.dataprep.domain

import com.silenteight.proto.fab.api.v1.AlertMessageDetails
import com.silenteight.proto.fab.api.v1.AlertMessageStored

import groovy.json.JsonSlurper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

import static com.silenteight.fab.dataprep.domain.Fixtures.*

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@ActiveProfiles("dev")
class AlertParserTest extends Specification {

  @Autowired
  private AlertParser underTest;

  def "AlertDetails should be parsed"() {
    given:
    def alertMessageDetails = AlertMessageDetails.newBuilder().setMessageName(MESSAGE_NAME)
        .setPayload(MESSAGE).build();
    def alertMessageStored = AlertMessageStored.newBuilder().setBatchName(BATCH_NAME)
        .setMessageName(MESSAGE_NAME).build();

    when:
    def parsedAlertMessage = underTest.parse(alertMessageStored, alertMessageDetails)

    then:
    parsedAlertMessage.getParsedMessageData().getGender() == "M";
    parsedAlertMessage.getSystemId() == 'TRAINING!60C2ED1B-58A1D68E-0326AE78-A8C7CC79'
    parsedAlertMessage.hits.size() == 1
    parsedAlertMessage.hits.values().each  {it ->
      def parser = new JsonSlurper();
      assert parser.parseText(it.getPayload().toString()) == parser.parseText(HIT)
    }
  }

}
