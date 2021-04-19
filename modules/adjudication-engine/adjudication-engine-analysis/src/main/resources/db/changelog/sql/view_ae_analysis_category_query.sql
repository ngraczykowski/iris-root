SELECT aac.analysis_category_id,
       aac.analysis_id,
       ac.category_id,
       ac.category
FROM ae_analysis_category aac
         JOIN ae_category ac on aac.category_id = ac.category_id;
