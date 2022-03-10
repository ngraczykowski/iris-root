package com.silenteight.fab.dataprep.domain

import com.silenteight.fab.dataprep.domain.feature.FeatureInputsCommand
import com.silenteight.fab.dataprep.domain.model.ExtractedAlert
import com.silenteight.fab.dataprep.domain.model.ExtractedAlert.Match
import com.silenteight.fab.dataprep.domain.model.ParsedPayload

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.io.Resources

class Fixtures {

  static final String MATCH_NAME = 'matches/1'
  static final String ALERT_NAME = 'alerts/1'

  static def MAPPER = new ObjectMapper()

  static ParsedPayload PARSED_PAYLOAD = ParsedPayload.builder()
      .salutation('MR')
      .name('OSAMA BIN LADIN')
      .shortName('BIN LADIN')
      .customerType('I')
      .dob('30/8/1965')
      .dateOfEstablishment('10/20/2022')
      .gender('M')
      .swiftBic('')
      .address1('ADDAX TOWER')
      .address2()
      .city('ABU DHABI')
      .state('ABU DHABI')
      .country('IR')
      .countryOfIncorporation('IR')
      .countryOfDomicile('IR')
      .countryOfBirth('IR')
      .customerSegment('WEALTH')
      .profession('LAWYER')
      .passportNum('AVB2833444')
      .national('S93849384A')
      .tradeLicPlaceOfIssue('10/20/2020')
      .groupOrCompanyName('FIRST ABU DHABI BANK')
      .source('FAB_MOB')
      .sourceSystemId('')
      .customerNumber('ABC1234567')
      .alternate('')
      .latestCustomerNumber('ABC1234567')
      .lastUpdateTime('')
      .build()

  static Match MATCH = Match.builder()
      .matchId(UUID.randomUUID().toString())
      .matchName(MATCH_NAME)
      .payload(MAPPER.readTree(Resources.getResource('message.json')))
  .build()

  static FeatureInputsCommand FEATURE_INPUTS_COMMAND = FeatureInputsCommand.builder()
      .batchId('batchId')   //TODO remove one of batchId
      .extractedAlert(
          ExtractedAlert.builder()
              .batchId('batchId')
              .alertId('alertId')
              .alertName(ALERT_NAME)
              .parsedPayload(PARSED_PAYLOAD)
              .matches([MATCH])
              .build())
      .build()
}
