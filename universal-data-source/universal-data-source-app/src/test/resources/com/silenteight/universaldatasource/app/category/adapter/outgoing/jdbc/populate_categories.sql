INSERT INTO uds_category (category_id, category_display_name, category_type, allowed_values, multi_value)
VALUES
       ('categories/categoryOne', 'displayNameOne','ENUMERATED', 'YES,NO', false),
       ('categories/categoryTwo', 'displayNameTwo','ENUMERATED', 'YES,NO', false),
       ('categories/categoryThree', 'displayNameThree','ENUMERATED', 'YES,NO', false),
       ('categories/categoryFour', 'displayNameFour','ENUMERATED', 'YES,NO', false);


INSERT INTO uds_category_value(category_id, alert_name, match_name, category_value)
VALUES
  ('categories/categoryOne', 'alerts/1', 'alerts/1/matches/1', 'YES'),
  ('categories/categoryTwo', 'alerts/1', 'alerts/1/matches/1', 'NO'),
  ('categories/categoryThree', 'alerts/1', 'alerts/1/matches/1', 'YES'),
  ('categories/categoryOne', 'alerts/2', 'alerts/2/matches/2', 'YES'),
  ('categories/categoryTwo', 'alerts/2', 'alerts/2/matches/2', 'NO'),
  ('categories/categoryThree', 'alerts/2', 'alerts/2/matches/2', 'YES'),
  ('categories/categoryOne', 'alerts/3', 'alerts/3/matches/3', 'YES'),
  ('categories/categoryOne', 'alerts/4', 'alerts/4/matches/4', 'YES'),
  ('categories/categoryOne', 'alerts/5', 'alerts/5/matches/5', 'YES'),
  ('categories/categoryOne', 'alerts/6', 'alerts/6/matches/6', 'YES'),
  ('categories/categoryOne', 'alerts/7', 'alerts/7/matches/7', 'YES'),
  ('categories/categoryOne', 'alerts/8', 'alerts/8/matches/8', 'YES'),
  ('categories/categoryThree', 'alerts/9', 'alerts/9/matches/9', 'YES'),
  ('categories/categoryThree', 'alerts/10', 'alerts/10/matches/10', 'YES'),
  ('categories/categoryThree', 'alerts/11', 'alerts/11/matches/11', 'YES');
