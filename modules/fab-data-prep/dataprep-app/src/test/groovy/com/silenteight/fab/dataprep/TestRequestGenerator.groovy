package com.silenteight.fab.dataprep

import com.silenteight.fab.dataprep.domain.model.ParsedMessageData
import com.silenteight.fab.dataprep.domain.model.ParsedMessageData.ParsedMessageDataBuilder

import com.google.common.io.Resources
import com.jayway.jsonpath.DocumentContext
import com.jayway.jsonpath.JsonPath
import com.jayway.jsonpath.internal.JsonFormatter

import java.util.stream.Collectors

class TestRequestGenerator {

  static ParsedMessageDataBuilder getParsedMessageData() {
    ParsedMessageData.builder()
        .salutation('MR')
        .name('OSAMA BIN LADIN')
        .shortName('BIN LADIN')
        .customerType('Individual')
        .dob('30/8/1965')
        .dateOfEstablishment('10/20/2022')
        .gender('M')
        .swiftBic('')
        .address1('ADDAX TOWER')
        .address2('')
        .city('ABU DHABI')
        .state('ABU DHABI')
        .country('IR')
        .countryOfIncorporation('IR')
        .countryOfDomicile('IR')
        .countryOfBirth('IR')
        .customerSegment('WEALTH')
        .profession('LAWYER')
        .passportNum('AVB2833444')
        .nationalId('S93849384A')
        .tradeLicPlaceOfIssue('10/20/2020')
        .groupOrCompanyName('FIRST ABU DHABI BANK')
        .source('FAB_MOB')
        .sourceSystemId('')
        .customerNumber('ABC1234567')
        .alternate('')
        .latestCustomerNumber('ABC1234567')
        .lastUpdateTime('')
  }

  static ParsedMessageDataBuilder getEmptyParsedMessageData() {
    ParsedMessageData.builder()
        .salutation('')
        .name('')
        .shortName('')
        .customerType('')
        .dob('')
        .dateOfEstablishment('')
        .gender('')
        .swiftBic('')
        .address1('')
        .address2('')
        .city('')
        .state('')
        .country('')
        .countryOfIncorporation('')
        .countryOfDomicile('')
        .countryOfBirth('')
        .customerSegment('')
        .profession('')
        .passportNum('')
        .nationalId('')
        .tradeLicPlaceOfIssue('')
        .groupOrCompanyName('')
        .source('')
        .sourceSystemId('')
        .customerNumber('')
        .alternate('')
        .latestCustomerNumber('')
        .lastUpdateTime('')
  }

  static def getMessageData(ParsedMessageData data) {
    [data.salutation,
     data.name,
     data.shortName,
     data.customerType,
     data.dob,
     data.dateOfEstablishment,
     data.gender,
     data.swiftBic,
     data.address1,
     data.address2,
     data.city,
     data.state,
     data.country,
     data.countryOfIncorporation,
     data.countryOfDomicile,
     data.countryOfBirth,
     data.customerSegment,
     data.profession,
     data.passportNum,
     data.nationalId,
     data.tradeLicPlaceOfIssue,
     data.groupOrCompanyName,
     data.source,
     data.sourceSystemId,
     data.customerNumber,
     data.alternate,
     data.latestCustomerNumber,
     data.lastUpdateTime].stream()
        .collect(Collectors.joining(';'))
  }

  static def getRawMessage(ParsedMessageData data) {
    def message = JsonPath.parse(Resources.getResource('message.json').text)
    message.set('$.Message.Hits[0].Hit.HittedEntity.ID', UUID.randomUUID().toString())
    //random ofac_id
    message.set('$.Message.MessageData', getMessageData(data))
    message
  }

  static def addMessages(DocumentContext request, List<DocumentContext> messages) {
    messages.each {
      request.add('$.Body.msg_SendMessage.Messages', it.json())
    }
  }

  static def getRawRequest() {
    JsonPath.parse(Resources.getResource('raw-request.json').text)
  }

  static def requestSimple() {
    def request = getRawRequest()

    def message0 = getRawMessage(getParsedMessageData().build())

    def message1 = getRawMessage(getParsedMessageData().build())
    message1.set('$.Message.Hits[0].Hit.HittedEntity.AdditionalInfo', '12345667890')
    message1.set('$.Message.SystemID', 'SAN!60C2ED1B-58A1D68E-0326AE78-A8C7CC79')

    def message2 = getRawMessage(
        getParsedMessageData()
            .name('')
            .shortName('')
            .dob('')
            .customerType('Corporate')
            .swiftBic('123')
            .build())
    message2.set('$.Message.Hits[0].Hit.MatchingText', '')
    message2.set('$.Message.Hits[0].Hit.HittedEntity.Names', [])

    def message3 = getRawMessage(
        getParsedMessageData()
            .address1('')
            .address2('')
            .country('')
            .city('')
            .nationalId('')
            .build())
    message3.set('$.Message.Hits[0].Hit.MatchingText', '')
    message3.set('$.Message.Hits[0].Hit.HittedEntity.Names[0].Name', 'BIN LADIN')
    message3.delete('$.Message.Hits[0].Hit.HittedEntity.Names[1:]')
    message3.set('$.Message.Hits[0].Hit.HittedEntity.Addresses', [])

    def message4 = getRawMessage(
        getParsedMessageData()
            .dob('30/8/1965')
            .customerType('Corporate')
            .swiftBic('123')
            .build())
    message4.set('$.Message.Hits[0].Hit.HittedEntity.Names', [])
    message4.set('$.Message.Hits[0].Hit.HittedEntity.Addresses', [])
    message4.set('$.Message.Hits[0].Hit.HittedEntity.DatesOfBirth[0].DateOfBirth', '30-08-1965')

    def message5 = getRawMessage(
        getParsedMessageData()
            .country('PL')
            .build())
    message5.set('$.Message.Hits[0].Hit.HittedEntity.Names', [])
    message5.add('$.Message.Hits[0].Hit.HittedEntity.Addresses[0].Address.Countries', 'pl')

    def message6 = getRawMessage(
        getParsedMessageData()
            .customerType('unknown??')
            .build())
    message6.set('$.Message.Hits[0].Hit.HittedEntity.Type', 'unknown??')

    def message7 = getRawMessage(
        getParsedMessageData()
            .build())
    message7.add('$.Message.Hits', message7.read('$.Message.Hits[0]'))

    def message8 = getRawMessage(
        getParsedMessageData()
            .country('pl')
            .build())
    message8.add(
        '$.Message.Hits[0].Hit.HittedEntity.Addresses',
        message8.read('$.Message.Hits[0].Hit.HittedEntity.Addresses[0]'))
    message8.add('$.Message.Hits[0].Hit.HittedEntity.Addresses[1].Address.Countries', 'pl')

    def message9 = getRawMessage(
        getParsedMessageData()
            .dob('23/3/2023')
            .customerType('Corporate')
            .build())
    message9.add(
        '$.Message.Hits[0].Hit.HittedEntity.DatesOfBirth',
        message9.read('$.Message.Hits[0].Hit.HittedEntity.DatesOfBirth[0]'))
    message9.set('$.Message.Hits[0].Hit.HittedEntity.DatesOfBirth[1].DateOfBirth', '2023-03-23')
    message9.set('$.Message.Hits[0].Hit.HittedEntity.Type', 'C')

    addMessages(
        request,
        [message0, message1, message2, message3, message4, message5, message6, message7, message8,
         message9])

    request
  }

  static def requestEmpty() {
    def request = getRawRequest()

    def message0 = getRawMessage(getEmptyParsedMessageData().build())
    message0.delete('$.Message')

    def message1 = getRawMessage(getEmptyParsedMessageData().build())
    message1.delete('$.Message.*')

    def message2 = getRawMessage(getEmptyParsedMessageData().build())
    message2.delete('$.Message.Hits')

    def message3 = getRawMessage(getEmptyParsedMessageData().build())
    message3.delete('$.Message.Hits.*')

    def message4 = getRawMessage(getEmptyParsedMessageData().build())
    message4.delete('$.Message.SystemID')

    def message5 = getRawMessage(getEmptyParsedMessageData().build())
    message5.delete('$.Message.MessageData')

    def message6 = getRawMessage(getEmptyParsedMessageData().build())
    message6.delete('$.Message.Hits[0].Hit.HittedEntity')

    def message7 = getRawMessage(getEmptyParsedMessageData().build())
    message7.delete('$.Message.Hits[0].Hit.HittedEntity.*')

    def message8 = getRawMessage(getEmptyParsedMessageData().build())
    message8.delete('$.Message.Hits[0].Hit.MatchingText')

    addMessages(
        request,
        [message0, message1, message2, message3, message4, message5, message6, message7, message8])

    request
  }

  static def requestHuge() {
    def request = getRawRequest()

    def message0 = getRawMessage(getParsedMessageData().build())
    (0..100).each {
      message0.add(
          '$.Message.Hits[0].Hit.HittedEntity.Names',
          message0.read('$.Message.Hits[0].Hit.HittedEntity.Names[0]'))
    }

    def message1 = getRawMessage(getParsedMessageData().build())
    (0..100).each {
      message1.add(
          '$.Message.Hits[0].Hit.HittedEntity.Addresses',
          message1.read('$.Message.Hits[0].Hit.HittedEntity.Addresses[0]'))
    }

    def message2 = getRawMessage(getParsedMessageData().build())
    (0..100).each {
      message2.add('$.Message.Hits', message2.read('$.Message.Hits[0]'))
    }

    def message3 = getRawMessage(getParsedMessageData().build())
    (0..100).each {
      message3.add('$.Message.Hits', message3.read('$.Message.Hits[0]'))
    }

    addMessages(
        request,
        [message0, message1, message2, message3])

    request
  }

  static def requestRandom() {
    def names = ['Thomas James Holden', 'Morley Vernon King',
                 'William Raymond Nesbit', 'Henry Randolph Mitchell', 'Omar August Pinson',
                 'thomas james holden', 'morley vernon king',
                 'william raymond nesbit', 'henry randolph mitchell', 'omar august pinson',
                 'THOMAS JAMES HOLDEN', 'MORLEY VERNON KING',
                 'WILLIAM RAYMOND NESBIT', 'HENRY RANDOLPH MITCHELL', 'OMAR AUGUST PINSON']

    def countries = ['IR', 'AF', 'IQ']

    def dates = ['2000-01-01', '1950-03-09', '09/06/1986']

    def request = getRawRequest()
    def messages = []

    (0..100).each {
      def message = getRawMessage(
          getEmptyParsedMessageData()
              .name(names.shuffled().first())
              .country(countries.shuffled().first())
              .dob(dates.shuffled().first())
              .build())

      def randomName = names.shuffled().first()
      message.set('$.Message.Hits[0].Hit.MatchingText', randomName)
      message.set('$.Message.Hits[0].Hit.HittedEntity.Names[0].Name', randomName)
      message.set('$.Message.Hits[0].Hit.HittedEntity.Names[0].Name', randomName)
      message.set(
          '$.Message.Hits[0].Hit.HittedEntity.DatesOfBirth[0].DateOfBirth',
          dates.shuffled().first())

      messages << message
    }

    addMessages(request, messages)

    request
  }

  static void main(String[] args) {
    def outputFolder = new File('/tmp/fab')
    outputFolder.mkdirs()
    new File(outputFolder, 'request-simple.json').write(
        JsonFormatter.prettyPrint(requestSimple().jsonString()))
    new File(outputFolder, 'request-empty.json').write(
        JsonFormatter.prettyPrint(requestEmpty().jsonString()))
    new File(outputFolder, 'request-huge.json').write(
        JsonFormatter.prettyPrint(requestHuge().jsonString()))
    new File(outputFolder, 'request-random.json').write(
        JsonFormatter.prettyPrint(requestRandom().jsonString()))
  }
}
