import itertools

template = """
{
  "feature_collection": {
    "feature": [
      %s
    ]
  },
  "feature_vectors": [
    %s
  ]
}
"""

ap_type_values = ["I", "C"]
is_deny_values = ["YES", "NO"]
name_agent_values = ["STRONG_MATCH", "WEAK_MATCH", "EXACT_MATCH", "HQ_NO_MATCH", "NO_MATCH"]
date_agent_values = ["EXACT", "NEAR", "OUT_OF_RANGE"]
national_id_agent_values = ["PERFECT MATCH", "NO_MATCH"]
passport_agent_values = ["DIGIT_MATCH", "NO_MATCH"]
document_agent_values = ["PERFECT_MATCH", "NO_MATCH"]
# national_id_agent_values = ["PERFECT_MATCH", "DIGIT_MATCH", "WEAK_DIGIT_MATCH", "WEAK_MATCH", "NO_MATCH"]
# passport_agent_values = ["PERFECT_MATCH", "DIGIT_MATCH", "WEAK_DIGIT_MATCH", "WEAK_MATCH", "NO_MATCH"]
# document_agent_values = ["PERFECT_MATCH", "DIGIT_MATCH", "WEAK_DIGIT_MATCH", "WEAK_MATCH", "NO_MATCH"]
gender_agent_values = ["MATCH", "NO_MATCH"]
nationality_agent_values = ["WEAK_MATCH", "MATCH", "NO_MATCH"]
residency_agent_values = ["WEAK_MATCH", "MATCH", "NO_MATCH"]


features = {
  "apType": ap_type_values,
  "isDeny": is_deny_values,
  "nameAgent": name_agent_values,
  "dateAgent": date_agent_values,
  "nationalIdAgent": national_id_agent_values,
  "passportAgent": passport_agent_values,
  "documentAgent": document_agent_values,
  "genderAgent": gender_agent_values,
  "nationalityAgent": nationality_agent_values,
  "residencyAgent": residency_agent_values,
}

def generate_fv():
  features_names_list = ','.join(["""{"name": "%s"}""" % key for key in features.keys()])

  features_values_list = list(itertools.product(*[
    ap_type_values,
    is_deny_values,
    name_agent_values,
    date_agent_values,
    passport_agent_values,
    document_agent_values,
    gender_agent_values,
    nationality_agent_values,
    residency_agent_values,
    national_id_agent_values,
  ]))
  vectors = []

  for line in features_values_list:
    values_str = ','.join(['"%s"' % value for value in line])
    vectors.append("""{ "feature_value": [%s] }""" % values_str)

  result = template % (features_names_list, ',\n'.join(vectors))
  with open("output.json","w") as f:
    f.write(result)


if __name__ == '__main__':
  generate_fv()
