from enum import Enum

from etl_pipeline.config import columns_namespace as cn

MATCH_RECORDS = "match_ids"
ALERT_ID = "alert_id"
ACCT_NUM = "ACCT_NUM"
PRTY_ID = "PRTY_ID"
SRC_REF_KEY = "SRC_REF_KEY"
PARTY_ID = "PARTY_ID"
ALERTED_PARTY_NAME = "ALERTED_PARTY_NAME"
PRTY_NM = "PRTY_NM"
PRTY_FST_NM = "PRTY_FST_NM"
PRTY_MDL_NM = "PRTY_MDL_NM"
PRTY_LST_NM = "PRTY_LST_NM"
CONNECTED_FULL_NAME = "CONNECTED_FULL_NAME"
PRTY_TYP = "PRTY_TYP"
DOB_DT = "DOB_DT"
PRTY_CNTRY_OF_BIRTH = "PRTY_CNTRY_OF_BIRTH"
PRTY_PRIM_CTZNSH_CNTRY = "PRTY_PRIM_CTZNSH_CNTRY"
PRTY_RSDNC_CNTRY_CD = "PRTY_RSDNC_CNTRY_CD"
ADDRESS_ID = "ADDRESS_ID"
AD_BNFL_NM = "AD_BNFL_NM"
ADDRESS1_COUNTRY = "ADDRESS1_COUNTRY"
ADDRESS1_LINE1 = "ADDRESS1_LINE1"
ADDRESS1_LINE2 = "ADDRESS1_LINE2"
ADDRESS1_LINE3 = "ADDRESS1_LINE3"
ADDRESS1_LINE4 = "ADDRESS1_LINE4"
ADDRESS1_LINE5 = "ADDRESS1_LINE5"
CONCAT_ADDRESS = "CONCAT_ADDRESS"
ALL_PARTS_NAMES = "all_party_names"
ACCT_REGS_NM1 = "ACCT_REGS_NM1"
CONCAT_RESIDUE = "concat_residue"
CONCAT_ADDRESS_NO_CHANGES = "concat_address_no_changes"
ALL_CONNECTED_PARTIES_NAMES = "all_connected_parties_names"
WL_NAME = "WL_NAME"
WL_STATENAME = "WL_STATENAME"
WL_ADDRESS1 = "WL_ADDRESS1"
WL_ENTITYTYPE = "WL_ENTITYTYPE"
WL_POSTALCODE = "WL_POSTALCODE"
WL_CITY = "WL_CITY"
WL_STATE = "WL_STATE"
WL_MATCHED_TOKENS = cn.WL_MATCHED_TOKENS
WL_ROUTING_CODE_BIC = "WL_ROUTING_CODE_BIC"
WL_ROUTING_CODE_CHIPS_UID = "WL_ROUTING_CODE_CHIPS_UID"
WL_ROUTING_CODE_NATL_ROUTING_CODE = "WL_ROUTING_CODE_NATL_ROUTING_CODE"
WL_ROUTING_CODE_SWIFT = "WL_ROUTING_CODE_SWIFT"
MATCH_INPUT_VERSION_ID = "MATCH_INPUT_VERSION_ID"
AP_TYPE = "AP_TYPE"
ANALYST_DECISION = "ANALYST_DECISION"
WL_NATIONALITY = "WL_NATIONALITY"
WL_POB = "WL_POB"
WL_CITIZENSHIP = "WL_CITIZENSHIP"
WL_COUNTRY = "WL_COUNTRY"
WL_COUNTRYNAME = "WL_COUNTRYNAME"
WL_DOB = "WL_DOB"
WL_ALIASES = "WL_ALIASES"
WL_ENTITYTYPE = "wl_entitytype"
ACCT_CLAS_DESCR = "ACCT_CLAS_DESCR"
WP_TYPE = "wp_type"
STATUS_DESC = "status_desc"
ANALYST_SOLUTION = "analyst_solution"
ALERT_INTERNAL_ID = "ALERT_INTERNAL_ID"
ENTITY_ID = "ENTITY_ID"
ENTITY_VERSION = "ENTITY_VERSION"
MATCH_ID = "match_id"
# temporary
CONNECTED_FULL_NAME = "CONNECTED_FULL_NAME"

SRC_SYS_ACCT_KEY = "SRC_SYS_ACCT_KEY"
TRIGGERED_BY = "TRIGGERED_BY"
ALL_PARTY_DETAILS = "ALL_PARTY_DETAILS"
ALL_PARTY_NAMES = "all_party_names"
ALL_PARTY_DOBS = "all_party_dobs"
ALL_PARTY_BIRTH_COUNTRIES = "all_party_birth_countries"
ALL_PARTY_CITIZENSHIP_COUNTRIES = "all_party_citizenship_countries"
ALL_PARTY_RESIDENCY_COUNTRIES = "all_party_residency_countries"
AD_BNFL_NM = "AD_BNFL_NM"
IS_BENEFICIARY_HIT = "is_beneficiary_hit"
CLEANED_NAMES = "cleaned_names"
AP_TRIGGERS = "AP_TRIGGERS"
DELIVERED_ALERTED_PARTY_TYPE = "derived_alerted_party_type"
NUMBER_OF_HITS = "number_of_hits"
WL_ALL_NAMES_AGGREGATED = "wl_all_names_aggregated"
WL_NAME = "wl_name"
AP_ALL_NAMES_AGGREGATED = "ap_all_names_aggregated"
ACCT_PMH_ADDR_ID = "ACCT_PMH_ADDR_ID"


class AccountType(str, Enum):
    WM_ADDRESS = "WM_ADDRESS"
    ISG_ACCOUNT = "ISG_ACCOUNT"


TAX_ID_TYP_ID = "TAX_ID_TYP_ID"
US_RSDNC_CD = "US_RSDNC_CD"
ORG_NM = "ORG_NM"
CNTRY_OF_INCRP = "CNTRY_OF_INCRP"
BUS_DT = "BUS_DT"
CREA_TS = "CREA_TS"
TAX_ID = "TAX_ID"
EMPLOYER_BUS_CAT = "EMPLOYER_BUS_CAT"
OCCUP_CD = "OCCUP_CD"
PEP_IND = "PEP_IND"
EMPLOYER_NM = "EMPLOYER_NM"
POSITION_TITLE = "POSITION_TITLE"
IND_INVLD_NM = "IND_INVLD_NM"
IND_INVLD_DT_BIRTH = "IND_INVLD_DT_BIRTH"
IND_INVLD_SSN_TAX_ID = "IND_INVLD_SSN_TAX_ID"
EMPMT_STAT = "EMPMT_STAT"
MS_EMPL_STAT = "MS_EMPL_STAT"
GOV_FUND_IND = "GOV_FUND_IND"
PUBLICLY_TRAD_IND = "PUBLICLY_TRAD_IND"
MS_EMPLOYEE_ID = "MS_EMPLOYEE_ID"
MERGING_PARTIES = [
    PARTY_ID,
    PRTY_TYP,
    PRTY_NM,
    PRTY_FST_NM,
    PRTY_LST_NM,
    PRTY_MDL_NM,
    TAX_ID_TYP_ID,
    DOB_DT,
    PRTY_CNTRY_OF_BIRTH,
    PRTY_PRIM_CTZNSH_CNTRY,
    PRTY_RSDNC_CNTRY_CD,
]
MERGING_ACCOUNTS = [PARTY_ID, ADDRESS_ID, SRC_SYS_ACCT_KEY, ACCT_NUM]


PARTIES = "parties"
ACCOUNTS = "accounts"
CONNECTED_FULL_NAME = "CONNECTED_FULL_NAME"
ALL_PARTY_TYPES = "ALL_PARTY_TYPES"
