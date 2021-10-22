CREATE TYPE category_feature_ids AS
(
  category_ids BIGINT[],
  feature_ids  BIGINT[]
);

CREATE OR REPLACE FUNCTION ae_select_unsolved_matches(_analysis_id bigint, _limit int)
  RETURNS TABLE
          (
            _alert_id                bigint,
            _match_id                bigint,
            _client_match_identifier varchar,
            _category_names          character varying[],
            _category_values         character varying[],
            _feature_names           character varying[],
            _feature_values          character varying[],
            _feature_reasons         jsonb[],
            _agent_configs           character varying[]
          )
  LANGUAGE plpgsql
AS
$$
DECLARE
  _count                int;
  _category_feature_ids category_feature_ids;
BEGIN
  -- pending recommendations for the given analysis
  DROP TABLE IF EXISTS tmp_ae_pending_recommendation;
  CREATE TEMP TABLE tmp_ae_pending_recommendation ON COMMIT DROP AS (
    SELECT alert_id FROM ae_pending_recommendation apr WHERE apr.analysis_id = _analysis_id
  );
  ANALYZE tmp_ae_pending_recommendation;

  -- expected category/feature vector for the given analysis
  SELECT category_ids, agent_config_feature_ids
  INTO _category_feature_ids
  FROM ae_analysis_feature_vector_elements_query aafveq
  WHERE aafveq.analysis_id = _analysis_id;

  -- missing match solutions for a given analysis
  DROP TABLE IF EXISTS tmp_ae_missing_match_solution;
  CREATE TEMP TABLE tmp_ae_missing_match_solution ON COMMIT DROP AS (
    SELECT am.alert_id, am.match_id
    FROM tmp_ae_pending_recommendation apr
           JOIN ae_match am ON am.alert_id = apr.alert_id
           LEFT JOIN ae_match_solution ams ON ams.match_id = am.match_id
      AND ams.analysis_id = _analysis_id
    WHERE ams.match_id IS NULL
  );
  ANALYZE tmp_ae_missing_match_solution;
  -- exit if table is empty
  SELECT COUNT(*) INTO _count FROM (SELECT 1 FROM tmp_ae_missing_match_solution LIMIT 1) AS a;
  IF _count = 0 THEN
    RETURN;
  END IF;

  -- aggregate feature ids for all unsolved matches for given analysis
  DROP TABLE IF EXISTS tmp_analysis_match_agent_config_feature_ids;
  CREATE TEMP TABLE tmp_analysis_match_agent_config_feature_ids ON COMMIT DROP AS (
    SELECT amms.match_id,
           amms.alert_id,
           ARRAY_AGG(
               amfv.agent_config_feature_id
               ORDER BY amfv.agent_config_feature_id) AS agent_config_feature_ids
    FROM tmp_ae_missing_match_solution amms
           JOIN ae_match_feature_value amfv ON amms.match_id = amfv.match_id
           JOIN ae_analysis_feature aaf
                ON amfv.agent_config_feature_id = aaf.agent_config_feature_id AND
                   aaf.analysis_id = _analysis_id
    GROUP BY 1, 2
  );
  ANALYZE tmp_analysis_match_agent_config_feature_ids;

  -- aggregate category ids for all unsolved matches for given analysis
  DROP TABLE IF EXISTS tmp_analysis_match_agent_config_category_ids;
  CREATE TEMP TABLE tmp_analysis_match_agent_config_category_ids ON COMMIT DROP AS (
    SELECT amms.alert_id,
           amms.match_id,
           ARRAY_AGG(amcv.category_id ORDER BY amcv.category_id) AS category_ids
    FROM tmp_ae_missing_match_solution amms
           JOIN ae_match_category_value amcv ON amms.match_id = amcv.match_id
           JOIN ae_analysis_category aac
                ON amcv.category_id = aac.category_id AND aac.analysis_id = _analysis_id
    GROUP BY 1, 2
  );
  ANALYZE tmp_analysis_match_agent_config_category_ids;

  -- unsolved matched ids for the given analysis which have the expected
  -- category/feature vector already and can be solved
  -- limited to _limit number of rows
  DROP TABLE IF EXISTS tmp_ae_unsolved_match_ids;

  CREATE TEMP TABLE tmp_ae_unsolved_match_ids ON COMMIT DROP AS (
    SELECT amms.alert_id
         , amms.match_id
    FROM tmp_ae_missing_match_solution amms
           JOIN tmp_analysis_match_agent_config_category_ids tamacci
                ON amms.match_id = tamacci.match_id
                  AND amms.alert_id = tamacci.alert_id
                  AND _category_feature_ids.category_ids = tamacci.category_ids
           JOIN tmp_analysis_match_agent_config_feature_ids tamacfi
                ON amms.match_id = tamacfi.match_id
                  AND amms.alert_id = tamacfi.alert_id
                  AND _category_feature_ids.feature_ids = tamacfi.agent_config_feature_ids
    LIMIT _limit
  );

  ANALYSE tmp_ae_unsolved_match_ids;
  -- exit if table is empty
  SELECT COUNT(*) INTO _count FROM (SELECT 1 FROM tmp_ae_unsolved_match_ids LIMIT 1) AS a;
  IF _count = 0 THEN
    RETURN;
  END IF;


  -- aggregate feature values, reasons and configs for limited unsolved matches
  DROP TABLE IF EXISTS tmp_feature_aggregates;
  CREATE TEMP TABLE tmp_feature_aggregates ON COMMIT DROP AS (
    SELECT ids.match_id,
           ids.alert_id,
           ARRAY_AGG(amfv.value ORDER BY amfv.agent_config_feature_id)        AS feature_values,
           ARRAY_AGG(amfv.reason ORDER BY amfv.agent_config_feature_id)       AS feature_reasons,
           ARRAY_AGG(aacf.agent_config ORDER BY amfv.agent_config_feature_id) AS agent_configs

    FROM tmp_ae_unsolved_match_ids ids
           JOIN ae_match_feature_value amfv ON ids.match_id = amfv.match_id
           JOIN ae_analysis_feature aaf
                ON amfv.agent_config_feature_id = aaf.agent_config_feature_id AND
                   aaf.analysis_id = _analysis_id
           JOIN ae_agent_config_feature aacf
                ON amfv.agent_config_feature_id = aacf.agent_config_feature_id
    GROUP BY 1, 2
  );
  ANALYZE tmp_feature_aggregates;

  -- aggregate category values for limited unsolved matches
  DROP TABLE IF EXISTS tmp_category_aggregates;
  CREATE TEMP TABLE tmp_category_aggregates ON COMMIT DROP AS (
    SELECT ids.alert_id,
           ids.match_id,
           ARRAY_AGG(amcv.value ORDER BY amcv.category_id) AS category_values
    FROM tmp_ae_unsolved_match_ids ids
           JOIN ae_match_category_value amcv ON ids.match_id = amcv.match_id
           JOIN ae_analysis_category aac
                ON amcv.category_id = aac.category_id AND aac.analysis_id = _analysis_id
    GROUP BY 1, 2
  );
  ANALYZE tmp_category_aggregates;

  -- return category values vector and feature values vector for the given matches and
  -- the given analysis
  RETURN QUERY SELECT taumi.alert_id,
                      taumi.match_id,
                      am.client_match_identifier,
                      aacaq.category_names,
                      tca.category_values,
                      aafaq.feature_names,
                      tfa.feature_values,
                      tfa.feature_reasons,
                      tfa.agent_configs
               FROM tmp_ae_unsolved_match_ids taumi
                      JOIN ae_match am ON am.match_id = taumi.match_id
                      JOIN ae_analysis_categories_agg_query aacaq
                           ON aacaq.analysis_id = _analysis_id
                      JOIN ae_analysis_features_agg_query aafaq
                           ON aafaq.analysis_id = _analysis_id
                      JOIN tmp_category_aggregates tca
                           ON taumi.match_id = tca.match_id
                      JOIN tmp_feature_aggregates tfa
                           ON taumi.match_id = tfa.match_id;
END;
$$;
