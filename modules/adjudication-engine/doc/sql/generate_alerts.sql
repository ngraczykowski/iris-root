CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

----------------------------------------------------------------------------------------------------
-- GENERATING ALERTS
----------------------------------------------------------------------------------------------------
INSERT INTO ae_alert (client_alert_identifier, created_at, alerted_at, priority)
SELECT concat((ARRAY ['PL','US','GB','SG','AE','IN'])[ceil(random() * 6)]
           , '_'
           , (ARRAY ['PERD','EMPL'])[ceil(random() * 2)]
           , '_'
           , (ARRAY ['DENY','PEPL','AM'])[ceil(random() * 3)]
           , '!'
           , upper(cast(uuid_generate_v4() as VARCHAR))) as client_alert_identifier,
       now()                                             as created_at,
       timestamp '2021-04-01 08:00:00' + random() * (timestamp '2021-05-10 15:00:00' -
                                                     timestamp '2021-04-01 08:00:00')
                                                         as alerted_at,
       ceil(random() * 10)                                  priority
FROM generate_series(1, 1000000);

----------------------------------------------------------------------------------------------------
-- GENERATING MATCHES
----------------------------------------------------------------------------------------------------
INSERT INTO ae_match(alert_id, created_at, client_match_identifier)
SELECT 2 + ceil(random() * ((SELECT max(alert_id) FROM ae_alert) - 2)) as alert_id,
       now()                                                           as created_at,
       concat((ARRAY ['AS','DB','GSN','CL','AZ','AM'])[ceil(random() * 6)]
           , LPAD((ceil(random() * 100000))::text, 8, '0'))            as client_match_identifier
FROM generate_series(1, 2500000)
ON CONFLICT DO NOTHING;

-- Set correct sort_index
UPDATE ae_match
SET sort_index = match_ordered.rn
FROM (
         SELECT alert_id,
                match_id,
                row_number() over (partition by alert_id order by client_match_identifier) as rn
         FROM ae_match
     ) match_ordered
WHERE ae_match.alert_id > 2
  AND ae_match.match_id = match_ordered.match_id;
