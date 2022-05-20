INSERT INTO uds_category (category_id, category_display_name, category_type, allowed_values, multi_value)
VALUES
       ('categories/chineseCode', 'Chinese Commercial Code Category','ENUMERATED', 'YES,NO', false),
       ('categories/delimiter', 'Delimiter Category','ENUMERATED', 'YES,NO', false),
       ('categories/firstTokenAddress', 'Matchtext First Token of Address Category','ENUMERATED', 'YES,NO', false),
       ('categories/historicalRiskAssessment', 'Historical Risk Assessment','ENUMERATED', 'YES,NO', false);


INSERT INTO uds_category_value(category_id, alert_name, match_name, category_value)
VALUES
  ('categories/chineseCode', 'alerts/1', 'alerts/1/matches/1', 'YES'),
  ('categories/delimiter', 'alerts/1', 'alerts/1/matches/1', 'NO'),
  ('categories/firstTokenAddress', 'alerts/1', 'alerts/1/matches/1', 'YES'),
  ('categories/chineseCode', 'alerts/2', 'alerts/2/matches/2', 'YES'),
  ('categories/delimiter', 'alerts/2', 'alerts/2/matches/2', 'NO'),
  ('categories/firstTokenAddress', 'alerts/2', 'alerts/2/matches/2', 'YES'),
  ('categories/chineseCode', 'alerts/3', 'alerts/3/matches/3', 'YES'),
  ('categories/chineseCode', 'alerts/4', 'alerts/4/matches/4', 'YES'),
  ('categories/chineseCode', 'alerts/5', 'alerts/5/matches/5', 'YES'),
  ('categories/chineseCode', 'alerts/6', 'alerts/6/matches/6', 'YES'),
  ('categories/chineseCode', 'alerts/7', 'alerts/7/matches/7', 'YES'),
  ('categories/chineseCode', 'alerts/8', 'alerts/8/matches/8', 'YES'),
  ('categories/firstTokenAddress', 'alerts/9', 'alerts/9/matches/9', 'YES'),
  ('categories/firstTokenAddress', 'alerts/10', 'alerts/10/matches/10', 'YES'),
  ('categories/firstTokenAddress', 'alerts/11', 'alerts/11/matches/11', 'YES');
