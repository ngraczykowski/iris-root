DELETE
FROM ae_match_solution
WHERE match_solution_id NOT IN
      (SELECT max(match_solution_id)
       FROM ae_match_solution
       GROUP BY analysis_id, match_id
       HAVING count(*) > 1)
