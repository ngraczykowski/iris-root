UPDATE governance_policy_step_feature_values
SET value = 'AGENT_ERROR'
WHERE feature_id IN
      (SELECT id FROM governance_policy_step_match_condition WHERE name = 'features/geo')
  AND value = 'ERROR';