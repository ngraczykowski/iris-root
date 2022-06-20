/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.reports

import com.silenteight.data.api.v2.Alert
import com.silenteight.iris.bridge.scb.reports.domain.model.Report
import com.silenteight.iris.bridge.scb.reports.domain.model.Report.AlertData

import com.google.protobuf.Struct
import com.google.protobuf.Value

import java.time.OffsetDateTime
import java.time.ZoneOffset

import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME

class ReportFixtures {

  static def ALERT_ONE_ID = 'SANC-ASM-1252185'
  static def ALERT_ONE_NAME = 'alertName/1'
  static def ALERT_ONE_ANALYST_DECISION = 'some decision'
  static def ALERT_ONE_DECISION_MODIFIED_TIME = OffsetDateTime.of(2022,04,19,10,20,0,0, ZoneOffset.UTC)
  static def ALERT_ONE_ANALYST_REASON = 'some reason'

  static AlertData ALERT_DATA_ONE = AlertData.builder()
      .id(ALERT_ONE_ID)
      .alertName(ALERT_ONE_NAME)
      .analystDecision(ALERT_ONE_ANALYST_DECISION)
      .analystDecisionModifiedDateTime(ALERT_ONE_DECISION_MODIFIED_TIME)
      .analystReason(ALERT_ONE_ANALYST_REASON)
      .build()

  static def REPORT_ONE = new Report(ALERT_DATA_ONE)

  static def WAREHOUSE_ALERT = Alert.newBuilder()
      .setName(ALERT_ONE_NAME)
      .setDiscriminator(ALERT_DATA_ONE.id())
      .setAccessPermissionTag('')
      .setPayload(
          Struct.newBuilder()
              .putFields('analystDecision', getValue(ALERT_ONE_ANALYST_DECISION))
              .putFields(
                  'analystDecisionModifiedDateTime', getValue(ALERT_ONE_DECISION_MODIFIED_TIME))
              .putFields('analystReason', getValue(ALERT_ONE_ANALYST_REASON))
              .build())
      .build()

  private static Value getValue(String value) {
    Value.newBuilder().setStringValue(value).build()
  }

  private static Value getValue(OffsetDateTime value) {
    Value.newBuilder().setStringValue(ISO_OFFSET_DATE_TIME.format(value)).build()
  }
}
