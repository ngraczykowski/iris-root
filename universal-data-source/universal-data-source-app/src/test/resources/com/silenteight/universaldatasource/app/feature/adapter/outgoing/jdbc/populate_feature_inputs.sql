INSERT INTO uds_feature_input(alert_name, match_name, feature, agent_input_type, agent_input)
VALUES ('alerts/1', 'alerts/1/matches/0', 'features/name', 'com.silenteight.datasource.api.name.v1.NameFeatureInput', '{
  "feature": "features/name",
  "alerted_party_names": [{"name" : "JohnDoe"}],
  "watchlist_names": [{"name" : "JohnDoe"}],
  "alerted_party_type": "INDIVIDUAL",
  "matching_texts": ["Doe"]
}'),
       ('alerts/2', 'alerts/2/matches/2', 'features/name', 'NameFeatureInput', '{}'),
       ('alerts/3', 'alerts/3/matches/3', 'features/state', 'com.silenteight.datasource.api.location.v1.LocationFeatureInput', '{
  "feature": "features/state",
  "alerted_party_location": "Cambridge TerraceWellington 6011, New Zealand",
  "watchlist_location": "Cambridge 6011, New Zealand"
}
'),
       ('alerts/4', 'alerts/4/matches/4', 'features/city', 'com.silenteight.datasource.api.location.v1.LocationFeatureInput', '{
  "feature": "features/city",
  "alerted_party_location": "HongKong",
  "watchlist_location": "Singapore"
}'),
       ('alerts/5', 'alerts/5/matches/5', 'features/pep', 'com.silenteight.datasource.api.ispep.v2.IsPepFeatureInput', '{
  "feature": "features/pep",
  "watchlist_item": {},
  "alerted_party_item": {}
}'),
       ('alerts/6', 'alerts/6/matches/6', 'features/freetext', 'com.silenteight.datasource.api.freetext.v1.FreeTextFeatureInput', '{
  "feature": "features/freetext",
  "matched_name": "Joanna",
  "matched_name_synonym": "Joe",
  "matched_type": "name",
  "matching_texts": ["Joe"],
  "freetext": "Joe"
}'),
       ('alerts/7', 'alerts/7/matches/7', 'features/name', 'NameFeatureInput', '{}'),
       ('alerts/8', 'alerts/8/matches/8', 'features/name', 'NameFeatureInput', '{}'),
       ('alerts/9', 'alerts/9/matches/9', 'features/name', 'NameFeatureInput', '{}'),
       ('alerts/10', 'alerts/10/matches/10', 'features/name', 'NameFeatureInput', '{}');


