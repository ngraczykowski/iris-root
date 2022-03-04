package com.silenteight.customerbridge.gnsrt.mapper

import com.silenteight.customerbridge.common.gender.Gender
import com.silenteight.customerbridge.gnsrt.model.request.ScreenableData
import com.silenteight.proto.serp.scb.v1.ScbAlertedPartyDetails
import com.silenteight.proto.serp.v1.alert.Party.Source

import spock.lang.Specification

class GnsRtAlertedPartyCreatorSpec extends Specification {

  def recordId = 'recordId'
  def data = new ScreenableData(
      amlCountry: 'SG',
      clientType: 'I',
      countryOfBirthOrRegistration: 'ID',
      customerIdentificationNo: 'customerIdentificationNo',
      customerStatus: 'Customer status',
      dateOfBirthOrRegistration: '1999-01-01',
      fullLegalName: 'mr John Smith',
      gender: 'M',
      nationalityAll: 'SG 1',
      registeredOrResidentialAddressCountry: 'AB',
      sourceSystemIdentifier: 'sourceSystemIdentifier',

      //national ids or passport fields
      identificationType1: 'PAN',
      identificationNumber1: 'AAAPZ1234C',
      identificationType2: 'NRIC',
      identificationNumber2: 'BAJPC4350M',
      identificationType3: 'taxpayer identification number',
      identificationNumber3: 'AAAPZ1234E',
      identificationType4: 'national identity card',
      identificationNumber4: 'AAAPZ1234F',
      identificationTypeRest: 'passport',
      identificationNumberRest: 'AAAPZ1234G',

      //name synonyms fields
      alternateName1: 'Dimebag Darrell',
      alternateName2: 'Phil Anselmo',
      alternateName3: 'Vinnie Paul',
      alternateNameRest: 'Rex Brown',
      fullTradingName: 'Rock Company',
      legalParentOrGroupName: 'Vulgar Display of Power',
      executiveManagementNames: 'Abbot Brothers',
      supplementaryCardName: 'Card Name',

      //residence synonyms fields
      mailingOrCommunicationAddressCountry: 'AC',
      operatingOrOfficialAddressCountry: 'AD',
      otherAddressCountry: 'AE',
      registeredAddressCountryOfHeadOffice: 'AF',
      registeredAddressCountryOfParentCompany: 'AG',
      establishmentCountryOfHO: 'AH',
      establishmentCountryOfParentCompany: 'AJ',

      //residential addresses
      registeredOrResidentialAddress: 'registeredOrResidentialAddress',
      mailingOrCommunicationAddress: 'mailingOrCommunicationAddress',
      operatingOrOfficialAddress: 'operatingOrOfficialAddress',
      otherAddress: 'otherAddress',
      registeredAddressOfHeadOffice: 'registeredAddressOfHeadOffice',
      registeredAddressOfParentCompany: 'registeredAddressOfParentCompany',
      nameOfAuthority: 'nameOfAuthority'
  )

  def 'should create gns alerted party'() {
    when:
    def result = GnsRtAlertedPartyCreator.createAlertedParty(data, recordId)

    then:
    result.source == Source.SOURCE_CONFIDENTIAL
    result.id.sourceId == recordId
    with(result.details.unpack(ScbAlertedPartyDetails.class)) {
      apId == data.customerIdentificationNo
      apDbCountry == data.amlCountry
      apBookingLocation == data.amlCountry
      apName == data.fullLegalName
      apDobDoi == data.dateOfBirthOrRegistration
      apNationality == data.nationalityAll
      apCustStatus == data.customerStatus
      apGender == data.gender
      apResidence == data.registeredOrResidentialAddressCountry
      custType == data.clientType
      apGenderFromName == Gender.MALE.name()

      apDocNationalIdsList.asList().containsAll(
          [
              data.identificationNumber1,
              data.identificationNumber2,
              data.identificationNumber3,
              data.identificationNumber4
          ])

      apNameSynonymsList.asList().containsAll(
          [
              data.alternateName1,
              data.alternateName2,
              data.alternateName3,
              data.alternateNameRest,
              data.fullTradingName,
              data.legalParentOrGroupName,
              data.executiveManagementNames,
              data.supplementaryCardName
          ])

      apDocPassportsList.asList().containsAll(
          [
              data.identificationNumberRest
          ])

      apNationalitySynonymsList.asList().containsAll(
          [
              data.countryOfBirthOrRegistration,
              data.nationalityAll
          ])

      apResidenceSynonymsList.asList().containsAll(
          [
              data.registeredOrResidentialAddressCountry,
              data.mailingOrCommunicationAddressCountry,
              data.operatingOrOfficialAddressCountry,
              data.otherAddressCountry,
              data.registeredAddressCountryOfHeadOffice,
              data.registeredAddressCountryOfParentCompany,
              data.establishmentCountryOfHO,
              data.establishmentCountryOfParentCompany
          ])

      apResidentialAddressesList.asList().containsAll(
          [
              data.registeredOrResidentialAddress,
              data.mailingOrCommunicationAddress,
              data.operatingOrOfficialAddress,
              data.otherAddress,
              data.registeredAddressOfHeadOffice,
              data.registeredAddressOfParentCompany,
              data.nameOfAuthority,
          ])

      apDocOthersList.asList().containsAll(
          [
              data.identificationNumber1,
              data.identificationNumber2,
              data.identificationNumber3,
              data.identificationNumber4,
              data.identificationNumberRest
          ])
    }
  }

  def 'should create correct gender based on name'() {
    given:
    def genderCaseData = new ScreenableData(
        customerIdentificationNo: 'customerIdentificationNo',
        clientType: 'I',
        fullLegalName: 'John Smith',
        alternateName1: 'mr John Smith',
    )

    when:
    def result = GnsRtAlertedPartyCreator.createAlertedParty(genderCaseData, recordId)

    then:
    result.id.sourceId == recordId
    with(result.details.unpack(ScbAlertedPartyDetails.class)) {
      apGenderFromName == Gender.MALE.name()
    }
  }
}
