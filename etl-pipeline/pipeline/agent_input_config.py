import copy


def prepend_agent_name_to_ap_or_wl_or_aliases_key(agent_input_config):
    """Prepend the agent name (level 1 key) to level 2 key. So the new level 2 key will be

    Input:
    { 'name_agent': {'ap': ['record_name'],
                    'ap_aliases': [],
                    'wl': ['name_hit'],
                    'wl_aliases': []
                    }
    }

    Output:
    { 'name_agent': {'name_agent_ap': ['record_name'],
                    'name_agent_ap_aliases': [],
                    'name_agent_wl': ['name_hit'],
                    'name_agent_wl_aliases': []
                    }
    }
    """
    result = dict()
    for agent_name, config in agent_input_config.items():
        result[agent_name] = dict()

        for ap_or_wl_or_aliases, source_cols in config.items():
            prepended_key_name = "_".join([agent_name, ap_or_wl_or_aliases])
            result[agent_name][prepended_key_name] = source_cols

    return result

    # Sanity check


input_template = {"ap": [], "ap_aliases": [], "wl": [], "wl_aliases": []}

agent_list = [
    "party_type_agent",
    "name_agent",
    "dob_agent",
    "pob_agent",
    "gender_agent",
    "national_id_agent",
    #     'passport_agent',
    "document_number_agent",
    "nationality_agent",
    "historical_decision_name_agent",
    "pep_payment_agent",
    "hit_is_san_agent",
    "hit_is_deceased_agent",
    "hit_has_dob_id_address_agent",
    "rba_agent",
]

agent_input_config = {}

for agent in agent_list:
    new_input = copy.deepcopy(input_template)
    agent_input_config[agent] = new_input


# Agent input creator

# DO NOTE the fff_format use i-based index While, python list is 0-based index
agent_input_config["party_type_agent"]["ap"].extend(["alert_partyType"])
agent_input_config["party_type_agent"]["wl"].extend(["hit_entryType"])

agent_input_config["name_agent"]["ap"].extend(["ap_hit_names"])
agent_input_config["name_agent"]["wl"].extend(["wl_hit_names"])

# agent_input_config['dob_agent']['ap'].extend(['alert_partyDOB', 'alert_partyYOB'])
# agent_input_config['dob_agent']['wl'].extend(['hit_datesOfBirth_birthDate', 'hit_datesOfBirth_yearOfBirth'])

# The alert_partyDOB has value of '31/12/99' which is actually '9999-12-31' from P14
agent_input_config["dob_agent"]["ap"].extend(["P14"])
agent_input_config["dob_agent"]["wl"].extend(
    ["hit_datesOfBirth_birthDate", "hit_cs_1_data_points.dob"]
)

agent_input_config["pob_agent"]["ap"].extend(["alert_partyBirthCountry"])
# It's weird hit_placesOfBirth_birthPlace has country data instead of hit_placesOfBirth_birthCountry
agent_input_config["pob_agent"]["wl"].extend(["hit_placesOfBirth_birthPlace"])

agent_input_config["gender_agent"]["ap"].extend(["alert_partyGender"])
agent_input_config["gender_agent"]["wl"].extend(["hit_gender"])

# The national ID is for SG NRIC only
agent_input_config["national_id_agent"]["ap"].extend(["ap_nric"])
agent_input_config["national_id_agent"]["wl"].extend(["hit_cs_1_data_points.nric"])

# agent_input_config['passport_agent']['ap'].extend(get_ap_screenable_attributes('PASSPORT'))
# agent_input_config['passport_agent']['wl'].extend(['passport'])
agent_input_config["document_number_agent"]["ap"].extend(["alert_partyIds_idNumber"])
# The 'hit_cs_1_data_points.nric' is a subset of 'hit_cs_1_data_points.possible_nric'. Hence, use 'hit_cs_1_data_points.possible_nric' only
agent_input_config["document_number_agent"]["wl"].extend(
    ["hit_ids_idNumber", "hit_cs_1_data_points.possible_nric"]
)

# "alert_partyNatCountries_countryCd" is alwasy empty, "alert_partyNatCountries_countryCd" is actually part of address
# but it's used by screening engine and analyst to match with WL nationality country
agent_input_config["nationality_agent"]["ap"].extend(
    ["alert_partyNatCountries_countryCd", "alert_partyAddresses_partyCountry"]
)
agent_input_config["nationality_agent"]["wl"].append("hit_nationalityCountries_country")

agent_input_config["historical_decision_name_agent"]["ap"].extend(
    ["alert_ahData_numberOfHits", "STATUS_NAME", "alert_ahData_partyName", "ALERT_DATE"]
)
agent_input_config["historical_decision_name_agent"]["wl"].extend(["hit_entryId"])

# agent_input_config['historical_decision_entity_key_agent']['ap'].extend(['alert_ahData_numberOfHits', 'STATUS_NAME', 'alert_alertEntityKey', 'ALERT_DATE'])
# agent_input_config['historical_decision_entity_key_agent']['wl'].extend(['hit_entryId'])

agent_input_config["pep_payment_agent"]["ap"].extend(["P12"])
agent_input_config["pep_payment_agent"]["wl"].extend(["hit_listID"])

# The P36 and P38 are at alert level, use `hit_listId` and `hit_categories_category` which is at hit level
agent_input_config["hit_is_san_agent"]["wl"].extend(["hit_listId", "hit_categories_category"])

agent_input_config["hit_is_deceased_agent"]["wl"].extend(["hit_isDeceased"])

agent_input_config["hit_has_dob_id_address_agent"]["wl"].extend(
    [
        "hit_datesOfBirth_birthDate",
        "hit_cs_1_data_points.dob",
        "hit_ids_idNumber",
        "hit_cs_1_data_points.possible_nric",
        "hit_addresses_streetAddress1",
    ]
)

agent_input_config["rba_agent"]["ap"].extend(
    [
        "alert_ahData_numberOfHits",
        "STATUS_NAME",
        "alert_ahData_partyName",
        "ALERT_DATE",
        "last_note",
    ]
)
agent_input_config["rba_agent"]["wl"].extend(["hit_entryId"])
agent_input_prepended_agent_name_config = prepend_agent_name_to_ap_or_wl_or_aliases_key(
    agent_input_config
)
