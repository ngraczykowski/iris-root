INSERT INTO ae_alert
VALUES (1, 'AVIR128SCR13925IN123TEST0003:IN:GR-ESAN:273067', NOW(), NOW(), 5);
INSERT INTO ae_alert
VALUES (2, 'SCREEN8255R93250ENTSAN1065:EG:GR-ESAN:1013', NOW(), NOW(), 5);

INSERT INTO ae_recommendation (analysis_id, alert_id, created_at, recommended_action)
VALUES (1, 2, NOW(), 'TO_THE_MOON'),
       (1, 1, NOW(), 'TO_THE_MOON');

INSERT INTO ae_match
VALUES (1, 1, NOW(), 'GSN00067068', 1);
INSERT INTO ae_match
VALUES (2, 1, NOW(), 'DB00051992', 2);
INSERT INTO ae_match
VALUES (3, 2, NOW(), 'DB00051992', 1);

INSERT INTO ae_match_category_value
VALUES (2, 1, NOW(), 'NO_VALUE');
VALUES (1, 1, NOW(), 'NO_VALUE');

INSERT INTO ae_match_feature_value
VALUES (2, 1, NOW(), 'MATCH', '{
  "customerValues": [
    "M"
  ],
  "watchlistValues": [
    "M"
  ]
}');

INSERT INTO ae_dataset_alert
VALUES (1, 1);

INSERT INTO ae_match_solution (analysis_id, match_id, created_at, solution, reason)
VALUES (1, 1, NOW(), 'SOLUTION_NO_DECISION', '{
  "stepId": "",
  "featureVectorSignature": "C7pHOd+hYXCB9mXF0m+g7iHU33E="
}'),
       (1, 2, NOW(), 'SOLUTION_NO_DECISION', '{
         "stepId": "",
         "featureVectorSignature": "C7pHOd+hYXCB9mXF0m+g7iHU33E="
       }'),
       (1, 3, NOW(), 'SOLUTION_NO_DECISION', '{
         "stepId": "",
         "featureVectorSignature": "C7pHOd+hYXCB9mXF0m+g7iHU33E="
       }');

INSERT INTO ae_alert_comment_input (alert_id, created_at, value)
VALUES (1, NOW(), '{
  "reason": {
    "stepId": "123",
    "featureVectorSignature": "otiR989mzU4qkEbHSQa+BiJQBgk="
  }
}'),
       (2, NOW(), '{
         "reason": {
           "stepId": "420",
           "featureVectorSignature": "otiR989mzU4qkEbHSQa+BiJQBgk="
         }
       }');
