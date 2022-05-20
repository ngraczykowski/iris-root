SELECT aa.analysis_id,
       array_agg(aac.category_id ORDER BY aac.category_id) as category_ids,
       array_agg(ac.category ORDER BY ac.category_id)      as category_names
FROM ae_analysis aa
         JOIN ae_analysis_category aac on aa.analysis_id = aac.analysis_id
         JOIN ae_category ac on aac.category_id = ac.category_id
GROUP BY 1
