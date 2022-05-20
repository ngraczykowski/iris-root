SELECT aac.analysis_id                                                                       as analysis_id,
       ac.category                                                                           as category,
       ac.category_id                                                                        as category_id,
       to_jsonb(json_agg(json_build_object('matchId', am.match_id, 'alertId', am.alert_id))) as match_alert
FROM ae_pending_recommendation apr
       JOIN ae_match am ON am.alert_id = apr.alert_id
       JOIN ae_analysis_category aac ON apr.analysis_id = aac.analysis_id
       JOIN ae_category ac ON ac.category_id = aac.category_id
       LEFT JOIN ae_match_category_value amcv
                 ON amcv.match_id = am.match_id AND amcv.category_id = aac.category_id
WHERE amcv.category_id IS NULL
GROUP BY ac.category_id, ac.category, aac.analysis_id
ORDER BY ac.category
