--------------------------------------------------------
--  File created - Friday-May-27-2022   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Sequence HIBERNATE_SEQUENCE
--------------------------------------------------------

   CREATE SEQUENCE  "LC_GNS_WEB_SIT_01"."HIBERNATE_SEQUENCE"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 681 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Sequence SEQ_FOF_STATES
--------------------------------------------------------

   CREATE SEQUENCE  "LC_GNS_WEB_SIT_01"."SEQ_FOF_STATES"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 290 CACHE 20 NOORDER  NOCYCLE ;
--------------------------------------------------------
--  DDL for Table CBS_HITS_DETAILS
--------------------------------------------------------

  CREATE TABLE "LC_GNS_WEB_SIT_01"."CBS_HITS_DETAILS" 
   (	"CBS_HIT_DETAILS_ID" VARCHAR2(64 BYTE), 
	"CBS_HISTBATCH_ID" NUMBER, 
	"SYSTEM_ID" VARCHAR2(64 BYTE), 
	"BATCH_ID" VARCHAR2(64 BYTE), 
	"GNS_RUN_DATE" DATE, 
	"HIT_UNIQ_ID" VARCHAR2(1000 BYTE), 
	"HIT_WATCHLIST_ID" VARCHAR2(11 BYTE), 
	"HIT_NEO_FLAG" VARCHAR2(20 BYTE), 
	"HIT_DATE" VARCHAR2(32 BYTE), 
	"HIT_SCORE" VARCHAR2(100 BYTE), 
	"HIT_OFFICIAL_REFERENCE" VARCHAR2(300 BYTE), 
	"HIT_MATCHING_TEXT" VARCHAR2(300 BYTE), 
	"HIT_ENTITY_TEXT" VARCHAR2(300 BYTE), 
	"HIT_ENTITY_TYPE" VARCHAR2(100 BYTE), 
	"HIT_NAME" CLOB, 
	"HIT_ADDRESS" CLOB, 
	"HIT_CITY" CLOB, 
	"HIT_STATE" CLOB, 
	"HIT_COUNTRY" CLOB, 
	"HIT_ORIGIN" VARCHAR2(100 BYTE), 
	"HIT_DESIGNATION" VARCHAR2(100 BYTE), 
	"HIT_KEYWORDS" CLOB, 
	"HIT_TAG" VARCHAR2(16 BYTE), 
	"HIT_ADDITIONAL_INFO" CLOB, 
	"HIT_FML_TYPE" VARCHAR2(100 BYTE), 
	"HIT_FML_PRIORITY" VARCHAR2(100 BYTE), 
	"HIT_FML_CONFIDENTIALITY" VARCHAR2(100 BYTE), 
	"HIT_FML_INFO" VARCHAR2(100 BYTE), 
	"HIT_DATEOFBIRTH" VARCHAR2(300 BYTE), 
	"HIT_PLACEOFBIRTH" VARCHAR2(300 BYTE), 
	"HIT_HYPERLINKS" VARCHAR2(2000 BYTE), 
	"HIT_USERDATA1" VARCHAR2(300 BYTE), 
	"HIT_USERDATA2" VARCHAR2(300 BYTE), 
	"HIT_NATIONALITY" VARCHAR2(300 BYTE), 
	"HIT_PASSPORT" VARCHAR2(300 BYTE), 
	"HIT_NATIONAL_ID" VARCHAR2(300 BYTE), 
	"HIT_SEARCH_CODES" VARCHAR2(300 BYTE), 
	"HIT_BIC_CODES" VARCHAR2(300 BYTE), 
	"HIT_PEP" VARCHAR2(100 BYTE), 
	"HIT_FEP" VARCHAR2(100 BYTE), 
	"HIT_TYS" VARCHAR2(100 BYTE), 
	"HIT_ISN" VARCHAR2(100 BYTE), 
	"HIT_BATCH_ID" VARCHAR2(100 BYTE), 
	"HIT_RESULT" VARCHAR2(100 BYTE), 
	"HIT_CLOB" CLOB, 
	"CIF_ACCOUNT" VARCHAR2(256 BYTE), 
	"CIF_CITY" VARCHAR2(1024 BYTE), 
	"CIF_COUNTRY" VARCHAR2(256 BYTE), 
	"CIF_DATEOFBIRTH" VARCHAR2(128 BYTE), 
	"CIF_NAME" VARCHAR2(256 BYTE), 
	"CIF_PLACEOFBIRTH" VARCHAR2(256 BYTE), 
	"CIF_STATE" VARCHAR2(256 BYTE), 
	"CIF_USERDATA1" VARCHAR2(256 BYTE), 
	"CIF_USERDATA2" VARCHAR2(256 BYTE), 
	"CIF_RECORD" CLOB, 
	"ALERT_UNIT" VARCHAR2(32 BYTE), 
	"ALERT_BUSINESS_UNIT" VARCHAR2(256 BYTE), 
	"ALERT_APPLICATION_CODE" VARCHAR2(256 BYTE), 
	"ALERT_MESSAGE_ID" VARCHAR2(256 BYTE), 
	"ALERT_LAST_OPERATOR" VARCHAR2(64 BYTE), 
	"ALERT_LAST_COMMENT" VARCHAR2(1024 BYTE), 
	"ALERT_FILTERING_DATE" VARCHAR2(32 BYTE), 
	"ALERT_CREATE_DATE" VARCHAR2(32 BYTE), 
	"ALERT_BLOCKING_HITS" NUMBER, 
	"ALERT_NONBLOCKING_HITS" NUMBER, 
	"WATCHLIST_SOURCE" VARCHAR2(20 BYTE), 
	"WATCHLIST_CATEGORY" VARCHAR2(20 BYTE), 
	"SCREENING_TYPE" VARCHAR2(10 BYTE), 
	"SCREENING_PARAMETERS" VARCHAR2(10 BYTE), 
	"CIF_FILE_FORMAT" VARCHAR2(20 BYTE), 
	"CIF_FIELD_1" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_2" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_3" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_4" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_5" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_6" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_7" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_8" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_9" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_10" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_11" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_12" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_13" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_14" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_15" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_16" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_17" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_18" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_19" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_20" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_21" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_22" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_23" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_24" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_25" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_26" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_27" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_28" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_29" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_30" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_31" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_32" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_33" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_34" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_35" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_36" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_37" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_38" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_39" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_40" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_41" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_42" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_43" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_44" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_45" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_46" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_47" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_48" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_49" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_50" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_51" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_52" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_53" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_54" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_55" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_56" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_57" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_58" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_59" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_60" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_61" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_62" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_63" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_64" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_65" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_66" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_67" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_68" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_69" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_70" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_71" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_72" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_73" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_74" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_75" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_76" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_77" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_78" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_79" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_80" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_81" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_82" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_83" VARCHAR2(4000 BYTE), 
	"CIF_FIELD_84" VARCHAR2(4000 BYTE), 
	"ROUTING_FLAG" VARCHAR2(10 BYTE), 
	"STATUS_FLAG" VARCHAR2(10 BYTE), 
	"ALERT_EXTRACT_DATE" VARCHAR2(50 BYTE), 
	"ARCHIVAL_DATE" DATE, 
	"LAST_UPDATED_PROCESS" VARCHAR2(100 BYTE), 
	"LAST_UPDATE_DATE" VARCHAR2(50 BYTE), 
	"SUPPLEMENTARY_INFO_1" VARCHAR2(1000 BYTE), 
	"SUPPLEMENTARY_INFO_2" VARCHAR2(1000 BYTE), 
	"SUPPLEMENTARY_INFO_3" VARCHAR2(1000 BYTE), 
	"SUPPLEMENTARY_INFO_4" VARCHAR2(1000 BYTE), 
	"SUPPLEMENTARY_INFO_5" VARCHAR2(1000 BYTE), 
	"SENS_ACK_FLAG" VARCHAR2(10 BYTE), 
	"HIT_SEQ_NUMBER" NUMBER, 
	"CIF_USERDATA_1" VARCHAR2(256 CHAR), 
	"CIF_USERDATA_2" VARCHAR2(256 CHAR), 
	"HIT_USERDATA_1" VARCHAR2(300 CHAR), 
	"HIT_USERDATA_2" VARCHAR2(300 CHAR)
   ) TABLESPACE "LC_GNS_SIT_DATA"  ;
--------------------------------------------------------
--  DDL for Table CBS_S8_READ_LOG
--------------------------------------------------------

  CREATE TABLE "LC_GNS_WEB_SIT_01"."CBS_S8_READ_LOG" 
   (	"GNS_RUN_DATE" DATE, 
	"SYSTEM_ID" VARCHAR2(64 BYTE), 
	"BATCH_ID" VARCHAR2(64 BYTE), 
	"HIT_WATCHLIST_ID" VARCHAR2(11 BYTE), 
	"HIT_UNIQ_ID" VARCHAR2(4000 BYTE), 
	"S8_READ_DATE" VARCHAR2(50 BYTE), 
	"HIT_RECOMMENDED_STATUS" VARCHAR2(64 BYTE), 
	"HIT_RECOMMENDED_COMMENTS" CLOB, 
	"LIST_RECOMMENDED_STATUS" VARCHAR2(64 BYTE), 
	"LIST_RECOMMENDED_COMMENTS" CLOB, 
	"S8_RECOMENDATION_DATE" VARCHAR2(50 BYTE), 
	"STATUS_FLAG" VARCHAR2(10 BYTE), 
	"SOURCE_APPLN" VARCHAR2(50 BYTE)
) TABLESPACE "LC_GNS_SIT_DATA"  ;
--------------------------------------------------------
--  DDL for Table FFF_DECISIONS
--------------------------------------------------------

  CREATE TABLE "LC_GNS_WEB_SIT_01"."FFF_DECISIONS" 
   (	"SYSTEM_ID" VARCHAR2(64 BYTE), 
	"DECISION_DATE" VARCHAR2(32 BYTE), 
	"TYPE" NUMBER(22,0), 
	"OPERATOR" VARCHAR2(64 BYTE), 
	"COMMENTS" VARCHAR2(1024 BYTE)
) TABLESPACE "LC_GNS_SIT_DATA"  ;
--------------------------------------------------------
--  DDL for Table FFF_HISTBATCH
--------------------------------------------------------

  CREATE TABLE "LC_GNS_WEB_SIT_01"."FFF_HISTBATCH" 
   (	"BATCH_ID" VARCHAR2(32 BYTE), 
	"ALGO_BITSTREAM" NUMBER(22,0), 
	"AVG_DURATION_PER_RECORD" NUMBER(22,0), 
	"BATCH_DATE" VARCHAR2(32 BYTE), 
	"BLOCKING_RATE" FLOAT(126), 
	"FILE_NAME" VARCHAR2(256 BYTE), 
	"FILTER_SEVERITY" VARCHAR2(32 BYTE), 
	"FILTER_TYPE" VARCHAR2(32 BYTE), 
	"FMT_CHECKPART" VARCHAR2(4 BYTE), 
	"FMT_DUPLICATES" VARCHAR2(5 BYTE), 
	"FMT_FIELD_SEPARATOR" VARCHAR2(8 BYTE), 
	"FMT_FILE" VARCHAR2(32 BYTE), 
	"FMT_RECORD_SEPARATOR" VARCHAR2(8 BYTE), 
	"FMT_RECORD_TYPE" VARCHAR2(32 BYTE), 
	"HIT_RATE" FLOAT(126), 
	"LIST_AUTHOR" VARCHAR2(128 BYTE), 
	"LIST_CREATION_DATE" VARCHAR2(32 BYTE), 
	"LIST_FILE_NAME" VARCHAR2(32 BYTE), 
	"LIST_GENERATOR" VARCHAR2(128 BYTE), 
	"LIST_NB_LISTED" NUMBER(22,0), 
	"LIST_NB_EXCEPTIONS" NUMBER(22,0), 
	"LIST_NB_RECORDS" NUMBER(22,0), 
	"LIST_ORIGIN" VARCHAR2(256 BYTE), 
	"LIST_RULE_FILE_NAME" VARCHAR2(32 BYTE), 
	"LIST_TITLE" VARCHAR2(128 BYTE), 
	"NB_ALERTS" NUMBER(22,0) DEFAULT 0, 
	"NB_ALERTS_EXISTING" NUMBER(22,0) DEFAULT 0, 
	"NB_NEW_ALERTS" NUMBER(22,0) DEFAULT 0, 
	"NB_NEW_RECORDS_WITH_ALERTS" NUMBER(22,0) DEFAULT 0, 
	"NB_RECORDS_WITH_ALERTS" NUMBER(22,0) DEFAULT 0, 
	"NB_RECORDS_NO_ALERTS" NUMBER(22,0) DEFAULT 0, 
	"NB_RECORDS_NOT_FILTERED" NUMBER(22,0) DEFAULT 0, 
	"NB_RECORDS_PROCESSED" NUMBER(22,0) DEFAULT 0, 
	"NB_RECORDS_UPDATED" NUMBER(22,0) DEFAULT 0, 
	"NON_BLOCKING_RATE" FLOAT(126), 
	"PREFILTERED_RATE" FLOAT(126), 
	"PROCESS_ENGINE" VARCHAR2(32 BYTE), 
	"PROCESS_ENGINE_VERSION" VARCHAR2(16 BYTE), 
	"PROCESS_OS" VARCHAR2(32 BYTE), 
	"PROCESS_STATUS" VARCHAR2(32 BYTE), 
	"SENDING_APP" VARCHAR2(32 BYTE), 
	"THRESHOLD" NUMBER(22,0) DEFAULT 0, 
	"TIME_END" VARCHAR2(32 BYTE), 
	"TIME_START" VARCHAR2(32 BYTE), 
	"UNIT" VARCHAR2(32 BYTE), 
	"NB_RECORDS_NEW" NUMBER(22,0) DEFAULT 0, 
	"NB_RECORDS_TRUE" NUMBER(22,0) DEFAULT 0, 
	"NB_RECORDS_FALSE" NUMBER(22,0) DEFAULT 0, 
	"NB_RECORDS_PENDING" NUMBER(22,0) DEFAULT 0, 
	"NB_RECORDS_NEW_TRUE" NUMBER(22,0) DEFAULT 0, 
	"NB_RECORDS_NEW_FALSE" NUMBER(22,0) DEFAULT 0
) TABLESPACE "LC_GNS_SIT_DATA"  ;
--------------------------------------------------------
--  DDL for Table FFF_HITS_COMMENTS
--------------------------------------------------------

  CREATE TABLE "LC_GNS_WEB_SIT_01"."FFF_HITS_COMMENTS" 
   (	"SYSTEM_ID" VARCHAR2(64 BYTE), 
	"HIT_ID" VARCHAR2(16 BYTE), 
	"HIT_POSITIONS" VARCHAR2(256 BYTE), 
	"OPERATOR" VARCHAR2(64 BYTE), 
	"HIT_COMMENT" VARCHAR2(256 BYTE), 
	"IS_TRUE_HIT" NUMBER(22,0), 
	"COMMENT_DATE" VARCHAR2(32 BYTE)
) TABLESPACE "LC_GNS_SIT_DATA"  ;
--------------------------------------------------------
--  DDL for Table FFF_HITS_DETAILS
--------------------------------------------------------

  CREATE TABLE "LC_GNS_WEB_SIT_01"."FFF_HITS_DETAILS" 
   (	"SYSTEM_ID" VARCHAR2(64 BYTE), 
	"DETAILS" CLOB
) TABLESPACE "LC_GNS_SIT_DATA"  ;
--------------------------------------------------------
--  DDL for Table FFF_RECORDS
--------------------------------------------------------

  CREATE TABLE "LC_GNS_WEB_SIT_01"."FFF_RECORDS" 
   (	"SYSTEM_ID" VARCHAR2(64 BYTE), 
	"BATCH_ID" VARCHAR2(32 BYTE), 
	"LAST_DEC_BATCH_ID" VARCHAR2(32 BYTE), 
	"BLOCKING_ALERTS" NUMBER(22,0) DEFAULT 0, 
	"BUNIT" VARCHAR2(256 BYTE), 
	"CHAR_SEP" NUMBER(22,0) DEFAULT 10, 
	"CONFIDENTIALITY" NUMBER(22,0) DEFAULT 0, 
	"CREATED" VARCHAR2(32 BYTE), 
	"DB_ACCOUNT" VARCHAR2(256 BYTE), 
	"DB_CITY" VARCHAR2(1024 BYTE), 
	"DB_COUNTRY" VARCHAR2(256 BYTE), 
	"DB_DOB" VARCHAR2(128 BYTE), 
	"DB_NAME" VARCHAR2(256 BYTE), 
	"DB_POB" VARCHAR2(256 BYTE), 
	"DB_STATE" VARCHAR2(256 BYTE), 
	"DB_USER1" VARCHAR2(256 BYTE), 
	"DB_USER2" VARCHAR2(256 BYTE), 
	"DECISION_TYPE" NUMBER(22,0), 
	"FILTERED" VARCHAR2(32 BYTE), 
	"FROM_APPLI" VARCHAR2(256 BYTE), 
	"HOLDER" VARCHAR2(64 BYTE), 
	"LAST_COMMENT" VARCHAR2(1024 BYTE), 
	"LAST_OPERATOR" VARCHAR2(64 BYTE), 
	"LAST_UPDATE" VARCHAR2(32 BYTE), 
	"NATURE" VARCHAR2(256 BYTE), 
	"NONBLOCKING_ALERTS" NUMBER(22,0) DEFAULT 0, 
	"PRIORITY" NUMBER(22,0) DEFAULT 0, 
	"RECORD" CLOB, 
	"RECORD_ID" VARCHAR2(256 BYTE), 
	"RECORD_LOCK" NUMBER(22,0) DEFAULT 0, 
	"SIGN_KEY" VARCHAR2(256 BYTE), 
	"SIGN_KEY2" VARCHAR2(256 BYTE), 
	"TYPE_OF_REC" VARCHAR2(256 BYTE), 
	"UNIT" VARCHAR2(32 BYTE), 
	"PREV_DECISION_TYPE" NUMBER(22,0) DEFAULT -1, 
	"FMT_NAME" VARCHAR2(64 BYTE)
) TABLESPACE "LC_GNS_SIT_DATA"  ;
--------------------------------------------------------
--  DDL for Table FOF_HITS
--------------------------------------------------------

  CREATE TABLE "LC_GNS_WEB_SIT_01"."FOF_HITS" 
   (	"SYSTEM_ID" VARCHAR2(64 BYTE), 
	"SEQNUMBER" NUMBER(22,0), 
	"ID_LIST" VARCHAR2(11 BYTE), 
	"TYPE" VARCHAR2(17 BYTE), 
	"ORIGIN" VARCHAR2(21 BYTE), 
	"DESIGNATION" VARCHAR2(21 BYTE), 
	"KEYWORDS" CLOB, 
	"PEP" NUMBER(22,0) DEFAULT 0, 
	"FEP" NUMBER(22,0) DEFAULT 0, 
	"TAG" VARCHAR2(16 BYTE), 
	"NAME" VARCHAR2(300 BYTE), 
	"CITY" VARCHAR2(128 BYTE), 
	"COUNTRY" VARCHAR2(100 BYTE), 
	"STATE" VARCHAR2(100 BYTE), 
	"USERDATA1" VARCHAR2(64 BYTE), 
	"USERDATA2" VARCHAR2(64 BYTE), 
	"OFFICIALREF" VARCHAR2(64 BYTE), 
	"FMLTYPE" VARCHAR2(4 BYTE), 
	"FMLPRIORITY" NUMBER(22,0) DEFAULT 0, 
	"FMLCONLEVEL" NUMBER(22,0) DEFAULT 0, 
	"ID_REFERENCE" VARCHAR2(36 BYTE), 
	"FILTERINGDATE" VARCHAR2(32 BYTE), 
	"MATCH_LEVEL" VARCHAR2(6 BYTE), 
	"SIGN_KEY" VARCHAR2(255 BYTE), 
	"HIT_TYPE" VARCHAR2(255 BYTE), 
	"RECORD_MATCHING_STRING" VARCHAR2(300 BYTE), 
	"LIST_MATCHING_STRING" VARCHAR2(300 BYTE), 
	"BATCH_ID" VARCHAR2(32 BYTE)
) TABLESPACE "LC_GNS_SIT_DATA"  ;
--------------------------------------------------------
--  DDL for Table FOF_STATES
--------------------------------------------------------

  CREATE TABLE "LC_GNS_WEB_SIT_01"."FOF_STATES" 
   (	"STATE_ID" NUMBER(22,0), 
	"STATE_NAME" VARCHAR2(16 BYTE), 
	"FINAL_STATE" NUMBER(22,0), 
	"DATA_TYPE" NUMBER(22,0), 
	"STATUS" NUMBER(22,0), 
	"CONTROL_TRANSITION" NUMBER(22,0), 
	"BEHAVIOUR" NUMBER(22,0), 
	"LABEL" VARCHAR2(10 BYTE), 
	"DESCRIPTION" VARCHAR2(500 BYTE)
) TABLESPACE "LC_GNS_SIT_DATA"  ;

CREATE OR REPLACE FORCE VIEW "LC_GNS_WEB_SIT_01"."CBS_HITS_DETAILS_S8_HLPR_V_WL" (
    "SEQ_NO",
    "SYSTEM_ID",
    "BATCH_ID",
    "HIT_UNIQ_ID",
    "HIT_NEO_FLAG"
) AS
    SELECT
        hit_seq_number AS seq_no,
        system_id      AS system_id,
        batch_id       AS batch_id,
        hit_uniq_id    AS hit_uniq_id,
        hit_neo_flag   AS hit_neo_flag
    FROM
        cbs_hits_details a
    WHERE
        routing_flag IN ( 'S8', 'SV', 'SE' )
        AND NOT EXISTS (
            SELECT
                1
            FROM
                cbs_s8_read_log b
            WHERE
                    a.gns_run_date = b.gns_run_date
                AND a.system_id = b.system_id
                AND a.hit_uniq_id = b.hit_uniq_id
                AND a.batch_id = b.batch_id
                AND b.source_appln = 'S8'
        );
    
--------------------------------------------------------
--  DDL for View CBS_HITS_DETAILS_S8_SERP_V_AL
--------------------------------------------------------

CREATE OR REPLACE FORCE VIEW "LC_GNS_WEB_SIT_01"."CBS_HITS_DETAILS_S8_SERP_V_AL" (
    "SYSTEM_ID",
    "BATCH_ID",
    "HIT_UNIQ_ID",
    "HIT_NEO_FLAG"
) AS
    SELECT
        system_id    AS system_id,
        batch_id     AS batch_id,
        hit_uniq_id  AS hit_uniq_id,
        hit_neo_flag AS hit_neo_flag
    FROM
        cbs_hits_details a
    WHERE
            routing_flag = 'S8'
        AND status_flag = 'P'
        AND hit_neo_flag = 'N'
        AND NOT EXISTS (
            SELECT
                1
            FROM
                cbs_s8_read_log b
            WHERE
                    a.gns_run_date = b.gns_run_date
                AND a.system_id = b.system_id
                AND a.hit_uniq_id = b.hit_uniq_id
                AND a.batch_id = b.batch_id
                AND b.source_appln = 'SA'
        );
        
--------------------------------------------------------
--  DDL for View CBS_HITS_DETAILS_S8_SERP_V_WL
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "LC_GNS_WEB_SIT_01"."CBS_HITS_DETAILS_S8_SERP_V_WL" (
    "SYSTEM_ID",
    "BATCH_ID",
    "HIT_UNIQ_ID",
    "HIT_NEO_FLAG"
)
    AS
        SELECT
            system_id    AS system_id,
            batch_id     AS batch_id,
            hit_uniq_id  AS hit_uniq_id,
            hit_neo_flag AS hit_neo_flag
        FROM
            cbs_hits_details a
        WHERE
            routing_flag IN ( 'S8', 'SE', 'SV' )
            AND status_flag = 'P'
                AND hit_neo_flag = 'N'
                    AND NOT EXISTS (
                SELECT
                    1
                FROM
                    cbs_s8_read_log b
                WHERE
                        a.gns_run_date = b.gns_run_date
                    AND a.system_id = b.system_id
                        AND a.hit_uniq_id = b.hit_uniq_id
                            AND a.batch_id = b.batch_id
                                AND b.source_appln = 'S8'
            );
            
--------------------------------------------------------
--  DDL for View SENS_V_FFF_RECORDS_AL
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "LC_GNS_WEB_SIT_01"."SENS_V_FFF_RECORDS_AL" ("SYSTEM_ID", "BATCH_ID", "LAST_DEC_BATCH_ID", "BLOCKING_ALERTS", "BUNIT", "CHAR_SEP", "CONFIDENTIALITY", "CREATED", "DB_ACCOUNT", "DB_CITY", "DB_COUNTRY", "DB_DOB", "DB_NAME", "DB_POB", "DB_STATE", "DB_USER1", "DB_USER2", "DECISION_TYPE", "FILTERED", "FROM_APPLI", "HOLDER", "LAST_COMMENT", "LAST_OPERATOR", "LAST_UPDATE", "NATURE", "NONBLOCKING_ALERTS", "PRIORITY", "RECORD", "RECORD_ID", "RECORD_LOCK", "SIGN_KEY", "SIGN_KEY2", "TYPE_OF_REC", "UNIT", "PREV_DECISION_TYPE", "FMT_NAME") AS 
  select "SYSTEM_ID","BATCH_ID","LAST_DEC_BATCH_ID","BLOCKING_ALERTS","BUNIT","CHAR_SEP","CONFIDENTIALITY","CREATED","DB_ACCOUNT","DB_CITY","DB_COUNTRY","DB_DOB","DB_NAME","DB_POB","DB_STATE","DB_USER1","DB_USER2","DECISION_TYPE","FILTERED","FROM_APPLI","HOLDER","LAST_COMMENT","LAST_OPERATOR","LAST_UPDATE","NATURE","NONBLOCKING_ALERTS","PRIORITY","RECORD","RECORD_ID","RECORD_LOCK","SIGN_KEY","SIGN_KEY2","TYPE_OF_REC","UNIT","PREV_DECISION_TYPE","FMT_NAME"
FROM FFF_RECORDS fr
WHERE 
  decision_type in (0,4) 
  and (SYSTEM_ID,BATCH_ID) IN (SELECT  SYSTEM_ID,BATCH_ID FROM CBS_HITS_DETAILS_S8_SERP_V_AL) 
and nvl(bunit,'XXXXX')!='GNSRT' 
and nvl(db_user2,'YYYYY')!='GNSRT';

--------------------------------------------------------
--  DDL for View SENS_V_FFF_RECORDS_WL
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "LC_GNS_WEB_SIT_01"."SENS_V_FFF_RECORDS_WL" ("SYSTEM_ID", "BATCH_ID", "LAST_DEC_BATCH_ID", "BLOCKING_ALERTS", "BUNIT", "CHAR_SEP", "CONFIDENTIALITY", "CREATED", "DB_ACCOUNT", "DB_CITY", "DB_COUNTRY", "DB_DOB", "DB_NAME", "DB_POB", "DB_STATE", "DB_USER1", "DB_USER2", "DECISION_TYPE", "FILTERED", "FROM_APPLI", "HOLDER", "LAST_COMMENT", "LAST_OPERATOR", "LAST_UPDATE", "NATURE", "NONBLOCKING_ALERTS", "PRIORITY", "RECORD", "RECORD_ID", "RECORD_LOCK", "SIGN_KEY", "SIGN_KEY2", "TYPE_OF_REC", "UNIT", "PREV_DECISION_TYPE", "FMT_NAME") AS 
  select "SYSTEM_ID","BATCH_ID","LAST_DEC_BATCH_ID","BLOCKING_ALERTS","BUNIT","CHAR_SEP","CONFIDENTIALITY","CREATED","DB_ACCOUNT","DB_CITY","DB_COUNTRY","DB_DOB","DB_NAME","DB_POB","DB_STATE","DB_USER1","DB_USER2","DECISION_TYPE","FILTERED","FROM_APPLI","HOLDER","LAST_COMMENT","LAST_OPERATOR","LAST_UPDATE","NATURE","NONBLOCKING_ALERTS","PRIORITY","RECORD","RECORD_ID","RECORD_LOCK","SIGN_KEY","SIGN_KEY2","TYPE_OF_REC","UNIT","PREV_DECISION_TYPE","FMT_NAME"
FROM FFF_RECORDS fr
where 
decision_type in (0,4)
and (SYSTEM_ID,BATCH_ID) IN (SELECT  SYSTEM_ID,BATCH_ID FROM CBS_HITS_DETAILS_S8_SERP_V_WL)
and substr(trim(batch_id),12,15) != 'LIVE' 
and nvl(bunit,'XXXXX')!='GNSRT' 
and nvl(db_user2,'YYYYY')!='GNSRT' 
and substr(batch_id, 1, 10) >= '2020/10/01';


 CREATE OR REPLACE FORCE VIEW "LC_GNS_WEB_SIT_01"."CBS_HITS_DETAILS_S8_SERP_V" ("SYSTEM_ID", "BATCH_ID", "HIT_UNIQ_ID", "HIT_NEO_FLAG") AS
  SELECT
          System_ID AS System_ID,
          Batch_ID AS Batch_ID,
          Hit_Uniq_ID AS Hit_Uniq_ID,
          Hit_NEO_Flag AS Hit_NEO_Flag
    FROM CBS_HITS_DETAILS
    WHERE ROUTING_FLAG IN('S8','SE','SV')
    AND STATUS_FLAG='P'
    AND HIT_NEO_FLAG='N';


  CREATE OR REPLACE FORCE VIEW "LC_GNS_WEB_SIT_01"."SERP_V_FFF_RECORDS_WL_DENY" ("SYSTEM_ID", "BATCH_ID", "LAST_DEC_BATCH_ID", "BLOCKING_ALERTS", "BUNIT", "CHAR_SEP", "CONFIDENTIALITY", "CREATED", "DB_ACCOUNT", "DB_CITY", "DB_COUNTRY", "DB_DOB", "DB_NAME", "DB_POB", "DB_STATE", "DB_USER1", "DB_USER2", "DECISION_TYPE", "FILTERED", "FROM_APPLI", "HOLDER", "LAST_COMMENT", "LAST_OPERATOR", "LAST_UPDATE", "NATURE", "NONBLOCKING_ALERTS", "PRIORITY", "RECORD", "RECORD_ID", "RECORD_LOCK", "SIGN_KEY", "SIGN_KEY2", "TYPE_OF_REC", "UNIT", "PREV_DECISION_TYPE", "FMT_NAME") AS
  SELECT
SYSTEM_ID, BATCH_ID, LAST_DEC_BATCH_ID, BLOCKING_ALERTS, BUNIT, CHAR_SEP, CONFIDENTIALITY, CREATED, DB_ACCOUNT, DB_CITY, DB_COUNTRY, DB_DOB, DB_NAME, DB_POB,
DB_STATE, DB_USER1, DB_USER2, DECISION_TYPE, FILTERED, FROM_APPLI, HOLDER, LAST_COMMENT, LAST_OPERATOR, LAST_UPDATE, NATURE, NONBLOCKING_ALERTS, PRIORITY,
RECORD, RECORD_ID, RECORD_LOCK, SIGN_KEY, SIGN_KEY2, TYPE_OF_REC, UNIT, PREV_DECISION_TYPE,
CASE
  WHEN FMT_NAME IN (
    'SCB_DUDL_CDD_V1',
    'SCB_DUDL_CDDR_V1',
    'SCB_DUDL_SME_V1',
    'SCB_DUDL_SMER_V1'
  ) THEN
  'SCB_EDMP_ADVM_2'
  WHEN FMT_NAME IN (
    'SCB_DUED_CDD_V1',
    'SCB_DUED_CDDR_V1',
    'SCB_DUED_SME_V1',
    'SCB_DUED_SMER_V1'
  ) THEN
  'SCB_EDMP_DUED_2'
  ELSE FMT_NAME
END AS FMT_NAME
FROM FFF_RECORDS
WHERE (SYSTEM_ID,BATCH_ID) IN (SELECT  SYSTEM_ID,BATCH_ID FROM CBS_HITS_DETAILS_S8_SERP_V)
AND UPPER(TRIM(UNIT)) LIKE '%DENY%';

CREATE OR REPLACE FORCE VIEW "LC_GNS_WEB_SIT_01"."SERP_V_FFF_RECORDS_WL_NON_DENY" ("SYSTEM_ID", "BATCH_ID", "LAST_DEC_BATCH_ID", "BLOCKING_ALERTS", "BUNIT", "CHAR_SEP", "CONFIDENTIALITY", "CREATED", "DB_ACCOUNT", "DB_CITY", "DB_COUNTRY", "DB_DOB", "DB_NAME", "DB_POB", "DB_STATE", "DB_USER1", "DB_USER2", "DECISION_TYPE", "FILTERED", "FROM_APPLI", "HOLDER", "LAST_COMMENT", "LAST_OPERATOR", "LAST_UPDATE", "NATURE", "NONBLOCKING_ALERTS", "PRIORITY", "RECORD", "RECORD_ID", "RECORD_LOCK", "SIGN_KEY", "SIGN_KEY2", "TYPE_OF_REC", "UNIT", "PREV_DECISION_TYPE", "FMT_NAME") AS
SELECT 
SYSTEM_ID, BATCH_ID, LAST_DEC_BATCH_ID, BLOCKING_ALERTS, BUNIT, CHAR_SEP, CONFIDENTIALITY, CREATED, DB_ACCOUNT, DB_CITY, DB_COUNTRY, DB_DOB, DB_NAME, DB_POB,
DB_STATE, DB_USER1, DB_USER2, DECISION_TYPE, FILTERED, FROM_APPLI, HOLDER, LAST_COMMENT, LAST_OPERATOR, LAST_UPDATE, NATURE, NONBLOCKING_ALERTS, PRIORITY,
RECORD, RECORD_ID, RECORD_LOCK, SIGN_KEY, SIGN_KEY2, TYPE_OF_REC, UNIT, PREV_DECISION_TYPE,
CASE
  WHEN FMT_NAME IN (
    'SCB_DUDL_CDD_V1',
    'SCB_DUDL_CDDR_V1',
    'SCB_DUDL_SME_V1',
    'SCB_DUDL_SMER_V1'
  ) THEN
  'SCB_EDMP_ADVM_2'
  WHEN FMT_NAME IN (
    'SCB_DUED_CDD_V1',
    'SCB_DUED_CDDR_V1',
    'SCB_DUED_SME_V1',
    'SCB_DUED_SMER_V1'
  ) THEN
  'SCB_EDMP_DUED_2'
  ELSE FMT_NAME
END AS FMT_NAME
FROM FFF_RECORDS
WHERE (SYSTEM_ID,BATCH_ID) IN (SELECT  SYSTEM_ID,BATCH_ID FROM CBS_HITS_DETAILS_S8_SERP_V)
AND UPPER(TRIM(UNIT)) NOT LIKE '%DENY%';


--------------------------------------------------------
--  DDL for Index PK_CBS_HITS_DETAILS
--------------------------------------------------------

  CREATE UNIQUE INDEX "LC_GNS_WEB_SIT_01"."PK_CBS_HITS_DETAILS" ON "LC_GNS_WEB_SIT_01"."CBS_HITS_DETAILS" ("BATCH_ID", "SYSTEM_ID", "HIT_UNIQ_ID") 
  TABLESPACE "LC_GNS_SIT_DATA"  ;
--------------------------------------------------------
--  DDL for Index PK_CBS_S8_READ_LOG
--------------------------------------------------------

  CREATE UNIQUE INDEX "LC_GNS_WEB_SIT_01"."PK_CBS_S8_READ_LOG" ON "LC_GNS_WEB_SIT_01"."CBS_S8_READ_LOG" ("BATCH_ID", "SYSTEM_ID", "HIT_UNIQ_ID") 
  TABLESPACE "LC_GNS_SIT_DATA" ;
--------------------------------------------------------
--  DDL for Index FDEC_SYSID
--------------------------------------------------------

  CREATE INDEX "LC_GNS_WEB_SIT_01"."FDEC_SYSID" ON "LC_GNS_WEB_SIT_01"."FFF_DECISIONS" ("SYSTEM_ID") 
  TABLESPACE "LC_GNS_SIT_DATA" ;
--------------------------------------------------------
--  DDL for Index FFFDEC_SYSID_DECDATE_TYPE_OPE
--------------------------------------------------------

  CREATE INDEX "LC_GNS_WEB_SIT_01"."FFFDEC_SYSID_DECDATE_TYPE_OPE" ON "LC_GNS_WEB_SIT_01"."FFF_DECISIONS" ("SYSTEM_ID", "DECISION_DATE", "TYPE", "OPERATOR") 
  TABLESPACE "LC_GNS_SIT_DATA" ;
--------------------------------------------------------
--  DDL for Index FDEC_OPER
--------------------------------------------------------

  CREATE INDEX "LC_GNS_WEB_SIT_01"."FDEC_OPER" ON "LC_GNS_WEB_SIT_01"."FFF_DECISIONS" ("OPERATOR") 
  TABLESPACE "LC_GNS_SIT_DATA" ;
--------------------------------------------------------
--  DDL for Index FFFHISTBATCH_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "LC_GNS_WEB_SIT_01"."FFFHISTBATCH_PK" ON "LC_GNS_WEB_SIT_01"."FFF_HISTBATCH" ("BATCH_ID") 
  TABLESPACE "LC_GNS_SIT_DATA" ;
--------------------------------------------------------
--  DDL for Index FBAT_UNIT
--------------------------------------------------------

  CREATE INDEX "LC_GNS_WEB_SIT_01"."FBAT_UNIT" ON "LC_GNS_WEB_SIT_01"."FFF_HISTBATCH" ("UNIT") 
  TABLESPACE "LC_GNS_SIT_DATA" ;
--------------------------------------------------------
--  DDL for Index FHCO_SYSID
--------------------------------------------------------

  CREATE INDEX "LC_GNS_WEB_SIT_01"."FHCO_SYSID" ON "LC_GNS_WEB_SIT_01"."FFF_HITS_COMMENTS" ("SYSTEM_ID") 
  TABLESPACE "LC_GNS_SIT_DATA" ;
--------------------------------------------------------
--  DDL for Index FHCO_HITID
--------------------------------------------------------

  CREATE INDEX "LC_GNS_WEB_SIT_01"."FHCO_HITID" ON "LC_GNS_WEB_SIT_01"."FFF_HITS_COMMENTS" ("HIT_ID") 
  TABLESPACE "LC_GNS_SIT_DATA" ;
--------------------------------------------------------
--  DDL for Index FHCO_OPER
--------------------------------------------------------

  CREATE INDEX "LC_GNS_WEB_SIT_01"."FHCO_OPER" ON "LC_GNS_WEB_SIT_01"."FFF_HITS_COMMENTS" ("OPERATOR") 
  TABLESPACE "LC_GNS_SIT_DATA" ;
--------------------------------------------------------
--  DDL for Index FFFHITDETAILS_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "LC_GNS_WEB_SIT_01"."FFFHITDETAILS_PK" ON "LC_GNS_WEB_SIT_01"."FFF_HITS_DETAILS" ("SYSTEM_ID") 
  TABLESPACE "LC_GNS_SIT_DATA" ;
--------------------------------------------------------
--  DDL for Index FREC_MAIN
--------------------------------------------------------

  CREATE INDEX "LC_GNS_WEB_SIT_01"."FREC_MAIN" ON "LC_GNS_WEB_SIT_01"."FFF_RECORDS" ("UNIT", "DECISION_TYPE") 
  TABLESPACE "LC_GNS_SIT_DATA" ;
--------------------------------------------------------
--  DDL for Index FREC_LASTCOMM
--------------------------------------------------------

  CREATE INDEX "LC_GNS_WEB_SIT_01"."FREC_LASTCOMM" ON "LC_GNS_WEB_SIT_01"."FFF_RECORDS" ("LAST_COMMENT") 
  TABLESPACE "LC_GNS_SIT_DATA" ;
--------------------------------------------------------
--  DDL for Index FREC_DECTYPCREA
--------------------------------------------------------

  CREATE INDEX "LC_GNS_WEB_SIT_01"."FREC_DECTYPCREA" ON "LC_GNS_WEB_SIT_01"."FFF_RECORDS" ("DECISION_TYPE", "CREATED") 
  TABLESPACE "LC_GNS_SIT_DATA" ;
--------------------------------------------------------
--  DDL for Index FREC_FILTRED
--------------------------------------------------------

  CREATE INDEX "LC_GNS_WEB_SIT_01"."FREC_FILTRED" ON "LC_GNS_WEB_SIT_01"."FFF_RECORDS" ("FILTERED") 
  TABLESPACE "LC_GNS_SIT_DATA" ;
--------------------------------------------------------
--  DDL for Index FREC_PDTYPE
--------------------------------------------------------

  CREATE INDEX "LC_GNS_WEB_SIT_01"."FREC_PDTYPE" ON "LC_GNS_WEB_SIT_01"."FFF_RECORDS" ("PREV_DECISION_TYPE") 
  TABLESPACE "LC_GNS_SIT_DATA" ;
--------------------------------------------------------
--  DDL for Index FREC_BUNIT
--------------------------------------------------------

  CREATE INDEX "LC_GNS_WEB_SIT_01"."FREC_BUNIT" ON "LC_GNS_WEB_SIT_01"."FFF_RECORDS" ("BUNIT") 
  TABLESPACE "LC_GNS_SIT_DATA" ;
--------------------------------------------------------
--  DDL for Index FREC_UNIT
--------------------------------------------------------

  CREATE INDEX "LC_GNS_WEB_SIT_01"."FREC_UNIT" ON "LC_GNS_WEB_SIT_01"."FFF_RECORDS" ("UNIT") 
  TABLESPACE "LC_GNS_SIT_DATA" ;
--------------------------------------------------------
--  DDL for Index FREC_BATCH_PDTYPE
--------------------------------------------------------

  CREATE INDEX "LC_GNS_WEB_SIT_01"."FREC_BATCH_PDTYPE" ON "LC_GNS_WEB_SIT_01"."FFF_RECORDS" ("BATCH_ID", "PREV_DECISION_TYPE") 
  TABLESPACE "LC_GNS_SIT_DATA" ;
--------------------------------------------------------
--  DDL for Index FREC_UNHOLDECTYPE
--------------------------------------------------------

  CREATE INDEX "LC_GNS_WEB_SIT_01"."FREC_UNHOLDECTYPE" ON "LC_GNS_WEB_SIT_01"."FFF_RECORDS" ("UNIT", "HOLDER", "DECISION_TYPE") 
  TABLESPACE "LC_GNS_SIT_DATA" ;
--------------------------------------------------------
--  DDL for Index FREC_RECID
--------------------------------------------------------

  CREATE INDEX "LC_GNS_WEB_SIT_01"."FREC_RECID" ON "LC_GNS_WEB_SIT_01"."FFF_RECORDS" ("RECORD_ID") 
  TABLESPACE "LC_GNS_SIT_DATA" ;
--------------------------------------------------------
--  DDL for Index FREC_DTYPE
--------------------------------------------------------

  CREATE INDEX "LC_GNS_WEB_SIT_01"."FREC_DTYPE" ON "LC_GNS_WEB_SIT_01"."FFF_RECORDS" ("DECISION_TYPE") 
  TABLESPACE "LC_GNS_SIT_DATA" ;
--------------------------------------------------------
--  DDL for Index FREC_UNLOCK
--------------------------------------------------------

  CREATE INDEX "LC_GNS_WEB_SIT_01"."FREC_UNLOCK" ON "LC_GNS_WEB_SIT_01"."FFF_RECORDS" ("RECORD_LOCK", "HOLDER", "UNIT") 
  TABLESPACE "LC_GNS_SIT_DATA" ;
--------------------------------------------------------
--  DDL for Index FFFRECORDS_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "LC_GNS_WEB_SIT_01"."FFFRECORDS_PK" ON "LC_GNS_WEB_SIT_01"."FFF_RECORDS" ("SYSTEM_ID") 
  TABLESPACE "LC_GNS_SIT_DATA" ;
--------------------------------------------------------
--  DDL for Index FREC_BATCH_DTYPE
--------------------------------------------------------

  CREATE INDEX "LC_GNS_WEB_SIT_01"."FREC_BATCH_DTYPE" ON "LC_GNS_WEB_SIT_01"."FFF_RECORDS" ("BATCH_ID", "DECISION_TYPE", "PREV_DECISION_TYPE") 
  TABLESPACE "LC_GNS_SIT_DATA" ;
--------------------------------------------------------
--  DDL for Index FREC_CNTRY
--------------------------------------------------------

  CREATE INDEX "LC_GNS_WEB_SIT_01"."FREC_CNTRY" ON "LC_GNS_WEB_SIT_01"."FFF_RECORDS" ("DB_COUNTRY") 
  TABLESPACE "LC_GNS_SIT_DATA" ;
--------------------------------------------------------
--  DDL for Index FREC_CREATED
--------------------------------------------------------

  CREATE INDEX "LC_GNS_WEB_SIT_01"."FREC_CREATED" ON "LC_GNS_WEB_SIT_01"."FFF_RECORDS" ("CREATED") 
  TABLESPACE "LC_GNS_SIT_DATA" ;
--------------------------------------------------------
--  DDL for Index FREC_PRIORITY
--------------------------------------------------------

  CREATE INDEX "LC_GNS_WEB_SIT_01"."FREC_PRIORITY" ON "LC_GNS_WEB_SIT_01"."FFF_RECORDS" ("PRIORITY") 
  TABLESPACE "LC_GNS_SIT_DATA" ;
--------------------------------------------------------
--  DDL for Index FHIT_SYSID
--------------------------------------------------------

  CREATE INDEX "LC_GNS_WEB_SIT_01"."FHIT_SYSID" ON "LC_GNS_WEB_SIT_01"."FOF_HITS" ("SYSTEM_ID") 
  TABLESPACE "LC_GNS_SIT_DATA" ;
--------------------------------------------------------
--  DDL for Index FHIT_TYPE
--------------------------------------------------------

  CREATE INDEX "LC_GNS_WEB_SIT_01"."FHIT_TYPE" ON "LC_GNS_WEB_SIT_01"."FOF_HITS" ("TYPE") 
  TABLESPACE "LC_GNS_SIT_DATA" ;
--------------------------------------------------------
--  DDL for Index FHIT_ORIGIN
--------------------------------------------------------

  CREATE INDEX "LC_GNS_WEB_SIT_01"."FHIT_ORIGIN" ON "LC_GNS_WEB_SIT_01"."FOF_HITS" ("ORIGIN") 
  TABLESPACE "LC_GNS_SIT_DATA" ;
--------------------------------------------------------
--  DDL for Index FHIT_DESIGN
--------------------------------------------------------

  CREATE INDEX "LC_GNS_WEB_SIT_01"."FHIT_DESIGN" ON "LC_GNS_WEB_SIT_01"."FOF_HITS" ("DESIGNATION") 
  TABLESPACE "LC_GNS_SIT_DATA" ;
--------------------------------------------------------
--  DDL for Index UK_STATE_NAME
--------------------------------------------------------

  CREATE UNIQUE INDEX "LC_GNS_WEB_SIT_01"."UK_STATE_NAME" ON "LC_GNS_WEB_SIT_01"."FOF_STATES" ("STATE_NAME") 
  TABLESPACE "LC_GNS_SIT_DATA" ;
--------------------------------------------------------
--  DDL for Index FOFSTATES_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "LC_GNS_WEB_SIT_01"."FOFSTATES_PK" ON "LC_GNS_WEB_SIT_01"."FOF_STATES" ("STATE_ID") 
  TABLESPACE "LC_GNS_SIT_DATA" ;
--------------------------------------------------------
--  DDL for Index UK_STATE_LABEL
--------------------------------------------------------

  CREATE UNIQUE INDEX "LC_GNS_WEB_SIT_01"."UK_STATE_LABEL" ON "LC_GNS_WEB_SIT_01"."FOF_STATES" ("LABEL") 
  TABLESPACE "LC_GNS_SIT_DATA" ;

--------------------------------------------------------
--  DDL for Package CBS_INTERFACE_PKG
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE "LC_GNS_WEB_SIT_01"."CBS_INTERFACE_PKG" AS

FUNCTION F_CBS_S8_LOG_ACK(P_SOURCE_APPLN VARCHAR2, P_SYSTEM_ID VARCHAR2, P_BATCH_ID VARCHAR2) RETURN VARCHAR2;
FUNCTION F_CBS_S8_LOG_RECOM
(
    P_SOURCE_APPLN                  VARCHAR2,
    P_SYSTEM_ID                     VARCHAR2,
    P_BATCH_ID                      VARCHAR2,
    P_HIT_WATCHLIST_ID              VARCHAR2,
    P_HIT_RECOMMENDED_STATUS        VARCHAR2,
    P_HIT_RECOMMENDED_COMMENTS      CLOB,
    P_LIST_RECOMMENDED_STATUS       VARCHAR2,
    P_LIST_RECOMMENDED_COMMENTS     CLOB
) RETURN VARCHAR2;
END CBS_INTERFACE_PKG;

/
--------------------------------------------------------
--  DDL for Package Body CBS_INTERFACE_PKG
--------------------------------------------------------

  CREATE OR REPLACE PACKAGE BODY "LC_GNS_WEB_SIT_01"."CBS_INTERFACE_PKG" AS


FUNCTION F_CBS_S8_LOG_ACK
(
    P_SOURCE_APPLN          IN VARCHAR2,
    P_SYSTEM_ID             IN VARCHAR2,
    P_BATCH_ID              IN VARCHAR2
)
    RETURN VARCHAR2 AS
    PRAGMA AUTONOMOUS_TRANSACTION;
var_count number:=0;
var_result number:=0;
BEGIN

    IF p_source_appln NOT IN ('SA', 'S8') THEN
        RETURN '001';
    END IF;

    SELECT count(*)
    into var_count
    from cbs_s8_read_log logs
    where logs.system_id = p_system_id
      and logs.batch_id = p_batch_id
      and logs.source_appln = P_SOURCE_APPLN;

    IF var_count = 0 THEN
        INSERT INTO cbs_s8_read_log(source_appln, gns_run_date, system_id, batch_id, hit_uniq_id,
                                    hit_watchlist_id, S8_READ_DATE, STATUS_FLAG)
        select P_SOURCE_APPLN,
               gns_run_date,
               system_id,
               batch_id,
               hit_uniq_id,
               hit_watchlist_id,
               to_char(sysdate, 'DD-MON-YYYY HH24:MI:SS'),
               'R'
        from cbs_hits_details
        where system_id = p_system_id
          and batch_id = p_batch_id;
        var_result := sql%rowcount;

        if var_result = 0 then
            return '001';
        else
            commit;
            RETURN '000';
        end if;

    ELSE
        RETURN '002';
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        RETURN '001';
END F_CBS_S8_LOG_ACK;


FUNCTION F_CBS_S8_LOG_RECOM
(
    P_SOURCE_APPLN                  IN VARCHAR2,
    P_SYSTEM_ID                     IN VARCHAR2,
    P_BATCH_ID                      IN VARCHAR2,
    P_HIT_WATCHLIST_ID              IN VARCHAR2,
    P_HIT_RECOMMENDED_STATUS        IN VARCHAR2,
    P_HIT_RECOMMENDED_COMMENTS      IN CLOB,
    P_LIST_RECOMMENDED_STATUS       IN VARCHAR2,
    P_LIST_RECOMMENDED_COMMENTS     IN CLOB
)
    RETURN VARCHAR2 AS
    PRAGMA AUTONOMOUS_TRANSACTION;
var_count number:=0;
V_STATUS_FLAG VARCHAR2(10);
V_LIST_RECOM_STATUS VARCHAR2(1000);
V_ACK_STATUS varchar2(1000);
BEGIN

    IF p_source_appln NOT IN ('SA', 'S8') THEN
        RETURN '001';
    END IF;

    BEGIN
        SELECT distinct STATUS_FLAG
        INTO V_STATUS_FLAG
        FROM CBS_S8_READ_LOG
        WHERE BATCH_ID = P_BATCH_ID
          AND SYSTEM_ID = P_SYSTEM_ID
          AND HIT_WATCHLIST_ID = P_HIT_WATCHLIST_ID
          AND SOURCE_APPLN = P_SOURCE_APPLN;
    EXCEPTION
        WHEN OTHERS THEN
            BEGIN
                SELECT CBS_INTERFACE_PKG.F_CBS_S8_LOG_ACK(P_SOURCE_APPLN, P_SYSTEM_ID, P_BATCH_ID)
                INTO V_ACK_STATUS
                FROM DUAL;
                IF V_ACK_STATUS IN ('000', '002') THEN
                    Update CBS_S8_READ_LOG
                    SET HIT_RECOMMENDED_STATUS=P_HIT_RECOMMENDED_STATUS,
                        HIT_RECOMMENDED_COMMENTS=P_HIT_RECOMMENDED_COMMENTS,
                        LIST_RECOMMENDED_STATUS=P_LIST_RECOMMENDED_STATUS,
                        LIST_RECOMMENDED_COMMENTS=P_LIST_RECOMMENDED_COMMENTS,
                        S8_RECOMENDATION_DATE=to_char(sysdate, 'DD-MON-YYYY HH24:MI:SS'),
                        STATUS_FLAG='C'
                    WHERE SYSTEM_ID = P_SYSTEM_ID
                      AND BATCH_ID = P_BATCH_ID
                      AND HIT_WATCHLIST_ID = P_HIT_WATCHLIST_ID
                      AND SOURCE_APPLN = P_SOURCE_APPLN;
                    COMMIT;
                    RETURN '000';
                ELSIF V_ACK_STATUS = '001' THEN
                    RETURN '001';
                END IF;
            EXCEPTION
                WHEN OTHERS THEN
                    RETURN '001';
            END;
    END;

    IF V_STATUS_FLAG = 'R' THEN
        Update CBS_S8_READ_LOG
        SET HIT_RECOMMENDED_STATUS=P_HIT_RECOMMENDED_STATUS,
            HIT_RECOMMENDED_COMMENTS=P_HIT_RECOMMENDED_COMMENTS,
            LIST_RECOMMENDED_STATUS=P_LIST_RECOMMENDED_STATUS,
            LIST_RECOMMENDED_COMMENTS=P_LIST_RECOMMENDED_COMMENTS,
            S8_RECOMENDATION_DATE=to_char(sysdate, 'DD-MON-YYYY HH24:MI:SS'),
            STATUS_FLAG='C'
        WHERE SYSTEM_ID = P_SYSTEM_ID
          AND BATCH_ID = P_BATCH_ID
          AND HIT_WATCHLIST_ID = P_HIT_WATCHLIST_ID
          AND SOURCE_APPLN = P_SOURCE_APPLN;
        commit;
        RETURN '000';
    ELSIF V_STATUS_FLAG = 'C' THEN

        SELECT DISTINCT LIST_RECOMMENDED_STATUS
        INTO V_LIST_RECOM_STATUS
        FROM cbs_s8_read_log
        WHERE BATCH_ID = P_BATCH_ID
          AND SYSTEM_ID = P_SYSTEM_ID
          AND HIT_WATCHLIST_ID = P_HIT_WATCHLIST_ID
          AND SOURCE_APPLN = P_SOURCE_APPLN;

        IF V_LIST_RECOM_STATUS = 'DEFAULT_MANUAL' THEN
            RETURN '003';
        END IF;
        RETURN '002';
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        RETURN '001';
END F_CBS_S8_LOG_RECOM;


END CBS_INTERFACE_PKG;

/
--------------------------------------------------------
--  DDL for Function F_CBS_S8_LOG_ACK
--------------------------------------------------------

  CREATE OR REPLACE FUNCTION "LC_GNS_WEB_SIT_01"."F_CBS_S8_LOG_ACK" 
(
P_SYSTEM_ID				IN VARCHAR2,
P_BATCH_ID				IN VARCHAR2,
P_SOURCE_APP			IN VARCHAR2
)
RETURN NUMBER AS
PRAGMA AUTONOMOUS_TRANSACTION;
var_count number:=0;
var_result number:=0;
BEGIN
SELECT count(*) into var_count 
from cbs_s8_read_log logs
where logs.system_id= p_system_id
and logs.batch_id= p_batch_id;


IF (var_count=0) THEN
INSERT INTO cbs_s8_read_log(gns_run_date, system_id,batch_id,hit_uniq_id,hit_watchlist_id,S8_READ_DATE,STATUS_FLAG)
select gns_run_date,system_id,batch_id,hit_uniq_id,hit_watchlist_id,to_char(sysdate,'DD-MON-YYYY HH24:MI:SS'),'R'
from cbs_hits_details 
where system_id= p_system_id
and batch_id= p_batch_id;
var_result:=sql%rowcount;

if var_result=0 then
return 1;
else
commit;
RETURN 0;
end if;

ELSE
RETURN 2;
END IF;
EXCEPTION
WHEN OTHERS THEN
RETURN 1;
END F_CBS_S8_LOG_ACK;

/
--------------------------------------------------------
--  DDL for Function F_CBS_S8_LOG_RECOM
--------------------------------------------------------

  CREATE OR REPLACE FUNCTION "LC_GNS_WEB_SIT_01"."F_CBS_S8_LOG_RECOM" 
(
P_SYSTEM_ID 						IN VARCHAR2,
P_BATCH_ID	  					IN VARCHAR2,
P_HIT_WATCHLIST_ID	 			IN VARCHAR2,
P_HIT_RECOMMENDED_STATUS		IN VARCHAR2,
P_HIT_RECOMMENDED_COMMENTS		IN VARCHAR2,
P_LIST_RECOMMENDED_STATUS		IN VARCHAR2,
P_LIST_RECOMMENDED_COMMENTS		IN VARCHAR2,
P_LIST_RECOMMENDED_COMMENTS1	IN VARCHAR2
)
RETURN NUMBER AS
PRAGMA AUTONOMOUS_TRANSACTION;
var_count number:=0;
V_STATUS_FLAG VARCHAR2(10);
BEGIN

BEGIN
SELECT distinct STATUS_FLAG INTO V_STATUS_FLAG FROM CBS_S8_READ_LOG 
WHERE BATCH_ID=P_BATCH_ID
AND SYSTEM_ID=P_SYSTEM_ID
AND HIT_WATCHLIST_ID=P_HIT_WATCHLIST_ID;
EXCEPTION
WHEN OTHERS THEN
RETURN 1;
END;


--IF p_source_appln NOT IN ('SA','S8') THEN
--  RETURN 1;
--END IF;

IF V_STATUS_FLAG='R' THEN
Update CBS_S8_READ_LOG
SET HIT_RECOMMENDED_STATUS=P_HIT_RECOMMENDED_STATUS,
HIT_RECOMMENDED_COMMENTS=P_HIT_RECOMMENDED_COMMENTS,
LIST_RECOMMENDED_STATUS=P_LIST_RECOMMENDED_STATUS,
LIST_RECOMMENDED_COMMENTS=P_LIST_RECOMMENDED_COMMENTS,
S8_RECOMENDATION_DATE=to_char(sysdate,'DD-MON-YYYY HH24:MI:SS'),
STATUS_FLAG='C'
WHERE SYSTEM_ID=P_SYSTEM_ID
AND BATCH_ID=P_BATCH_ID
AND HIT_WATCHLIST_ID=P_HIT_WATCHLIST_ID;

commit;
RETURN 0;
ELSIF V_STATUS_FLAG='C' THEN
RETURN 2;
END IF;
EXCEPTION
WHEN OTHERS THEN
RETURN 1;
END F_CBS_S8_LOG_RECOM;

/
--------------------------------------------------------
--  DDL for Synonymn FOF STATES
--------------------------------------------------------

  CREATE OR REPLACE SYNONYM "LC_GNS_WEB_SIT_01"."FOF STATES" FOR "LC_GNS_WEB_SIT_01"."FOF_STATES";
--------------------------------------------------------
--  Constraints for Table CBS_HITS_DETAILS
--------------------------------------------------------

  ALTER TABLE "LC_GNS_WEB_SIT_01"."CBS_HITS_DETAILS" MODIFY ("CBS_HIT_DETAILS_ID" NOT NULL ENABLE);
  ALTER TABLE "LC_GNS_WEB_SIT_01"."CBS_HITS_DETAILS" MODIFY ("CBS_HISTBATCH_ID" NOT NULL ENABLE);
  ALTER TABLE "LC_GNS_WEB_SIT_01"."CBS_HITS_DETAILS" MODIFY ("SYSTEM_ID" NOT NULL ENABLE);
  ALTER TABLE "LC_GNS_WEB_SIT_01"."CBS_HITS_DETAILS" MODIFY ("BATCH_ID" NOT NULL ENABLE);
  ALTER TABLE "LC_GNS_WEB_SIT_01"."CBS_HITS_DETAILS" MODIFY ("GNS_RUN_DATE" NOT NULL ENABLE);
  ALTER TABLE "LC_GNS_WEB_SIT_01"."CBS_HITS_DETAILS" MODIFY ("HIT_UNIQ_ID" NOT NULL ENABLE);
  ALTER TABLE "LC_GNS_WEB_SIT_01"."CBS_HITS_DETAILS" MODIFY ("HIT_WATCHLIST_ID" NOT NULL ENABLE);
  ALTER TABLE "LC_GNS_WEB_SIT_01"."CBS_HITS_DETAILS" ADD CONSTRAINT "PK_CBS_HITS_DETAILS" PRIMARY KEY ("BATCH_ID", "SYSTEM_ID", "HIT_UNIQ_ID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "LC_GNS_SIT_DATA"  ENABLE;
--------------------------------------------------------
--  Constraints for Table CBS_S8_READ_LOG
--------------------------------------------------------

  ALTER TABLE "LC_GNS_WEB_SIT_01"."CBS_S8_READ_LOG" MODIFY ("GNS_RUN_DATE" NOT NULL ENABLE);
  ALTER TABLE "LC_GNS_WEB_SIT_01"."CBS_S8_READ_LOG" MODIFY ("SYSTEM_ID" NOT NULL ENABLE);
  ALTER TABLE "LC_GNS_WEB_SIT_01"."CBS_S8_READ_LOG" MODIFY ("BATCH_ID" NOT NULL ENABLE);
  ALTER TABLE "LC_GNS_WEB_SIT_01"."CBS_S8_READ_LOG" MODIFY ("HIT_WATCHLIST_ID" NOT NULL ENABLE);
  ALTER TABLE "LC_GNS_WEB_SIT_01"."CBS_S8_READ_LOG" MODIFY ("HIT_UNIQ_ID" NOT NULL ENABLE);
  ALTER TABLE "LC_GNS_WEB_SIT_01"."CBS_S8_READ_LOG" ADD CONSTRAINT "PK_CBS_S8_READ_LOG" PRIMARY KEY ("BATCH_ID", "SYSTEM_ID", "HIT_UNIQ_ID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 COMPUTE STATISTICS 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "LC_GNS_SIT_DATA"  ENABLE;
--------------------------------------------------------
--  Constraints for Table FFF_DECISIONS
--------------------------------------------------------

  ALTER TABLE "LC_GNS_WEB_SIT_01"."FFF_DECISIONS" MODIFY ("SYSTEM_ID" NOT NULL ENABLE);
  ALTER TABLE "LC_GNS_WEB_SIT_01"."FFF_DECISIONS" MODIFY ("DECISION_DATE" NOT NULL ENABLE);
  ALTER TABLE "LC_GNS_WEB_SIT_01"."FFF_DECISIONS" MODIFY ("TYPE" NOT NULL ENABLE);
  ALTER TABLE "LC_GNS_WEB_SIT_01"."FFF_DECISIONS" MODIFY ("OPERATOR" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table FFF_HISTBATCH
--------------------------------------------------------

  ALTER TABLE "LC_GNS_WEB_SIT_01"."FFF_HISTBATCH" MODIFY ("BATCH_ID" NOT NULL ENABLE);
  ALTER TABLE "LC_GNS_WEB_SIT_01"."FFF_HISTBATCH" ADD CONSTRAINT "FFFHISTBATCH_PK" PRIMARY KEY ("BATCH_ID")
  USING INDEX "LC_GNS_WEB_SIT_01"."FFFHISTBATCH_PK"  ENABLE;
--------------------------------------------------------
--  Constraints for Table FFF_HITS_COMMENTS
--------------------------------------------------------

  ALTER TABLE "LC_GNS_WEB_SIT_01"."FFF_HITS_COMMENTS" MODIFY ("SYSTEM_ID" NOT NULL ENABLE);
  ALTER TABLE "LC_GNS_WEB_SIT_01"."FFF_HITS_COMMENTS" MODIFY ("HIT_ID" NOT NULL ENABLE);
  ALTER TABLE "LC_GNS_WEB_SIT_01"."FFF_HITS_COMMENTS" MODIFY ("HIT_POSITIONS" NOT NULL ENABLE);
  ALTER TABLE "LC_GNS_WEB_SIT_01"."FFF_HITS_COMMENTS" MODIFY ("OPERATOR" NOT NULL ENABLE);
  ALTER TABLE "LC_GNS_WEB_SIT_01"."FFF_HITS_COMMENTS" MODIFY ("HIT_COMMENT" NOT NULL ENABLE);
  ALTER TABLE "LC_GNS_WEB_SIT_01"."FFF_HITS_COMMENTS" MODIFY ("IS_TRUE_HIT" NOT NULL ENABLE);
  ALTER TABLE "LC_GNS_WEB_SIT_01"."FFF_HITS_COMMENTS" MODIFY ("COMMENT_DATE" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table FFF_HITS_DETAILS
--------------------------------------------------------

  ALTER TABLE "LC_GNS_WEB_SIT_01"."FFF_HITS_DETAILS" MODIFY ("SYSTEM_ID" NOT NULL ENABLE);
  ALTER TABLE "LC_GNS_WEB_SIT_01"."FFF_HITS_DETAILS" MODIFY ("DETAILS" NOT NULL ENABLE);
  ALTER TABLE "LC_GNS_WEB_SIT_01"."FFF_HITS_DETAILS" ADD CONSTRAINT "FFFHITDETAILS_PK" PRIMARY KEY ("SYSTEM_ID")
  USING INDEX "LC_GNS_WEB_SIT_01"."FFFHITDETAILS_PK"  ENABLE;
--------------------------------------------------------
--  Constraints for Table FFF_RECORDS
--------------------------------------------------------

  ALTER TABLE "LC_GNS_WEB_SIT_01"."FFF_RECORDS" MODIFY ("UNIT" NOT NULL ENABLE);
  ALTER TABLE "LC_GNS_WEB_SIT_01"."FFF_RECORDS" MODIFY ("PREV_DECISION_TYPE" NOT NULL ENABLE);
  ALTER TABLE "LC_GNS_WEB_SIT_01"."FFF_RECORDS" ADD CONSTRAINT "FFFRECORDS_PK" PRIMARY KEY ("SYSTEM_ID")
  USING INDEX "LC_GNS_WEB_SIT_01"."FFFRECORDS_PK"  ENABLE;
  ALTER TABLE "LC_GNS_WEB_SIT_01"."FFF_RECORDS" MODIFY ("SYSTEM_ID" NOT NULL ENABLE);
  ALTER TABLE "LC_GNS_WEB_SIT_01"."FFF_RECORDS" MODIFY ("BLOCKING_ALERTS" NOT NULL ENABLE);
  ALTER TABLE "LC_GNS_WEB_SIT_01"."FFF_RECORDS" MODIFY ("CHAR_SEP" NOT NULL ENABLE);
  ALTER TABLE "LC_GNS_WEB_SIT_01"."FFF_RECORDS" MODIFY ("CONFIDENTIALITY" NOT NULL ENABLE);
  ALTER TABLE "LC_GNS_WEB_SIT_01"."FFF_RECORDS" MODIFY ("DECISION_TYPE" NOT NULL ENABLE);
  ALTER TABLE "LC_GNS_WEB_SIT_01"."FFF_RECORDS" MODIFY ("NONBLOCKING_ALERTS" NOT NULL ENABLE);
  ALTER TABLE "LC_GNS_WEB_SIT_01"."FFF_RECORDS" MODIFY ("PRIORITY" NOT NULL ENABLE);
  ALTER TABLE "LC_GNS_WEB_SIT_01"."FFF_RECORDS" MODIFY ("RECORD" NOT NULL ENABLE);
  ALTER TABLE "LC_GNS_WEB_SIT_01"."FFF_RECORDS" MODIFY ("RECORD_LOCK" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table FOF_HITS
--------------------------------------------------------

  ALTER TABLE "LC_GNS_WEB_SIT_01"."FOF_HITS" MODIFY ("SYSTEM_ID" NOT NULL ENABLE);
  ALTER TABLE "LC_GNS_WEB_SIT_01"."FOF_HITS" MODIFY ("SEQNUMBER" NOT NULL ENABLE);
--------------------------------------------------------
--  Constraints for Table FOF_STATES
--------------------------------------------------------

  ALTER TABLE "LC_GNS_WEB_SIT_01"."FOF_STATES" ADD CONSTRAINT "UK_STATE_NAME" UNIQUE ("STATE_NAME")
  USING INDEX "LC_GNS_WEB_SIT_01"."UK_STATE_NAME"  ENABLE;
  ALTER TABLE "LC_GNS_WEB_SIT_01"."FOF_STATES" ADD CONSTRAINT "UK_STATE_LABEL" UNIQUE ("LABEL")
  USING INDEX "LC_GNS_WEB_SIT_01"."UK_STATE_LABEL"  ENABLE;
  ALTER TABLE "LC_GNS_WEB_SIT_01"."FOF_STATES" MODIFY ("LABEL" NOT NULL ENABLE);
  ALTER TABLE "LC_GNS_WEB_SIT_01"."FOF_STATES" MODIFY ("BEHAVIOUR" NOT NULL ENABLE);
  ALTER TABLE "LC_GNS_WEB_SIT_01"."FOF_STATES" MODIFY ("CONTROL_TRANSITION" NOT NULL ENABLE);
  ALTER TABLE "LC_GNS_WEB_SIT_01"."FOF_STATES" MODIFY ("STATUS" NOT NULL ENABLE);
  ALTER TABLE "LC_GNS_WEB_SIT_01"."FOF_STATES" MODIFY ("DATA_TYPE" NOT NULL ENABLE);
  ALTER TABLE "LC_GNS_WEB_SIT_01"."FOF_STATES" MODIFY ("STATE_NAME" NOT NULL ENABLE);
  ALTER TABLE "LC_GNS_WEB_SIT_01"."FOF_STATES" MODIFY ("STATE_ID" NOT NULL ENABLE);
  ALTER TABLE "LC_GNS_WEB_SIT_01"."FOF_STATES" ADD CONSTRAINT "FOFSTATES_PK" PRIMARY KEY ("STATE_ID")
  USING INDEX "LC_GNS_WEB_SIT_01"."FOFSTATES_PK"  ENABLE;
