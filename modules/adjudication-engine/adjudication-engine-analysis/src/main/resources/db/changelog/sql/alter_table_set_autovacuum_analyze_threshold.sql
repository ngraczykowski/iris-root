ALTER TABLE ae_agent_exchange_match_feature
  SET (autovacuum_analyze_threshold = 2000000, autovacuum_analyze_scale_factor = 0, autovacuum_vacuum_threshold = 4000000, autovacuum_vacuum_scale_factor = 0);
ALTER TABLE ae_alert
  SET (autovacuum_analyze_threshold = 50000, autovacuum_analyze_scale_factor = 0, autovacuum_vacuum_threshold = 100000, autovacuum_vacuum_scale_factor = 0);
ALTER TABLE ae_alert_comment_input
  SET (autovacuum_analyze_threshold = 50000, autovacuum_analyze_scale_factor = 0, autovacuum_vacuum_threshold = 100000, autovacuum_vacuum_scale_factor = 0);
ALTER TABLE ae_analysis_alert
  SET (autovacuum_analyze_threshold = 50000, autovacuum_analyze_scale_factor = 0, autovacuum_vacuum_threshold = 100000, autovacuum_vacuum_scale_factor = 0);
ALTER TABLE ae_dataset_alert
  SET (autovacuum_analyze_threshold = 50000, autovacuum_analyze_scale_factor = 0, autovacuum_vacuum_threshold = 100000, autovacuum_vacuum_scale_factor = 0);
ALTER TABLE ae_match
  SET (autovacuum_analyze_threshold = 50000, autovacuum_analyze_scale_factor = 0, autovacuum_vacuum_threshold = 100000, autovacuum_vacuum_scale_factor = 0);
ALTER TABLE ae_match_category_value
  SET (autovacuum_analyze_threshold = 100000, autovacuum_analyze_scale_factor = 0, autovacuum_vacuum_threshold = 200000, autovacuum_vacuum_scale_factor = 0);
ALTER TABLE ae_match_feature_value
  SET (autovacuum_analyze_threshold = 1000000, autovacuum_analyze_scale_factor = 0, autovacuum_vacuum_threshold = 2000000, autovacuum_vacuum_scale_factor = 0);
ALTER TABLE ae_match_solution
  SET (autovacuum_analyze_threshold = 50000, autovacuum_analyze_scale_factor = 0, autovacuum_vacuum_threshold = 100000, autovacuum_vacuum_scale_factor = 0);
ALTER TABLE ae_recommendation
  SET (autovacuum_analyze_threshold = 50000, autovacuum_analyze_scale_factor = 0, autovacuum_vacuum_threshold = 100000, autovacuum_vacuum_scale_factor = 0);
