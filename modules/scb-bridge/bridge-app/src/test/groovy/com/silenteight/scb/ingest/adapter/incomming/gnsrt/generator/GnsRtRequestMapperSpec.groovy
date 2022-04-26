package com.silenteight.scb.ingest.adapter.incomming.gnsrt.generator

import com.silenteight.scb.ingest.adapter.incomming.common.alertrecord.AlertRecord
import com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.HitDetailsParser
import com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.HitDetailsParser.ParserException
import com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.model.HitDetails
import com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.model.Suspect
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.generator.GnsRtRequestMapper

import spock.lang.Specification

import static com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.GnsRtAlertStatus.POTENTIAL_MATCH
import static java.lang.String.valueOf

class GnsRtRequestMapperSpec extends Specification {

  def hitDetailsParser = Mock(HitDetailsParser)
  def objectUnderTest = new GnsRtRequestMapper(hitDetailsParser)

  def someSuspect = new Suspect(ofacId: '1')
  def someHitDetails = Mock(HitDetails) {
    extractUniqueSuspects() >> {[someSuspect]}
  }

  def 'should throw IllegalArgumentException when no alert records'() {
    when:
    objectUnderTest.map([])

    then:
    thrown(IllegalArgumentException)
  }

  def 'should throw ParserException when hit details cannot be parsed'() {
    given:
    def someIncorrectHitDetails = '?'
    def alertRecord = AlertRecord.builder()
        .details(someIncorrectHitDetails)
        .systemId('systemId')
        .build()

    when:
    objectUnderTest.map([alertRecord])

    then:
    1 * hitDetailsParser.parse(someIncorrectHitDetails) >> {
      throw new ParserException()
    }
    thrown(ParserException)
  }

  def 'should map to gnsRt request'() {
    given:
    def alertRecord = AlertRecord.builder()
        .batchId('batchId')
        .charSep((char) '~')
        .dbAccount('dbAccount')
        .dbCountry('dbCountry')
        .details(
            '''Suspect(s) detected by OFAC-Agent:3
SystemId: BD_SCIC_DENY!9BE06FFA-0058416D-929B432D-FB8BD10B
Associate: 
=============================
Suspect detected #1

OFAC ID:1

=============================''')
        .filteredString('2019/01/15 20:08:26')
        .fmtName('SCB-ADV')
        .record(
            '''(CLOB) CCMS~HKCCNS600~I~1~~John Smith~~75 W Mondamin St, Minooka, IL 60447, USA
        ~~~~~~118296867~~19690926~~~~~~~HK~ACTIVE~HKCB~1~D693360~~~~~~~~''')
        .systemId('systemId')
        .typeOfRec('I')
        .build()

    when:
    def result = objectUnderTest.map([alertRecord])

    then:
    1 * hitDetailsParser.parse(alertRecord.details) >> someHitDetails

    def screenCustomerNameResInfo = result
        .screenCustomerNameRes.screenCustomerNameResPayload.screenCustomerNameResInfo

    def originationDetails = result.screenCustomerNameRes.header.originationDetails

    with(screenCustomerNameResInfo.header) {
      clientType == alertRecord.typeOfRec
      countryCode == alertRecord.dbCountry
      sourceSystem == 'GNSRT'
      watchlistTypeAll == 'Y'
      userBankID == alertRecord.dbAccount
    }

    with(originationDetails) {
      trackingId == alertRecord.batchId
    }

    with(screenCustomerNameResInfo.immediateResponseData) {
      valueOf(immediateResponseTimestamp) == '2019-01-15T12:08:26Z'
      overAllStatus == POTENTIAL_MATCH
      alerts.size() == 1
      with(alerts.first()) {
        alertId == alertRecord.systemId
        alertStatus == POTENTIAL_MATCH
        watchlistType == "AM"
        hitList.size() == 1
        with(hitList.first()) {
          hitId == someSuspect.ofacId
          hitDetails == 'T0ZBQyBJRDoxCg=='
        }
      }
    }

    with(screenCustomerNameResInfo.screenableData) {
      amlCountry == alertRecord.dbCountry
      clientType == alertRecord.typeOfRec
      customerIdentificationNo == 'HKCCNS600'
      sourceSystemIdentifier == 'ECDDP'
      fullLegalName == 'John Smith'
      dateOfBirthOrRegistration == '19690926'
      registeredOrResidentialAddress == '75 W Mondamin St, Minooka, IL 60447, USA'
    }
  }
}
