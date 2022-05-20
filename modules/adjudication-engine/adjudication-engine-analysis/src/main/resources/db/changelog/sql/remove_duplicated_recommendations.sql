WITH duplicates AS (
  SELECT recommendation_id
       , row_number() OVER (PARTITION BY analysis_id, alert_id
    ORDER BY recommendation_id DESC) AS row_num
  FROM ae_recommendation
)
DELETE
FROM ae_recommendation
WHERE recommendation_id IN (
  SELECT duplicates.recommendation_id
  FROM duplicates
  WHERE duplicates.row_num > 1);
