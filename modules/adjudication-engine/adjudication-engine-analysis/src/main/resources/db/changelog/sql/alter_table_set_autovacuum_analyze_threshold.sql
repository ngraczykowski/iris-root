ALTER TABLE ae_agent_exchange_match_feature
  SET (autovacuum_analyze_threshold = 400000, autovacuum_analyze_scale_factor = 0, autovacuum_vacuum_threshold = 800000, autovacuum_vacuum_scale_factor = 0);
ALTER TABLE ae_alert
  SET (autovacuum_analyze_threshold = 20000, autovacuum_analyze_scale_factor = 0, autovacuum_vacuum_threshold = 40000, autovacuum_vacuum_scale_factor = 0);
ALTER TABLE ae_alert_comment_input
  SET (autovacuum_analyze_threshold = 20000, autovacuum_analyze_scale_factor = 0, autovacuum_vacuum_threshold = 40000, autovacuum_vacuum_scale_factor = 0);
ALTER TABLE ae_analysis_alert
  SET (autovacuum_analyze_threshold = 20000, autovacuum_analyze_scale_factor = 0, autovacuum_vacuum_threshold = 40000, autovacuum_vacuum_scale_factor = 0);
ALTER TABLE ae_dataset_alert
  SET (autovacuum_analyze_threshold = 20000, autovacuum_analyze_scale_factor = 0, autovacuum_vacuum_threshold = 40000, autovacuum_vacuum_scale_factor = 0);
ALTER TABLE ae_match
  SET (autovacuum_analyze_threshold = 20000, autovacuum_analyze_scale_factor = 0, autovacuum_vacuum_threshold = 40000, autovacuum_vacuum_scale_factor = 0);
ALTER TABLE ae_match_category_value
  SET (autovacuum_analyze_threshold = 40000, autovacuum_analyze_scale_factor = 0, autovacuum_vacuum_threshold = 80000, autovacuum_vacuum_scale_factor = 0);
ALTER TABLE ae_match_feature_value
  SET (autovacuum_analyze_threshold = 400000, autovacuum_analyze_scale_factor = 0, autovacuum_vacuum_threshold = 800000, autovacuum_vacuum_scale_factor = 0);
ALTER TABLE ae_match_solution
  SET (autovacuum_analyze_threshold = 20000, autovacuum_analyze_scale_factor = 0, autovacuum_vacuum_threshold = 40000, autovacuum_vacuum_scale_factor = 0);
ALTER TABLE ae_recommendation
  SET (autovacuum_analyze_threshold = 20000, autovacuum_analyze_scale_factor = 0, autovacuum_vacuum_threshold = 40000, autovacuum_vacuum_scale_factor = 0);
