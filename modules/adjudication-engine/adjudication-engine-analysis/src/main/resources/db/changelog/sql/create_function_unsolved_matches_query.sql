CREATE OR REPLACE FUNCTION select_unsolved_matches(in_analysis_id BIGINT)
  RETURNS TABLE
          (
            alert_id        BIGINT,
            match_id        BIGINT,
            category_values VARCHAR[],
            feature_values  VARCHAR[]
          )
  LANGUAGE plpgsql
AS
$$
BEGIN
  DROP TABLE IF EXISTS tmp_ae_pending_recommendation;
  CREATE TEMP TABLE tmp_ae_pending_recommendation AS (
    SELECT * FROM ae_pending_recommendation apr WHERE apr.analysis_id = in_analysis_id
  );
  ANALYZE tmp_ae_pending_recommendation;

  DROP TABLE IF EXISTS tmp_ae_analysis_feature_vector_elements_query;
  CREATE TEMP TABLE tmp_ae_analysis_feature_vector_elements_query AS (
    SELECT *
    FROM ae_analysis_feature_vector_elements_query aafveq
    WHERE aafveq.analysis_id = in_analysis_id
  );
  ANALYZE tmp_ae_analysis_feature_vector_elements_query;

  DROP TABLE IF EXISTS tmp_ae_analysis_match_feature_vector_elements_query;
  CREATE TEMP TABLE tmp_ae_analysis_match_feature_vector_elements_query AS (
    SELECT *
    FROM ae_analysis_match_feature_vector_elements_query aamfveq
    WHERE aamfveq.analysis_id = in_analysis_id
  );
  ANALYZE tmp_ae_analysis_match_feature_vector_elements_query;

  DROP TABLE IF EXISTS tmp_ae_match_solution;
  CREATE TEMP TABLE tmp_ae_match_solution AS (
    SELECT * FROM ae_match_solution ams WHERE ams.analysis_id = in_analysis_id
  );
  ANALYZE tmp_ae_match_solution;

  RETURN QUERY SELECT apr.alert_id,
                      aamfveq.match_id,
                      aamfveq.category_values,
                      aamfveq.feature_values
               FROM tmp_ae_pending_recommendation apr
                      JOIN tmp_ae_analysis_feature_vector_elements_query aafveq
                           on apr.analysis_id = aafveq.analysis_id
                      JOIN tmp_ae_analysis_match_feature_vector_elements_query aamfveq
                           on apr.alert_id = aamfveq.alert_id
                             AND apr.analysis_id = aamfveq.analysis_id
                             AND aafveq.category_ids = aamfveq.category_ids
                             AND
                              aafveq.agent_config_feature_ids = aamfveq.agent_config_feature_ids
                      LEFT JOIN tmp_ae_match_solution ams
                                on aamfveq.match_id = ams.match_id AND
                                   apr.analysis_id = ams.analysis_id
               WHERE ams.match_id IS NULL;
END;
$$
