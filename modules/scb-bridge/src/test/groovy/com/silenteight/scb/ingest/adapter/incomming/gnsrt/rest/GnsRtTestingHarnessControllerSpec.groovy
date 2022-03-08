package com.silenteight.scb.ingest.adapter.incomming.gnsrt.rest

import com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.HitDetailsParser.ParserException
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.generator.GnsRtRequestGenerator
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.GnsRtAlertStatus
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.request.*
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.rest.GnsRtTestingHarnessController

import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import spock.lang.Specification

import java.time.LocalDateTime
import java.time.ZoneId

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup

class GnsRtTestingHarnessControllerSpec extends Specification {

  def gnsRtRequestGenerator = Mock(GnsRtRequestGenerator)
  def controller = new GnsRtTestingHarnessController(gnsRtRequestGenerator)
  MockMvc mockMvc = standaloneSetup(controller).build()
  def someGnsRtRequest = createGnsRtRequest()
  def someIdValue = 'someId'

  def 'should return 404 when systemId has not been found'() {
    when:
    def results = mockMvc.perform(get('/v1/gnsrt/system-id/' + someIdValue))

    then:
    1 * gnsRtRequestGenerator.generateBySystemId(someIdValue) >> {
      throw new EmptyResultDataAccessException(1)
    }
    results.andExpect(status().isNotFound())
  }

  def 'should return 404 when recordId has not been found'() {
    when:
    def results = mockMvc.perform(get('/v1/gnsrt/record-id/' + someIdValue))

    then:
    1 * gnsRtRequestGenerator.generateByRecordId(someIdValue) >> {
      throw new EmptyResultDataAccessException(1)
    }
    results.andExpect(status().isNotFound())
  }

  def 'should return 404 when ParserException has been thrown'() {
    when: 'generate by system-id'
    def results = mockMvc.perform(get('/v1/gnsrt/system-id/' + someIdValue))

    then:
    1 * gnsRtRequestGenerator.generateBySystemId(someIdValue) >> {
      throw new ParserException()
    }
    results.andExpect(status().isNotFound())

    when: 'generate by record-id'
    results = mockMvc.perform(get('/v1/gnsrt/record-id/' + someIdValue))

    then:
    1 * gnsRtRequestGenerator.generateByRecordId(someIdValue) >> {
      throw new ParserException()
    }
    results.andExpect(status().isNotFound())
  }

  def 'should return gnsRtRequest'() {
    when: 'search by systemId'
    def results = mockMvc.perform(get('/v1/gnsrt/system-id/' + someIdValue))

    then:
    1 * gnsRtRequestGenerator.generateBySystemId(someIdValue) >> someGnsRtRequest
    verifyResults(results)

    when: 'search by recordId'
    results = mockMvc.perform(get('/v1/gnsrt/record-id/' + someIdValue))

    then:
    1 * gnsRtRequestGenerator.generateByRecordId(someIdValue) >> someGnsRtRequest
    verifyResults(results)
  }

  def verifyResults(ResultActions results) {
    results.andExpect(status().isOk())
    results.andExpect(content().contentType(MediaType.APPLICATION_JSON))
    results.andExpect(
        content().string(
            '{"screenCustomerNameRes":{"screenCustomerNameResPayload":{"payloadFormat":"payloadFormat","payloadVersion":"1","screenCustomerNameResInfo":{"header":{"userBankID":"userBankId","screeningUniqueReference":"uniqueReference","sourceSystem":"CLICEN","watchlistTypeAll":"Y","watchlistTypeSanctions":null,"watchlistTypePEP":null,"watchlistTypeAM":null,"watchlistTypeDD":null,"countryCode":"BD","segment":"BUSINESS_BANKING","clientType":"I","partyType":"Customer","genericSearchFlag":"N","priorityScreeningFlag":"N","userDepartment":"EDMI","requestorOrGroupMailId":null,"emailNotificationRequiredOrNot":null,"responseType":null,"requestInTime":null,"requestOutTime":null},"screenableData":{"amlcountry":"BD","sourceSystemIdentifier":"CLICEN","customerIdentificationNo":"BD_CLICEN_1234","clientType":"I","fullLegalName":"Joe Doe","fullTradingName":null,"legalParentOrGroupName":null,"executiveManagementNames":null,"supplementaryCardName":null,"alternateName1":null,"alternateName2":null,"alternateName3":null,"alternateNameRest":null,"registeredOrResidentialAddress":null,"registeredOrResidentialAddressCountry":"BD","mailingOrCommunicationAddress":null,"mailingOrCommunicationAddressCountry":null,"operatingOrOfficialAddress":null,"operatingOrOfficialAddressCountry":null,"otherAddress":null,"otherAddressCountry":null,"registeredAddressOfHeadOffice":null,"registeredAddressCountryOfHeadOffice":null,"registeredAddressOfParentCompany":null,"registeredAddressCountryOfParentCompany":null,"nationalityAll":null,"establishmentCountryOfHO":null,"establishmentCountryOfParentCompany":null,"identificationType1":"Passport","identificationNumber1":"1234","identificationType2":null,"identificationNumber2":null,"identificationType3":null,"identificationNumber3":null,"identificationType4":null,"identificationNumber4":null,"identificationTypeRest":null,"identificationNumberRest":null,"dateOfBirthOrRegistration":"2017-01-21","countryOfBirthOrRegistration":null,"homeStateAuthorityOrGovtCountryName":null,"nameOfStockExchange":null,"nameOfAuthority":null,"businessNature":null,"clientLegalEntityType":null,"gender":null,"countryOfEmployment":null,"addressCity":null,"dateTimeStamp":"2019-02-18 15:43:11","supplementaryInformation1":null,"supplementaryInformation2":null,"supplementaryInformation3":null,"supplementaryInformation4":null,"supplementaryInformation5":null,"supplementaryInformation6":null,"supplementaryInformation7":null,"supplementaryInformation8":null,"supplementaryInformation9":null,"supplementaryInformation10":null,"partyType":"Customer","supplierType":null,"acctOrRelationshipOrProfileClosedDate":null,"lastCDDApprovedDate":null,"linkedCustomerIdentificationNo":null,"natureOfRelationshipWithHomeStateAuthority":null,"ownershipStatusOfClient":null,"relationshipToThePrimaryCardholder":null,"clientSegment":null,"clientSubSegment":null,"classesOfBeneficiary":null,"customerStatus":null,"staffAccountIdentifier":null,"relatedPartyType":null,"casaFlag":null,"priorityIndicator":null,"relianceFlgOrSuppTypOrKoreanName":null,"realTimeMessageIndicator":null,"changeOrPriorityIndicator":null,"alternateID1Description":null,"alternateID1":null,"alternateID2Description":null,"alternateID2":null,"nottobeused":null,"cddriskRating":null,"pepstatus":null,"rmcodeOfCDDOwner":null,"rmlocationOfCDDOwner":null},"immediateResponseData":{"immediateResponseTimestamp":"2020-04-30T00:00:00.000","overAllStatus":"POTENTIAL_MATCH","alerts":[{"watchlistType":"Sanctions","alertStatus":"POTENTIAL_MATCH","alertID":"BD_DDDD_DENY!12340123","hitList":[{"hitID":"WL00036110","hitDetails":"SGl0RGV0YWlscy0x"}]}]}}},"header":{"originationDetails":{"trackingId":"uniqueTrackingId"}}}}'))
  }

  def createGnsRtRequest() {
    def someDate = LocalDateTime.of(2020, 4, 30, 0, 0, 0)
        .atZone(ZoneId.of("Asia/Hong_Kong")).toInstant()

    new GnsRtRecommendationRequest(
        screenCustomerNameRes: new GnsRtScreenCustomerNameRes(
            screenCustomerNameResPayload: new GnsRtScreenCustomerNameResPayload(
                payloadFormat: 'payloadFormat',
                payloadVersion: '1',
                screenCustomerNameResInfo: new GnsRtScreenCustomerNameResInfo(
                    header: new GnsRtScreenCustomerNameResInfoHeader(
                        userBankID: 'userBankId',
                        screeningUniqueReference: 'uniqueReference',
                        sourceSystem: 'CLICEN',
                        countryCode: 'BD',
                        segment: 'BUSINESS_BANKING',
                        clientType: 'I',
                        partyType: 'Customer',
                        watchlistTypeAll: 'Y',
                        genericSearchFlag: 'N',
                        priorityScreeningFlag: 'N',
                        userDepartment: 'EDMI'
                    ),
                    screenableData: new ScreenableData(
                        amlCountry: 'BD',
                        sourceSystemIdentifier: 'CLICEN',
                        customerIdentificationNo: 'BD_CLICEN_1234',
                        clientType: 'I',
                        fullLegalName: 'Joe Doe',
                        registeredOrResidentialAddressCountry: 'BD',
                        identificationType1: 'Passport',
                        identificationNumber1: '1234',
                        dateOfBirthOrRegistration: '2017-01-21',
                        partyType: 'Customer',
                        dateTimeStamp: '2019-02-18 15:43:11'
                    ),
                    immediateResponseData: new ImmediateResponseData(
                        immediateResponseTimestamp: someDate,
                        overAllStatus: GnsRtAlertStatus.POTENTIAL_MATCH,
                        alerts: [
                            new GnsRtAlert(
                                watchlistType: "Sanctions",
                                alertStatus: GnsRtAlertStatus.POTENTIAL_MATCH,
                                alertId: 'BD_DDDD_DENY!12340123',
                                hitList: [
                                    new GnsRtHit(
                                        hitId: 'WL00036110',
                                        hitDetails: 'SGl0RGV0YWlscy0x'
                                    )
                                ]
                            )
                        ]
                    )
                )
            ),
            header: new GnsRtScreenCustomerNameResHeader(
                originationDetails: new GnsRtOriginationDetails(
                    trackingId: 'uniqueTrackingId'
                )
            )
        )
    )
  }
}
