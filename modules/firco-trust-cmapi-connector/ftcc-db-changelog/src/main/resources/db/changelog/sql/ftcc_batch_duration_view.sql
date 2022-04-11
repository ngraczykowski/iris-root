CREATE OR REPLACE VIEW ftcc_batch_duration_view AS
SELECT fr.batch_id,
       fr.created_at                    AS start_at,
       fcr.created_at                   AS finish_at,
       (fcr.created_at - fr.created_at) AS duration
FROM (SELECT fcr.batch_id, MAX(fcr.created_at) AS created_at
 FROM ftcc_callback_request fcr
 GROUP BY fcr.batch_id) AS fcr
  JOIN ftcc_batch_completed fbc
       ON fcr.batch_id = fbc.batch_id
  JOIN ftcc_request fr ON fr.batch_id = fcr.batch_id;
