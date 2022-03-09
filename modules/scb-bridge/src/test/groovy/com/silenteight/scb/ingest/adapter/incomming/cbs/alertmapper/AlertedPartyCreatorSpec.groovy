package com.silenteight.scb.ingest.adapter.incomming.cbs.alertmapper

import com.silenteight.scb.ingest.adapter.incomming.common.alertrecord.AlertRecord
import com.silenteight.scb.ingest.adapter.incomming.common.gnsparty.GnsParty
import com.silenteight.scb.ingest.adapter.incomming.common.gnsparty.RecordParser
import com.silenteight.proto.serp.scb.v1.ScbAlertedPartyDetails
import com.silenteight.proto.serp.v1.alert.Party.Source
import com.silenteight.sep.base.common.protocol.AnyUtils

import spock.lang.Specification

import static java.util.Optional.empty

class AlertedPartyCreatorSpec extends Specification {

  def objectUnderTest = new AlertedPartyCreator()

  def 'should create alerted party (group 5)'() {
    given:
    def alertRecord = createRecord('EBBS~722172921~I~MUHAMMED iammasked lalala~~~~~~~~~registOrResidentialAddress~registOrResidenAddCntry~mailingOrCommunicationAddr~mailingOrCommunAddrCntry~operatingOrOfficialAddress~operatingOrOffAddrCntry~otherAddress~otherAddressCountry~registeredAddOfHeadOffice~registeredAddCntryOfHo~registeredAddOfParentComp~regAddCntryParCompany~PAK~estCountryOfHeadOffice~estCountryOfParentCompany~OLD NIC NUMBER~722172921~NEW NIC NUMBER~3520114467193~Passport No~~~~Deferred Docs~~1977-02-12~PK~~~nameOfAuthority~PK~Salaried~AZZ~~IND~EB Premium Banking~15~ACTIVE~~2017-06-09~~~~CUSTOMER                      ~N~~~02A~~~~~~N~Active CASA~~F~~~~~~~~~~~~~~~')
    def gnsParty = makeGnsParty(alertRecord)

    when:
    def result = objectUnderTest.makeAlertedParty(alertRecord, gnsParty)

    then:
    result.source == Source.SOURCE_CONFIDENTIAL
    with(result.id) {
      sourceId == alertRecord.recordId
      discriminator == '30YQz1dm5IuNRZ-e2Zj02xtK00M='
    }
    def scbAlertedPartyDetails = AnyUtils
        .maybeUnpack(result.details, ScbAlertedPartyDetails.class)
        .get()
    scbAlertedPartyDetails.apId == alertRecord.systemId
    scbAlertedPartyDetails.apSrcSysId == gnsParty.sourceSystemIdentifier
    scbAlertedPartyDetails.custType == gnsParty.fields.clientType
    scbAlertedPartyDetails.apDobDoi == gnsParty.fields.dateOfBirthOrRegis
    scbAlertedPartyDetails.apDbCountry == gnsParty.fields.bookingLocation
    scbAlertedPartyDetails.apBookingLocation == gnsParty.fields.bookingLocation
    scbAlertedPartyDetails.apName == gnsParty.name.get()
    scbAlertedPartyDetails.apNameSynonymsList as Set == gnsParty.alternateNames
    scbAlertedPartyDetails.apResidentialAddressesList == gnsParty.fields.residentialAddresses
    scbAlertedPartyDetails.apResidentialAddressesList == ['registOrResidentialAddress',
                                                          'mailingOrCommunicationAddr',
                                                          'operatingOrOfficialAddress',
                                                          'otherAddress',
                                                          'registeredAddOfHeadOffice',
                                                          'registeredAddOfParentComp',
                                                          'nameOfAuthority']
  }

  def 'should create alerted party (group 2)'() {
    given:
    def alertRecord = createRecord('EBBS~722172921~I~MUHAMMED iammasked lalala~~~~~~~~~registOrResidentialAddress~registOrResidenAddCntry~mailingOrCommunicationAddr~mailingOrCommunAddrCntry~operatingOrOfficialAddress~operatingOrOffAddrCntry~otherAddress~otherAddressCountry~registeredAddOfHeadOffice~registeredAddCntryOfHo~registeredAddOfParentComp~regAddCntryParCompany~PAK~estCountryOfHeadOffice~estCountryOfParentCompany~OLD NIC NUMBER~722172921~NEW NIC NUMBER~3520114467193~Passport No~~~~Deferred Docs~~1977-02-12~PK~~~nameOfAuthority~PK~Salaried~AZZ~~IND~EB Premium Banking~15~ACTIVE~~2017-06-09~~~~CUSTOMER                      ~N~~~02A~~~~~~N~Active CASA~~F~')
    def gnsParty = makeGnsParty(alertRecord)

    when:
    def result = objectUnderTest.makeAlertedParty(alertRecord, gnsParty)

    then:
    result.source == Source.SOURCE_CONFIDENTIAL
    with(result.id) {
      sourceId == alertRecord.recordId
      discriminator == '30YQz1dm5IuNRZ-e2Zj02xtK00M='
    }
    def scbAlertedPartyDetails = AnyUtils
        .maybeUnpack(result.details, ScbAlertedPartyDetails.class)
        .get()
    scbAlertedPartyDetails.apId == alertRecord.systemId
    scbAlertedPartyDetails.apSrcSysId == gnsParty.sourceSystemIdentifier
    scbAlertedPartyDetails.custType == gnsParty.fields.clientType
    scbAlertedPartyDetails.apDobDoi == gnsParty.fields.dateOfBirthOrRegis
    scbAlertedPartyDetails.apDbCountry == gnsParty.fields.bookingLocation
    scbAlertedPartyDetails.apBookingLocation == gnsParty.fields.bookingLocation
    scbAlertedPartyDetails.apName == gnsParty.name.get()
    scbAlertedPartyDetails.apNameSynonymsList as Set == gnsParty.alternateNames
    scbAlertedPartyDetails.apResidentialAddressesList == gnsParty.fields.residentialAddresses
    scbAlertedPartyDetails.apResidentialAddressesList == ['registOrResidentialAddress',
                                                          'mailingOrCommunicationAddr',
                                                          'operatingOrOfficialAddress',
                                                          'otherAddress',
                                                          'registeredAddOfHeadOffice',
                                                          'registeredAddOfParentComp',
                                                          'nameOfAuthority']
  }

  def 'should create alerted party (group 1)'() {
    given:
    def alertRecord = createRecord('CCMS~HKCCNS600~I~1~~John Smith~~streetAddress~city~state~countryOfRegistration~countryOfIncorporation~countryOfOrigin~118296867~~19690926~~~~~~~HK~ACTIVE~HKCB~1~D693360~~~~~~~~')
    def gnsParty = makeGnsParty(alertRecord)

    when:
    def result = objectUnderTest.makeAlertedParty(alertRecord, gnsParty)

    then:
    result.source == Source.SOURCE_CONFIDENTIAL
    with(result.id) {
      sourceId == alertRecord.recordId
      discriminator == 'Ub-aE39fHqnBpSRkGJuIKemhn8k='
    }
    def scbAlertedPartyDetails = AnyUtils
        .maybeUnpack(result.details, ScbAlertedPartyDetails.class)
        .get()
    scbAlertedPartyDetails.apId == alertRecord.systemId
    scbAlertedPartyDetails.apSrcSysId == gnsParty.sourceSystemIdentifier
    scbAlertedPartyDetails.custType == gnsParty.fields.clientType
    scbAlertedPartyDetails.apDobDoi == gnsParty.fields.dateOfBirthOrRegis
    scbAlertedPartyDetails.apDbCountry == gnsParty.fields.bookingLocation
    scbAlertedPartyDetails.apBookingLocation == gnsParty.fields.bookingLocation
    scbAlertedPartyDetails.apName == gnsParty.name.get()
    scbAlertedPartyDetails.apNameSynonymsList as Set == gnsParty.alternateNames
    scbAlertedPartyDetails.apResidentialAddressesList == gnsParty.fields.residentialAddresses
    scbAlertedPartyDetails.apResidentialAddressesList ==   ['streetAddress',
                                                            'city',
                                                            'state']
  }

  def 'should apName be empty if gnsParty has no name'() {
    given:
    def alertRecord = createRecord('')
    def gnsParty = Spy(GnsParty.create('sourceSystemId', 'customerId'))
    gnsParty.name >> empty()

    when:
    def result = objectUnderTest.makeAlertedParty(alertRecord, gnsParty)

    then:
    def scbAlertedPartyDetails = AnyUtils
        .maybeUnpack(result.details, ScbAlertedPartyDetails.class)
        .get()
    scbAlertedPartyDetails.apName == ''
  }

  private static AlertRecord createRecord(String value) {
    return AlertRecord.builder()
        .systemId('systemId')
        .dbCountry('PL')
        .dbDob('2000-01-01')
        .fmtName('SCB_EDMP_DUED')
        .charSep('~' as char)
        .record(value)
        .recordId('abc')
        .typeOfRec('I')
        .build()
  }

  private static GnsParty makeGnsParty(AlertRecord alertRow) {
    return RecordParser.parse(
        alertRow.getSystemId(),
        alertRow.getCharSep(),
        alertRow.getFmtName(),
        alertRow.getRecord())
  }
}
