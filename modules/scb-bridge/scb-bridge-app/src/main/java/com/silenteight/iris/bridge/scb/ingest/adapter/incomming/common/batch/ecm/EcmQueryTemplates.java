/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.batch.ecm;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.intellij.lang.annotations.Language;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class EcmQueryTemplates {

  @Language("Oracle")
  static final String RECORDS_QUERY =
      "SELECT R.SYSTEM_ID,\n"
          + "       R.BATCH_ID,\n"
          + "       R.CHAR_SEP,\n"
          + "       R.DB_ACCOUNT,\n"
          + "       R.DB_CITY,\n"
          + "       R.DB_COUNTRY,\n"
          + "       R.DB_DOB,\n"
          + "       R.DB_NAME,\n"
          + "       R.DB_POB,\n"
          + "       R.DECISION_TYPE,\n"
          + "       R.FILTERED,\n"
          + "       R.RECORD,\n"
          + "       R.RECORD_ID,\n"
          + "       R.TYPE_OF_REC,\n"
          + "       R.LAST_DEC_BATCH_ID,\n"
          + "       R.UNIT,\n"
          + "       R.FMT_NAME,\n"
          + "       H.DETAILS\n"
          + "FROM :dbRelationName R\n"
          + "         JOIN FFF_HITS_DETAILS H ON H.SYSTEM_ID = R.SYSTEM_ID\n"
          + "WHERE R.SYSTEM_ID IN (%s)";

  @Language("Oracle")
  static final String EXTERNAL_IDS_QUERY =
      "SELECT "
          + "SYSTEM_ID, "
          + "HIT_UNIQUE_ID "
          + "FROM %s "
          + "ORDER BY GNS_BATCH_ID DESC";

  @Language("Oracle")
  static final String DECISIONS_QUERY =
      "SELECT "
          + "D.SYSTEM_ID,"
          + "D.GNS_BATCH_ID,"
          + "D.HIT_UNIQUE_ID,"
          + "D.ANALYST_ACTIONS,"
          + "D.ANALYST_COMMENTS,"
          + "D.ACTION_DATE,"
          + "D.ANALYST_DECISION "
          + "FROM :ecmViewName D "
          + "WHERE D.SYSTEM_ID IN (%s) "
          + "ORDER BY D.ACTION_DATE DESC";
}
