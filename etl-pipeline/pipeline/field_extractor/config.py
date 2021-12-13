from enum import Enum


class Function(Enum):
    one_value_find = "find_text_one"
    find_all_values = "find_all_values"


HIT_CONFIG = {
    "hit_listId": [Function.one_value_find, 'elem[@name="listId"]'],
    "hit_entryId": [Function.one_value_find, 'elem[@name="entryId"]'],
    "hit_listVersion": [Function.one_value_find, 'elem[@name="listVersion"]'],
    "hit_entryType": [Function.one_value_find, 'elem[@name="entryType"]'],
    "hit_listUpdateDate": [Function.one_value_find, 'elem[@name="listUpdateDate"]'],
    "hit_entryCreatedDate": [Function.one_value_find, 'elem[@name="entryCreatedDate"]'],
    "hit_entryUpdateDate": [Function.one_value_find, 'elem[@name="entryUpdateDate"]'],
    # Move the hit_displayName the the proper position for eash data discovery
    "hit_displayName": [Function.one_value_find, 'elem[@name="displayName"]'],
    "hit_matchedName": [Function.one_value_find, 'elem[@name="matchedName"]'],
    "hit_isNameBroken": ["find_boolean_one", 'elem[@name="isNameBroken"]'],
    "hit_aliases_displayName": [
        Function.find_all_values,
        'elem[@name="aliases"]/elem[@name="displayName"]',
    ],
    "hit_aliases_matchedName": [
        Function.find_all_values,
        'elem[@name="aliases"]/elem[@name="matchedName"]',
    ],
    "hit_aliases_isNameBroken": [
        "find_boolean_all",
        'elem[@name="aliases"]/elem[@name="isNameBroken"]',
    ],
    "hit_aliases_matchStrength": [
        Function.find_all_values,
        'elem[@name="aliases"]/elem[@name="matchStrength"]',
    ],
    "hit_addresses_streetAddress1": [
        Function.find_all_values,
        'elem[@name="addresses"]/elem[@name="streetAddress1"]',
    ],
    "hit_addresses_streetAddress2": [
        Function.find_all_values,
        'elem[@name="addresses"]/elem[@name="streetAddress2"]',
    ],
    "hit_addresses_city": [Function.find_all_values, 'elem[@name="addresses"]/elem[@name="city"]'],
    "hit_addresses_stateProvince": [
        Function.find_all_values,
        'elem[@name="addresses"]/elem[@name="stateProvince"]',
    ],
    "hit_addresses_postalCode": [
        Function.find_all_values,
        'elem[@name="addresses"]/elem[@name="postalCode"]',
    ],
    "hit_addresses_country": [
        Function.find_all_values,
        'elem[@name="addresses"]/elem[@name="country"]',
    ],
    "hit_ids_idType": [Function.find_all_values, 'elem[@name="ids"]/elem[@name="idType"]'],
    "hit_ids_idNumber": [Function.find_all_values, 'elem[@name="ids"]/elem[@name="idNumber"]'],
    "hit_ids_idCountry": [Function.find_all_values, 'elem[@name="ids"]/elem[@name="idCountry"]'],
    "hit_nationalityCountries_country": [
        Function.find_all_values,
        'elem[@name="nationalityCountries"]/elem[@name="country"]',
    ],
    "hit_placesOfBirth_birthPlace": [
        Function.find_all_values,
        'elem[@name="placesOfBirth"]/elem[@name="birthPlace"]',
    ],
    "hit_placesOfBirth_birthCountry": [
        Function.find_all_values,
        'elem[@name="placesOfBirth"]/elem[@name="birthCountry"]',
    ],
    "hit_age": [Function.one_value_find, 'elem[@name="age"]'],
    "hit_ageAsOfDate": [Function.one_value_find, 'elem[@name="ageAsOfDate"]'],
    "hit_datesOfBirth_birthDate": [
        Function.find_all_values,
        'elem[@name="datesOfBirth"]/elem[@name="birthDate"]',
    ],
    "hit_datesOfBirth_yearOfBirth": [
        Function.find_all_values,
        'elem[@name="datesOfBirth"]/elem[@name="yearOfBirth"]',
    ],
    "hit_categories_category": [
        Function.find_all_values,
        'elem[@name="categories"]/elem[@name="category"]',
    ],
    "hit_keywords_keyword": [
        Function.find_all_values,
        'elem[@name="keywords"]/elem[@name="keyword"]',
    ],
    "hit_title": [Function.one_value_find, 'elem[@name="title"]'],
    "hit_position": [Function.one_value_find, 'elem[@name="position"]'],
    "hit_gender": [Function.one_value_find, 'elem[@name="gender"]'],
    "hit_isDeceased": ["find_boolean_one", 'elem[@name="isDeceased"]'],
    "hit_deceasedDate": [Function.one_value_find, 'elem[@name="deceasedDate"]'],
    "hit_cs_1": [Function.one_value_find, 'elem[@name="cs_1"]'],
    "hit_cs_2": [Function.one_value_find, 'elem[@name="cs_2"]'],
    "hit_cs_3": [Function.one_value_find, 'elem[@name="cs_3"]'],
    "hit_cs_4": [Function.one_value_find, 'elem[@name="cs_4"]'],
    "hit_cs_5": [Function.one_value_find, 'elem[@name="cs_5"]'],
    "hit_cs_6": [Function.one_value_find, 'elem[@name="cs_6"]'],
    "hit_cs_7": [Function.one_value_find, 'elem[@name="cs_7"]'],
    "hit_cs_8": [Function.one_value_find, 'elem[@name="cs_8"]'],
    "hit_cs_9": [Function.one_value_find, 'elem[@name="cs_9"]'],
    "hit_cs_10": [Function.one_value_find, 'elem[@name="cs_10"]'],
    "hit_cs_11": [Function.one_value_find, 'elem[@name="cs_11"]'],
    "hit_cs_12": [Function.one_value_find, 'elem[@name="cs_12"]'],
    "hit_cs_13": [Function.one_value_find, 'elem[@name="cs_13"]'],
    "hit_cs_14": [Function.one_value_find, 'elem[@name="cs_14"]'],
    "hit_cs_15": [Function.one_value_find, 'elem[@name="cs_15"]'],
    "hit_cs_16": [Function.one_value_find, 'elem[@name="cs_16"]'],
    "hit_cs_17": [Function.one_value_find, 'elem[@name="cs_17"]'],
    "hit_cs_18": [Function.one_value_find, 'elem[@name="cs_18"]'],
    "hit_additionalInfo_name": [
        Function.find_all_values,
        'elem[@name="additionalInfo"]/elem[@name="name"]',
    ],
    "hit_additionalInfo_value": [
        Function.find_all_values,
        'elem[@name="additionalInfo"]/elem[@name="value"]',
    ],
    "hit_score": [Function.one_value_find, 'elem[@name="score"]'],
    "hit_matchType": [Function.one_value_find, 'elem[@name="matchType"]'],
    "hit_scoreFactors_factorId": [
        Function.find_all_values,
        'elem[@name="scoreFactors"]/elem[@name="scoreFactors"]/elem[@name="factorId"]',
    ],
    "hit_scoreFactors_factorDesc": [
        Function.find_all_values,
        'elem[@name="scoreFactors"]/elem[@name="scoreFactors"]/elem[@name="factorDesc"]',
    ],
    "hit_scoreFactors_factorValue": [
        Function.find_all_values,
        'elem[@name="scoreFactors"]/elem[@name="scoreFactors"]/elem[@name="factorValue"]',
    ],
    "hit_scoreFactors_factorScore": [
        Function.find_all_values,
        'elem[@name="scoreFactors"]/elem[@name="scoreFactors"]/elem[@name="factorScore"]',
    ],
    "hit_scoreFactors_factorImpact": [
        Function.find_all_values,
        'elem[@name="scoreFactors"]/elem[@name="scoreFactors"]/elem[@name="factorImpact"]',
    ],
    "hit_scoresBreakdown_matchedName": [
        Function.one_value_find,
        'elem[@name="scoresBreakdown"]/elem[@name="matchedName"]',
    ],
    "hit_scoresBreakdown_aliases_matchedName": [
        Function.one_value_find,
        'elem[@name="scoresBreakdown"]/elem[@name="aliases_matchedName"]',
    ],
    "hit_scoresBreakdown_addresses_city": [
        Function.one_value_find,
        'elem[@name="scoresBreakdown"]/elem[@name="addresses_city"]',
    ],
    "hit_scoresBreakdown_addresses_country": [
        Function.one_value_find,
        'elem[@name="scoresBreakdown"]/elem[@name="addresses_country"]',
    ],
    "hit_scoresBreakdown_addresses_stateProvince": [
        Function.one_value_find,
        'elem[@name="scoresBreakdown"]/elem[@name="addresses_stateProvince"]',
    ],
    "hit_scoresBreakdown_ids_idNumber": [
        Function.one_value_find,
        'elem[@name="scoresBreakdown"]/elem[@name="ids_idNumber"]',
    ],
    "hit_explanations_matchedName_Explanation": [
        Function.find_all_values,
        'elem[@name="explanations"]/elem[@name="matchedName"]/elem[@name="Explanation"]',
    ],
    "hit_explanations_aliases_matchedName_Explanation": [
        Function.find_all_values,
        'elem[@name="explanations"]/elem[@name="aliases_matchedName"]/elem[@name="Explanation"]',
    ],
    "hit_explanations_nationalityCountries_country_Explanation": [
        Function.find_all_values,
        'elem[@name="explanations"]/elem[@name="nationalityCountries_country"]/elem[@name="Explanation"]',
    ],
    # <Guessed attributes based on the pattern beginning
    "hit_explanations_address_city_Explanation": [
        Function.find_all_values,
        'elem[@name="explanations"]/elem[@name="address_city"]/elem[@name="Explanation"]',
    ],
    "hit_explanations_address_country_Explanation": [
        Function.find_all_values,
        'elem[@name="explanations"]/elem[@name="address_country"]/elem[@name="Explanation"]',
    ],
    "hit_explanations_addresses_stateProvince_Explanation": [
        Function.find_all_values,
        'elem[@name="explanations"]/elem[@name="addresses_stateProvince"]/elem[@name="Explanation"]',
    ],
    "hit_explanations_ids_idNumber_Explanation": [
        Function.find_all_values,
        'elem[@name="explanations"]/elem[@name="ids_idNumber"]/elem[@name="Explanation"]',
    ],
    # Guessed attributes based on the pattern end>
    "hit_inputExplanations_matchedName_inputExplanation": [
        Function.find_all_values,
        'elem[@name="inputExplanations"]/elem[@name="matchedName"]/elem[@name="inputExplanation"]',
    ],
    "hit_inputExplanations_aliases_matchedName_inputExplanation": [
        Function.find_all_values,
        'elem[@name="inputExplanations"]/elem[@name="aliases_matchedName"]/elem[@name="inputExplanation"]',
    ],
    "hit_inputExplanations_nationalityCountries_country_inputExplanation": [
        Function.find_all_values,
        'elem[@name="inputExplanations"]/elem[@name="nationalityCountries_country"]/elem[@name="inputExplanation"]',
    ],
    # <Guessed attributes based on the pattern beginning
    "hit_inputExplanations_address_city_inputExplanation": [
        Function.find_all_values,
        'elem[@name="inputExplanations"]/elem[@name="address_city"]/elem[@name="inputExplanation"]',
    ],
    "hit_inputExplanations_address_country_inputExplanation": [
        Function.find_all_values,
        'elem[@name="inputExplanations"]/elem[@name="address_country"]/elem[@name="inputExplanation"]',
    ],
    "hit_inputExplanations_addresses_stateProvince_inputExplanation": [
        Function.find_all_values,
        'elem[@name="inputExplanations"]/elem[@name="addresses_stateProvince"]/elem[@name="inputExplanation"]',
    ],
    "hit_inputExplanations_ids_idNumber_inputExplanation": [
        Function.find_all_values,
        'elem[@name="inputExplanations"]/elem[@name="ids_idNumber"]/elem[@name="inputExplanation"]',
    ],
}


ALERT_HEADER_CONFIG = {
    "alert_alertId": [Function.one_value_find, 'elem[@name="alertId"]'],
    "alert_alertDate": [Function.one_value_find, 'elem[@name="alertDate"]'],
    "alert_alertEntityKey": [Function.one_value_find, 'elem[@name="alertEntityKey"]'],
    "alert_score": [Function.one_value_find, 'elem/elem[@name="score"]'],
    "alert_ahData_alertID": [Function.find_all_values, 'elem[@name="ahData"]/elem[@name="alertID"]'],
    "alert_ahData_alertDateTime": [
        Function.find_all_values,
        'elem[@name="ahData"]/elem[@name="alertDateTime"]',
    ],
    "alert_ahData_jobID": [Function.find_all_values, 'elem[@name="ahData"]/elem[@name="jobID"]'],
    "alert_ahData_jobName": [Function.find_all_values, 'elem[@name="ahData"]/elem[@name="jobName"]'],
    "alert_ahData_jobType": [Function.find_all_values, 'elem[@name="ahData"]/elem[@name="jobType"]'],
    "alert_ahData_score": [Function.find_all_values, 'elem[@name="ahData"]/elem[@name="score"]'],
    "alert_ahData_numberOfHits": [
        Function.one_value_find,
        'elem[@name="ahData"]/elem[@name="numberOfHits"]',
    ],
    "alert_ahData_partyKey": [
        Function.find_all_values,
        'elem[@name="ahData"]/elem[@name="partyKey"]',
    ],
    "alert_ahData_partyName": [
        Function.find_all_values,
        'elem[@name="ahData"]/elem[@name="partyName"]',
    ],
    "alert_ahData_entityExcludeListsNames": [
        Function.find_all_values,
        'elem[@name="ahData"]/elem[@name="entityExcludeListsNames"]',
    ],
    "alert_ahData_hitExcludeListsNames": [
        Function.find_all_values,
        'elem[@name="ahData"]/elem[@name="hitExcludeListsNames"]',
    ],
    "alert_partyType": [Function.one_value_find, 'elem[@name="partyType"]'],
    "alert_partyDOB": [Function.one_value_find, 'elem[@name="partyDOB"]'],
    "alert_partyYOB": [Function.one_value_find, 'elem[@name="partyYOB"]'],
    "alert_partyBirthCountry": [Function.one_value_find, 'elem[@name="partyBirthCountry"]'],
    "alert_partyBirthLocation": [Function.one_value_find, 'elem[@name="partyBirthLocation"]'],
    "alert_partyGender": [Function.one_value_find, 'elem[@name="partyGender"]'],
    "alert_partyIds_idType": [
        Function.find_all_values,
        'elem[@name="partyIds"]/elem[@name="idType"]',
    ],
    "alert_partyIds_idNumber": [
        Function.find_all_values,
        'elem[@name="partyIds"]/elem[@name="idNumber"]',
    ],
    "alert_partyIds_idCountry": [
        Function.find_all_values,
        'elem[@name="partyIds"]/elem[@name="idCountry"]',
    ],
    "alert_partyNatCountries_countryCd": [
        Function.find_all_values,
        'elem[@name="partyNatCountries"]/elem[@name="countryCd"]',
    ],
    "alert_partyAddresses_partyAddressLine1": [
        Function.find_all_values,
        'elem[@name="partyAddresses"]/elem[@name="partyAddressLine1"]',
    ],
    "alert_partyAddresses_partyAddressLine2": [
        Function.find_all_values,
        'elem[@name="partyAddresses"]/elem[@name="partyAddressLine2"]',
    ],
    "alert_partyAddresses_partyCity": [
        Function.find_all_values,
        'elem[@name="partyAddresses"]/elem[@name="partyCity"]',
    ],
    "alert_partyAddresses_partyPostalCd": [
        Function.find_all_values,
        'elem[@name="partyAddresses"]/elem[@name="partyPostalCd"]',
    ],
    "alert_partyAddresses_partyStateProvince": [
        Function.find_all_values,
        'elem[@name="partyAddresses"]/elem[@name="partyStateProvince"]',
    ],
    "alert_partyAddresses_partyCountry": [
        Function.find_all_values,
        'elem[@name="partyAddresses"]/elem[@name="partyCountry"]',
    ],
}
