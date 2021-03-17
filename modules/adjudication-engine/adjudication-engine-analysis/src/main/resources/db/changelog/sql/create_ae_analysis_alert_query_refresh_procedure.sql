CREATE OR REPLACE FUNCTION refresh_ae_analysis_alert_query() RETURNS trigger
LANGUAGE plpgsql
as $$
    begin
        execute 'REFRESH MATERIALIZED VIEW ae_analysis_alert_query';
        RETURN NEW;
    end;
$$
