/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.batch;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.intellij.lang.annotations.Language;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class QueryTemplates {

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
  static final String SYSTEM_IDS_QUERY =
      "SELECT SYSTEM_ID, BATCH_ID, PRIORITY FROM \n"
          + "   (SELECT SYSTEM_ID, BATCH_ID, 1 as PRIORITY FROM :dbRelationName \n"
          + "   WHERE SUBSTR(UNIT,-4) = 'DENY') deny\n"
          + "UNION ALL \n"
          + "SELECT SYSTEM_ID, BATCH_ID, PRIORITY FROM \n"
          + "   (SELECT SYSTEM_ID, BATCH_ID, 2 as PRIORITY FROM :dbRelationName \n"
          + "   WHERE SUBSTR(UNIT,-4) <> 'DENY') others \n"
          + "ORDER BY PRIORITY ASC, BATCH_ID DESC";
}
