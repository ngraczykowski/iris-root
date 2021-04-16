INSERT INTO ae_category(category, created_at)
    (SELECT DISTINCT category, now()
     FROM ae_analysis_category_migration
     ORDER BY category);

INSERT INTO ae_analysis_category(analysis_id, category_id)
    (SELECT aacm.analysis_id, ac.category_id
     FROM ae_analysis_category_migration aacm
              JOIN ae_category ac
                   on aacm.category = ac.category);
