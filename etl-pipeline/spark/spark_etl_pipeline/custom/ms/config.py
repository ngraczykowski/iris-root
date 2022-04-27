from spark_etl_pipeline.xml_spark.config import XMLExtractorConfig
from spark_etl_pipeline.xml_spark.xml_extractor import XMLFunction

RAW_DATA_DIR = "test_spark/shared/reference/1.raw"
CLEANSED_DATA_DIR = "data/3.cleansed"
STANDARDIZED_DATA_DIR = "data/2.standardized"
APPLICATION_DATA_DIR = "data/4.application"
ALERTS_FILE_NAME = "ALERTS.delta"
ALERT_NOTES_FILE_NAME = "ACM_ALERT_NOTES.delta"
ITEM_STATUS_FILE_NAME = "ACM_ITEM_STATUS_HISTORY.delta"
ALERT_STATUSES_DF_FILE_NAME = "ACM_MD_ALERT_STATUSES.delta"
JOINING_SEP = "====="
CONFIG_PATH = "./config.yaml"


def get_input_path(source, key) -> str or None:
    if key not in source:
        return None

    return source[key]["input-path"]


AIA_HIT_CONFIG: XMLExtractorConfig = {
    "hit_listId": [XMLFunction.extract_value, 'elem[@name="listId"]'],
    "hit_entryId": [XMLFunction.extract_value, 'elem[@name="entryId"]'],
    "hit_listVersion": [XMLFunction.extract_value, 'elem[@name="listVersion"]'],
    "hit_entryType": [XMLFunction.extract_value, 'elem[@name="entryType"]'],
    "hit_listUpdateDate": [XMLFunction.extract_value, 'elem[@name="listUpdateDate"]'],
    "hit_entryCreatedDate": [XMLFunction.extract_value, 'elem[@name="entryCreatedDate"]'],
    "hit_entryUpdateDate": [XMLFunction.extract_value, 'elem[@name="entryUpdateDate"]'],
    "hit_displayName": [XMLFunction.extract_value, 'elem[@name="displayName"]'],
    "hit_matchedName": [XMLFunction.extract_value, 'elem[@name="matchedName"]'],
    "hit_isNameBroken": [XMLFunction.extract_boolean_value, 'elem[@name="isNameBroken"]'],
    "hit_aliases_displayName": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="aliases"]/elem[@name="displayName"]',
    ],
    "hit_aliases_matchedName": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="aliases"]/elem[@name="matchedName"]',
    ],
    "hit_aliases_isNameBroken": [
        XMLFunction.extract_boolean_array,
        'elem[@name="aliases"]/elem[@name="isNameBroken"]',
    ],
    "hit_aliases_matchStrength": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="aliases"]/elem[@name="matchStrength"]',
    ],
    "hit_addresses_streetAddress1": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="addresses"]/elem[@name="streetAddress1"]',
    ],
    "hit_addresses_streetAddress2": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="addresses"]/elem[@name="streetAddress2"]',
    ],
    "hit_addresses_city": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="addresses"]/elem[@name="city"]',
    ],
    "hit_addresses_stateProvince": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="addresses"]/elem[@name="stateProvince"]',
    ],
    "hit_addresses_postalCode": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="addresses"]/elem[@name="postalCode"]',
    ],
    "hit_addresses_country": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="addresses"]/elem[@name="country"]',
    ],
    "hit_ids_idType": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="ids"]/elem[@name="idType"]',
    ],
    "hit_ids_idNumber": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="ids"]/elem[@name="idNumber"]',
    ],
    "hit_ids_idCountry": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="ids"]/elem[@name="idCountry"]',
    ],
    "hit_nationalityCountries_country": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="nationalityCountries"]/elem[@name="country"]',
    ],
    "hit_placesOfBirth_birthPlace": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="placesOfBirth"]/elem[@name="birthPlace"]',
    ],
    "hit_placesOfBirth_birthCountry": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="placesOfBirth"]/elem[@name="birthCountry"]',
    ],
    "hit_age": [XMLFunction.extract_value, 'elem[@name="age"]'],
    "hit_ageAsOfDate": [XMLFunction.extract_value, 'elem[@name="ageAsOfDate"]'],
    "hit_datesOfBirth_birthDate": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="datesOfBirth"]/elem[@name="birthDate"]',
    ],
    "hit_datesOfBirth_yearOfBirth": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="datesOfBirth"]/elem[@name="yearOfBirth"]',
    ],
    "hit_categories_category": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="categories"]/elem[@name="category"]',
    ],
    "hit_keywords_keyword": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="keywords"]/elem[@name="keyword"]',
    ],
    "hit_title": [XMLFunction.extract_value, 'elem[@name="title"]'],
    "hit_position": [XMLFunction.extract_value, 'elem[@name="position"]'],
    "hit_gender": [XMLFunction.extract_value, 'elem[@name="gender"]'],
    "hit_isDeceased": [XMLFunction.extract_boolean_value, 'elem[@name="isDeceased"]'],
    "hit_deceasedDate": [XMLFunction.extract_value, 'elem[@name="deceasedDate"]'],
    "hit_cs_1": [XMLFunction.extract_value, 'elem[@name="cs_1"]'],
    "hit_cs_2": [XMLFunction.extract_value, 'elem[@name="cs_2"]'],
    "hit_cs_3": [XMLFunction.extract_value, 'elem[@name="cs_3"]'],
    "hit_cs_4": [XMLFunction.extract_value, 'elem[@name="cs_4"]'],
    "hit_cs_5": [XMLFunction.extract_value, 'elem[@name="cs_5"]'],
    "hit_cs_6": [XMLFunction.extract_value, 'elem[@name="cs_6"]'],
    "hit_cs_7": [XMLFunction.extract_value, 'elem[@name="cs_7"]'],
    "hit_cs_8": [XMLFunction.extract_value, 'elem[@name="cs_8"]'],
    "hit_cs_9": [XMLFunction.extract_value, 'elem[@name="cs_9"]'],
    "hit_cs_10": [XMLFunction.extract_value, 'elem[@name="cs_10"]'],
    "hit_cs_11": [XMLFunction.extract_value, 'elem[@name="cs_11"]'],
    "hit_cs_12": [XMLFunction.extract_value, 'elem[@name="cs_12"]'],
    "hit_cs_13": [XMLFunction.extract_value, 'elem[@name="cs_13"]'],
    "hit_cs_14": [XMLFunction.extract_value, 'elem[@name="cs_14"]'],
    "hit_cs_15": [XMLFunction.extract_value, 'elem[@name="cs_15"]'],
    "hit_cs_16": [XMLFunction.extract_value, 'elem[@name="cs_16"]'],
    "hit_cs_17": [XMLFunction.extract_value, 'elem[@name="cs_17"]'],
    "hit_cs_18": [XMLFunction.extract_value, 'elem[@name="cs_18"]'],
    "hit_additionalInfo_name": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="additionalInfo"]/elem[@name="name"]',
    ],
    "hit_additionalInfo_value": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="additionalInfo"]/elem[@name="value"]',
    ],
    "hit_score": [XMLFunction.extract_value, 'elem[@name="score"]'],
    "hit_matchType": [XMLFunction.extract_value, 'elem[@name="matchType"]'],
    "hit_scoreFactors_factorId": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="scoreFactors"]/elem[@name="scoreFactors"]/elem[@name="factorId"]',
    ],
    "hit_scoreFactors_factorDesc": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="scoreFactors"]/elem[@name="scoreFactors"]/elem[@name="factorDesc"]',
    ],
    "hit_scoreFactors_factorValue": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="scoreFactors"]/elem[@name="scoreFactors"]/elem[@name="factorValue"]',
    ],
    "hit_scoreFactors_factorScore": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="scoreFactors"]/elem[@name="scoreFactors"]/elem[@name="factorScore"]',
    ],
    "hit_scoreFactors_factorImpact": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="scoreFactors"]/elem[@name="scoreFactors"]/elem[@name="factorImpact"]',
    ],
    "hit_scoresBreakdown_matchedName": [
        XMLFunction.extract_value,
        'elem[@name="scoresBreakdown"]/elem[@name="matchedName"]',
    ],
    "hit_scoresBreakdown_aliases_matchedName": [
        XMLFunction.extract_value,
        'elem[@name="scoresBreakdown"]/elem[@name="aliases_matchedName"]',
    ],
    "hit_scoresBreakdown_addresses_city": [
        XMLFunction.extract_value,
        'elem[@name="scoresBreakdown"]/elem[@name="addresses_city"]',
    ],
    "hit_scoresBreakdown_addresses_country": [
        XMLFunction.extract_value,
        'elem[@name="scoresBreakdown"]/elem[@name="addresses_country"]',
    ],
    "hit_scoresBreakdown_addresses_stateProvince": [
        XMLFunction.extract_value,
        'elem[@name="scoresBreakdown"]/elem[@name="addresses_stateProvince"]',
    ],
    "hit_scoresBreakdown_ids_idNumber": [
        XMLFunction.extract_value,
        'elem[@name="scoresBreakdown"]/elem[@name="ids_idNumber"]',
    ],
    "hit_explanations_matchedName_Explanation": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="explanations"]/elem[@name="matchedName"]/elem[@name="Explanation"]',
    ],
    "hit_explanations_aliases_matchedName_Explanation": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="explanations"]/elem[@name="aliases_matchedName"]/elem[@name="Explanation"]',
    ],
    "hit_explanations_nationalityCountries_country_Explanation": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="explanations"]/elem[@name="nationalityCountries_country"]/elem[@name="Explanation"]',
    ],
    # <Guessed attributes based on the pattern beginning
    "hit_explanations_address_city_Explanation": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="explanations"]/elem[@name="address_city"]/elem[@name="Explanation"]',
    ],
    "hit_explanations_address_country_Explanation": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="explanations"]/elem[@name="address_country"]/elem[@name="Explanation"]',
    ],
    "hit_explanations_addresses_stateProvince_Explanation": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="explanations"]/elem[@name="addresses_stateProvince"]/elem[@name="Explanation"]',
    ],
    "hit_explanations_ids_idNumber_Explanation": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="explanations"]/elem[@name="ids_idNumber"]/elem[@name="Explanation"]',
    ],
    # Guessed attributes based on the pattern end>
    "hit_inputExplanations_matchedName_inputExplanation": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="inputExplanations"]/elem[@name="matchedName"]/elem[@name="inputExplanation"]',
    ],
    "hit_inputExplanations_aliases_matchedName_inputExplanation": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="inputExplanations"]/elem[@name="aliases_matchedName"]/elem[@name="inputExplanation"]',
    ],
    "hit_inputExplanations_nationalityCountries_country_inputExplanation": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="inputExplanations"]/elem[@name="nationalityCountries_country"]/elem[@name="inputExplanation"]',
    ],
    # <Guessed attributes based on the pattern beginning
    "hit_inputExplanations_address_city_inputExplanation": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="inputExplanations"]/elem[@name="address_city"]/elem[@name="inputExplanation"]',
    ],
    "hit_inputExplanations_address_country_inputExplanation": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="inputExplanations"]/elem[@name="address_country"]/elem[@name="inputExplanation"]',
    ],
    "hit_inputExplanations_addresses_stateProvince_inputExplanation": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="inputExplanations"]/elem[@name="addresses_stateProvince"]/elem[@name="inputExplanation"]',
    ],
    "hit_inputExplanations_ids_idNumber_inputExplanation": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="inputExplanations"]/elem[@name="ids_idNumber"]/elem[@name="inputExplanation"]',
    ],
}


AIA_ALERT_HEADER_CONFIG: XMLExtractorConfig = {
    "alert_alertId": [XMLFunction.extract_value, 'elem[@name="alertId"]'],
    "alert_alertDate": [XMLFunction.extract_value, 'elem[@name="alertDate"]'],
    "alert_alertEntityKey": [XMLFunction.extract_value, 'elem[@name="alertEntityKey"]'],
    "alert_score": [XMLFunction.extract_value, 'elem/elem[@name="score"]'],
    "alert_ahData_alertID": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="ahData"]/elem[@name="alertID"]',
    ],
    "alert_ahData_alertDateTime": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="ahData"]/elem[@name="alertDateTime"]',
    ],
    "alert_ahData_jobID": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="ahData"]/elem[@name="jobID"]',
    ],
    "alert_ahData_jobName": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="ahData"]/elem[@name="jobName"]',
    ],
    "alert_ahData_jobType": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="ahData"]/elem[@name="jobType"]',
    ],
    "alert_ahData_score": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="ahData"]/elem[@name="score"]',
    ],
    "alert_ahData_numberOfHits": [
        XMLFunction.extract_value,
        'elem[@name="ahData"]/elem[@name="numberOfHits"]',
    ],
    "alert_ahData_partyKey": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="ahData"]/elem[@name="partyKey"]',
    ],
    "alert_ahData_partyName": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="ahData"]/elem[@name="partyName"]',
    ],
    "alert_ahData_entityExcludeListsNames": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="ahData"]/elem[@name="entityExcludeListsNames"]',
    ],
    "alert_ahData_hitExcludeListsNames": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="ahData"]/elem[@name="hitExcludeListsNames"]',
    ],
    "alert_partyType": [XMLFunction.extract_value, 'elem[@name="partyType"]'],
    "alert_partyDOB": [XMLFunction.extract_value, 'elem[@name="partyDOB"]'],
    "alert_partyYOB": [XMLFunction.extract_value, 'elem[@name="partyYOB"]'],
    "alert_partyBirthCountry": [XMLFunction.extract_value, 'elem[@name="partyBirthCountry"]'],
    "alert_partyBirthLocation": [XMLFunction.extract_value, 'elem[@name="partyBirthLocation"]'],
    "alert_partyGender": [XMLFunction.extract_value, 'elem[@name="partyGender"]'],
    "alert_partyIds_idType": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="partyIds"]/elem[@name="idType"]',
    ],
    "alert_partyIds_idNumber": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="partyIds"]/elem[@name="idNumber"]',
    ],
    "alert_partyIds_idCountry": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="partyIds"]/elem[@name="idCountry"]',
    ],
    "alert_partyNatCountries_countryCd": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="partyNatCountries"]/elem[@name="countryCd"]',
    ],
    "alert_partyAddresses_partyAddressLine1": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="partyAddresses"]/elem[@name="partyAddressLine1"]',
    ],
    "alert_partyAddresses_partyAddressLine2": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="partyAddresses"]/elem[@name="partyAddressLine2"]',
    ],
    "alert_partyAddresses_partyCity": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="partyAddresses"]/elem[@name="partyCity"]',
    ],
    "alert_partyAddresses_partyPostalCd": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="partyAddresses"]/elem[@name="partyPostalCd"]',
    ],
    "alert_partyAddresses_partyStateProvince": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="partyAddresses"]/elem[@name="partyStateProvince"]',
    ],
    "alert_partyAddresses_partyCountry": [
        XMLFunction.extract_string_elements_array,
        'elem[@name="partyAddresses"]/elem[@name="partyCountry"]',
    ],
}
