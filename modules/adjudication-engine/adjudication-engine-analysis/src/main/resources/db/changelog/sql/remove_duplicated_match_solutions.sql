WITH duplicates AS (
    SELECT match_solution_id
         , ROW_NUMBER() OVER (PARTITION BY analysis_id, match_id
        ORDER BY match_solution_id DESC) AS row_num
    FROM ae_match_solution
)
DELETE
FROM ae_match_solution
WHERE match_solution_id IN (
    SELECT match_solution_id
    FROM duplicates
    WHERE duplicates.row_num > 1);
