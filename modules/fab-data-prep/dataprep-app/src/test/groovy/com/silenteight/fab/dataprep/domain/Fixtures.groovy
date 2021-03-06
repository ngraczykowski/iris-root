package com.silenteight.fab.dataprep.domain

import com.silenteight.fab.dataprep.domain.category.BuildCategoryCommand
import com.silenteight.fab.dataprep.domain.feature.BuildFeatureCommand
import com.silenteight.fab.dataprep.domain.feature.FeatureInputsCommand
import com.silenteight.fab.dataprep.domain.model.AlertItem
import com.silenteight.fab.dataprep.domain.model.AlertState
import com.silenteight.fab.dataprep.domain.model.AlertStatus
import com.silenteight.fab.dataprep.domain.model.ParsedAlertMessage
import com.silenteight.fab.dataprep.domain.model.ParsedAlertMessage.Hit
import com.silenteight.fab.dataprep.domain.model.ParsedMessageData
import com.silenteight.fab.dataprep.domain.model.RegisteredAlert
import com.silenteight.fab.dataprep.domain.model.RegisteredAlert.Match
import com.silenteight.proto.fab.api.v1.AlertMessageDetails
import com.silenteight.proto.fab.api.v1.AlertMessagesDetailsResponse

import com.fasterxml.jackson.databind.JsonNode
import com.google.common.io.Resources
import com.jayway.jsonpath.Configuration
import com.jayway.jsonpath.JsonPath
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider

import static com.silenteight.sep.base.common.support.jackson.JsonConversionHelper.INSTANCE
import static java.nio.charset.StandardCharsets.UTF_8

class Fixtures {

  static final String MATCH_NAME = 'alerts/1/matches/1'
  static final String ALERT_NAME = 'alerts/1'
  static final String HIT_ID = 'd0bd7272-a12a-11ec-9ee7-7bf12518d571'
  static final String MESSAGE_NAME = 'messages/e525c926-a12a-11ec-97fc-3f5de86f02ac'
  static final String BATCH_NAME = 'batches/031dafde-a12b-11ec-8e04-2f2fd89dfc3f'
  static final String RECORD_ID = '60C2ED1B-58A1D68E-0326AE78-A8C7CC79'
  static final String SYSTEM_ID = "TRAINING!$RECORD_ID"
  static final String CURRENT_STATUS_NAME = "COMMHUB"
  static final String CURRENT_ACTION_DATE_TIME = "20180827094707"
  static final String METADATA = """{"systemId":"$SYSTEM_ID","recordId":"$RECORD_ID"}"""

  static ParsedMessageData PARSED_PAYLOAD = ParsedMessageData.builder()
      .salutation('MR')
      .name('OSAMA BIN LADIN')
      .shortName('BIN LADIN')
      .customerType('Individual')
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
      .nationalId('S93849384A')
      .tradeLicPlaceOfIssue('10/20/2020')
      .groupOrCompanyName('FIRST ABU DHABI BANK')
      .source('FAB_MOB')
      .sourceSystemId('')
      .customerNumber('ABC1234567')
      .alternate('')
      .latestCustomerNumber('ABC1234567')
      .lastUpdateTime('')
      .build()

  static String MESSAGE = Resources.toString(Resources.getResource("message.json"), UTF_8)

  static String HIT = JsonPath.using(
      Configuration.builder()
          .mappingProvider(new JacksonMappingProvider())
          .build())
      .parse(MESSAGE)
      .read("Message.Hits[0].Hit", JsonNode.class)
      .toPrettyString()

  static Match MATCH = Match.builder()
      .matchName(MATCH_NAME)
      .payloads([INSTANCE.objectMapper().readTree(HIT)])
      .build()

  static Match EMPTY_MATCH = Match.builder()
      .matchName(MATCH_NAME)
      .payloads([INSTANCE.objectMapper().readTree('{}')])
      .build()

  static RegisteredAlert REGISTERED_ALERT = RegisteredAlert.builder()
      .batchName(BATCH_NAME)
      .alertName(ALERT_NAME)
      .messageName(MESSAGE_NAME)
      .systemId(SYSTEM_ID)
      .parsedMessageData(PARSED_PAYLOAD)
      .matches([EMPTY_MATCH])
      .status(AlertStatus.SUCCESS)
      .build()

  static RegisteredAlert REGISTERED_ALERT_WITHOUT_MATCHES = RegisteredAlert.builder()
      .batchName(BATCH_NAME)
      .alertName(ALERT_NAME)
      .messageName(MESSAGE_NAME)
      .systemId(SYSTEM_ID)
      .parsedMessageData(PARSED_PAYLOAD)
      .matches([])
      .status(AlertStatus.SUCCESS)
      .build()

  static FeatureInputsCommand EMPTY_FEATURE_INPUTS_COMMAND = FeatureInputsCommand.builder()
      .registeredAlert(REGISTERED_ALERT)
      .build()

  static BuildFeatureCommand EMPTY_BUILD_FEATURE_COMMAND = BuildFeatureCommand.builder()
      .parsedMessageData(PARSED_PAYLOAD)
      .match(EMPTY_MATCH).build()

  static BuildFeatureCommand BUILD_FEATURE_COMMAND = BuildFeatureCommand.builder()
      .parsedMessageData(PARSED_PAYLOAD)
      .match(MATCH)
      .build()

  static BuildCategoryCommand BUILD_CATEGORY_COMMAND = BuildCategoryCommand.builder()
      .match(MATCH)
      .alertName(ALERT_NAME)
      .systemId(SYSTEM_ID)
      .parsedMessageData(PARSED_PAYLOAD)
      .build()

  static AlertMessageDetails ALERT_MESSAGE_DETAILS = AlertMessageDetails.newBuilder()
      .setMessageName(MESSAGE_NAME)
      .setPayload(MESSAGE)
      .build()

  static AlertMessagesDetailsResponse ALERT_MESSAGES_DETAILS_RESPONSE = AlertMessagesDetailsResponse
      .newBuilder()
      .addAlerts(ALERT_MESSAGE_DETAILS)
      .build()

  static ParsedAlertMessage PARSED_ALERT_MESSAGE = ParsedAlertMessage.builder()
      .batchName(BATCH_NAME)
      .messageName(MESSAGE_NAME)
      .systemId(SYSTEM_ID)
      .currentActionComment("")
      .currentActionDateTime(CURRENT_ACTION_DATE_TIME)
      .currentStatusName(CURRENT_STATUS_NAME)
      .parsedMessageData(PARSED_PAYLOAD)
      .hits(
          [
              (HIT_ID): Hit.builder()
                  .hitName(HIT_ID)
                  .payloads([INSTANCE.objectMapper().readTree('{}')])
                  .build()
          ])
      .build()

  static ALERT_ITEM = AlertItem.builder()
      .alertName(ALERT_NAME)
      .messageName(MESSAGE_NAME)
      .state(AlertState.REGISTERED)
      .matchNames([MATCH_NAME])
      .build()

  static ALERT_ITEM_IN_UDS = AlertItem.builder()
      .alertName(ALERT_NAME)
      .messageName(MESSAGE_NAME)
      .state(AlertState.IN_UDS)
      .matchNames([MATCH_NAME])
      .build()
}
