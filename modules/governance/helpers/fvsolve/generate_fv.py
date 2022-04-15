import itertools

# policy_name used in this example indicates the policy imported from solid-steps.json file
template = """
{
  "policy_name": "policies/9a4d3b90-833d-4f7b-8bbc-752d129ed301",
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
# categories
ap_type_values = ["I", "C"]
risk_type_values = ["AML", "OTHER", "PEP", "SAN", "SCION"]
# features
name_agent_values = ["STRONG_MATCH", "WEAK_MATCH", "EXACT_MATCH", "HQ_NO_MATCH", "NO_MATCH"]
date_agent_values = ["EXACT", "NEAR", "OUT_OF_RANGE"]
document_agent_values = ["PERFECT_MATCH", "DIGIT_MATCH", "WEAK_DIGIT_MATCH", "WEAK_MATCH", "NO_MATCH"]
gender_agent_values = ["MATCH", "NO_MATCH"]
country_agent_values = ["WEAK_MATCH", "MATCH", "NO_MATCH"]


features = {
  "categories/customerType": ap_type_values,
  "categories/hitType": risk_type_values,
  "features/name": name_agent_values,
  "features/dateOfBirth": date_agent_values,
  "features/nationalIdDocument": document_agent_values,
  "features/passportNumberDocument": document_agent_values,
  "features/otherDocument": document_agent_values,
  "features/gender": gender_agent_values,
  "features/nationalityCountry": country_agent_values,
  "features/residencyCountry": country_agent_values,
  "features/incorporationCountry": country_agent_values,
  "features/registrationCountry": country_agent_values,
  "features/otherCountry": country_agent_values,
}

def generate_fv():
  features_names_list = ','.join(["""{"name": "%s"}""" % key for key in features.keys()])

  features_values_list = list(itertools.product(*[
    ap_type_values,
    risk_type_values,
    name_agent_values,
    date_agent_values,
    document_agent_values,
    document_agent_values,
    document_agent_values,
    gender_agent_values,
    country_agent_values,
    country_agent_values,
    country_agent_values,
    country_agent_values,
    country_agent_values
  ]))
  vectors = []

  TAKE_EVERY_N_ELEMENT = 1000
  for line in features_values_list[0::TAKE_EVERY_N_ELEMENT]:
    values_str = ','.join(['"%s"' % value for value in line])
    vectors.append("""{ "feature_value": [%s] }""" % values_str)

  result = template % (features_names_list, ',\n'.join(vectors))
  with open("output.json","w") as f:
    f.write(result)


if __name__ == '__main__':
  generate_fv()
