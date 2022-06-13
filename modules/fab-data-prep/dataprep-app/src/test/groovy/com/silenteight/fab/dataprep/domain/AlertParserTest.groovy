package com.silenteight.fab.dataprep.domain

import com.silenteight.proto.fab.api.v1.AlertMessageDetails
import com.silenteight.proto.fab.api.v1.AlertMessageStored

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

import static com.silenteight.fab.dataprep.domain.Fixtures.*
import static com.silenteight.sep.base.common.support.jackson.JsonConversionHelper.INSTANCE
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals
import static org.skyscreamer.jsonassert.JSONCompareMode.STRICT

@ContextConfiguration(classes = ParserConfiguration)
class AlertParserTest extends Specification {

  @Autowired
  AlertParser underTest

  def "AlertDetails should be parsed"() {
    given:
    def alertMessageDetails = AlertMessageDetails.newBuilder().setMessageName(MESSAGE_NAME)
        .setPayload(MESSAGE).build()
    def alertMessageStored = AlertMessageStored.newBuilder().setBatchName(BATCH_NAME)
        .setMessageName(MESSAGE_NAME).build()

    when:
    def parsedAlertMessage = underTest.parse(alertMessageStored, alertMessageDetails)

    then:
    parsedAlertMessage.getParsedMessageData().getGender() == "M"
    parsedAlertMessage.getSystemId() == 'TRAINING!60C2ED1B-58A1D68E-0326AE78-A8C7CC79'
    parsedAlertMessage.getCurrentStatusName() == 'COMMHUB'
    parsedAlertMessage.getCurrentActionDateTime() == '20180827094707'
    parsedAlertMessage.getCurrentActionComment() == ''
    parsedAlertMessage.hits.size() == 1
    parsedAlertMessage.hits.values().each {it ->
      assert it.getHitName() == 'PSY0003'
      assertEquals(HIT, it.getPayloads().first().toString(), STRICT)
    }
  }

  def 'hits with same ofacId should be merged'() {
    given:
    def hit1 = INSTANCE.objectMapper().readTree(
        '''{
  "EntityText": "hit-1",
  "HittedEntity": {
    "ID": "PSY0003"
  },
  "OfficialReferences": [
    {
      "OfficialReference": {
        "Name": "OSFI_2006\\/02\\/09"
      }
    }
  ]
}''')
    def hit2 = INSTANCE.objectMapper().readTree(
        '''{
  "EntityText": "hit-2",
  "HittedEntity": {
    "ID": "PSY0001"
  },
  "OfficialReferences": [
    {
      "OfficialReference": {
        "Name": "OSFI_2006\\/02\\/09"
      }
    }
  ]
}''')
    def hit3 = INSTANCE.objectMapper().readTree(
        '''{
  "EntityText": "hit-3",
  "HittedEntity": {
    "ID": "PSY0003"
  },
  "OfficialReferences": [
    {
      "OfficialReference": {
        "Name": "OSFI_2022\\/02\\/09"
      }
    }
  ]
}''')
    def hit4 = INSTANCE.objectMapper().readTree('{"EntityText": "hit-4"}')
    def hit5 = INSTANCE.objectMapper().readTree('{"EntityText": "hit-5"}')
    def hit6 = INSTANCE.objectMapper().readTree(
        '''{
  "EntityText": "hit-6",
  "HittedEntity": {
    "ID": "PSY0003"
  },
  "OfficialReferences": [
    {
      "OfficialReference": {
        "Name": "OSFI_2021\\/02\\/09"
      }
    }
  ]
}''')

    def hits = [hit1, hit2, hit3, hit4, hit5, hit6]

    when:
    def result = underTest.mergeHits(hits).values()

    then:
    result as Set == [[hit2], [hit4], [hit5], [hit1, hit3, hit6]] as Set
  }
}
