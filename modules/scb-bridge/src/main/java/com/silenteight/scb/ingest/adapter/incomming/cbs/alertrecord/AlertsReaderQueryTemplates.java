package com.silenteight.scb.ingest.adapter.incomming.cbs.alertrecord;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.intellij.lang.annotations.Language;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class AlertsReaderQueryTemplates {

  @Language("Oracle")
  static final String RECORDS_QUERY =
      "SELECT R.SYSTEM_ID,"
          + "       R.BATCH_ID,"
          + "       R.CHAR_SEP,"
          + "       R.DB_ACCOUNT,"
          + "       R.DB_CITY,"
          + "       R.DB_COUNTRY,"
          + "       R.DB_DOB,"
          + "       R.DB_NAME,"
          + "       R.DB_POB,"
          + "       R.FILTERED,"
          + "       R.RECORD,"
          + "       R.RECORD_ID,"
          + "       R.TYPE_OF_REC,"
          + "       R.LAST_DEC_BATCH_ID,"
          + "       R.UNIT,"
          + "       R.FMT_NAME,"
          + "       H.DETAILS"
          + " FROM :dbRelationName R"
          + " JOIN FFF_HITS_DETAILS H ON H.SYSTEM_ID = R.SYSTEM_ID"
          + " WHERE R.SYSTEM_ID IN (%s)";
  
  @Language("Oracle")
  static final String DECISIONS_QUERY =
      "SELECT D.SYSTEM_ID, D.OPERATOR, D.DECISION_DATE, D.TYPE, D.COMMENTS, S.STATE_NAME"
          + " FROM FFF_DECISIONS D"
          + " LEFT JOIN FOF_STATES S ON S.STATE_ID = D.TYPE"
          + " WHERE D.TYPE != 7 AND D.SYSTEM_ID IN (%s)"
          + " ORDER BY D.DECISION_DATE DESC";
}
