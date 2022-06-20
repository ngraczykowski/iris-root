-- NOTE: this procedure removes expired partitions that are older than expired_date parameter
-- in order to work properly the name of partition
-- must have the following pattern 'partition_prefix_YYYY_MM'

-- Example: CALL drop_expired_partitions('scb_raw_alert', 'raw_alert_', '2022-01-01');
-- It will remove all partitions from scb_raw_alert table with prefix name 'raw_alert_'
-- that are older than '2022-01-01'

CREATE OR REPLACE PROCEDURE drop_expired_partitions(master_table VARCHAR,
                                                    partition_prefix_name VARCHAR,
                                                    expired_date DATE)
    LANGUAGE plpgsql
AS
$$
    DECLARE
        arow record;
    BEGIN
        -- Create temporary table to keep list of partitions of given master table
        CREATE TEMPORARY TABLE temp_partition_table_names AS
        SELECT inhrelid::regclass::text                                                                   AS partition_name,
               TO_DATE(SUBSTRING(inhrelid::regclass::text, LENGTH(partition_prefix_name) + 1), 'YYYY_MM') AS partition_date
        FROM pg_catalog.pg_inherits
        WHERE inhparent = master_table::regclass
        AND inhrelid::regclass::text LIKE partition_prefix_name || '%';

        -- Delete partitions older than expired_date
        FOR arow IN SELECT *
                    FROM temp_partition_table_names
                    WHERE partition_date < expired_date
            LOOP
                EXECUTE FORMAT('ALTER TABLE %I DETACH PARTITION %I', master_table, arow.partition_name);
                EXECUTE FORMAT('DROP TABLE %I', arow.partition_name);
                RAISE NOTICE '[%] Partition % has been removed', NOW(), arow.partition_name;
            END LOOP;

        DROP TABLE temp_partition_table_names;
    END;
$$;
