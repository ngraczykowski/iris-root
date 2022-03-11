package com.silenteight.fab.dataprep.domain

import com.silenteight.fab.dataprep.domain.feature.FeatureInputsCommand
import com.silenteight.fab.dataprep.domain.model.ParsedMessageData
import com.silenteight.fab.dataprep.domain.model.RegisteredAlert
import com.silenteight.fab.dataprep.domain.model.RegisteredAlert.Match

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.io.Resources

import static java.nio.charset.StandardCharsets.UTF_8

class Fixtures {

  static final String MATCH_NAME = 'matches/1'
  static final String ALERT_NAME = 'alerts/1'
  static final String HIT_NAME = 'hits/d0bd7272-a12a-11ec-9ee7-7bf12518d571'
  static final String MESSAGE_NAME = 'messages/e525c926-a12a-11ec-97fc-3f5de86f02ac'
  static final String BATCH_NAME = 'batches/031dafde-a12b-11ec-8e04-2f2fd89dfc3f'

  static def MAPPER = new ObjectMapper()

  static ParsedMessageData PARSED_PAYLOAD = ParsedMessageData.builder()
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

  private static URL HIT_URL = Resources.getResource("hit.json");

  static Match MATCH = Match.builder()
      .hitName(UUID.randomUUID().toString())
      .matchName(MATCH_NAME)
      .payload(MAPPER.readTree(HIT_URL))
      .build()

  static Match EMPTY_MATCH = Match.builder()
      .hitName(UUID.randomUUID().toString())
      .matchName(MATCH_NAME)
      .payload(MAPPER.readTree('{}'))
      .build()

  static String HIT = Resources.toString(HIT_URL, UTF_8);

  static String MESSAGE = Resources.toString(Resources.getResource("message.json"), UTF_8);

  static FeatureInputsCommand FEATURE_INPUTS_COMMAND = FeatureInputsCommand.builder()
      .registeredAlert(
          RegisteredAlert.builder()
              .batchName(BATCH_NAME)
              .messageName(MESSAGE_NAME)
              .alertName(ALERT_NAME)
              .parsedMessageData(PARSED_PAYLOAD)
              .matches([MATCH])
              .build())
      .build()

  static FeatureInputsCommand EMPTY_FEATURE_INPUTS_COMMAND = FeatureInputsCommand.builder()
      .registeredAlert(
          RegisteredAlert.builder()
              .batchName(BATCH_NAME)
              .messageName(MESSAGE_NAME)
              .alertName(ALERT_NAME)
              .parsedMessageData(PARSED_PAYLOAD)
              .matches([EMPTY_MATCH])
              .build())
      .build()
}
