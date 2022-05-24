--
-- A hint submitted by a user: Oracle DB MUST be created as "shared" and the
-- job_queue_processes parameter  must be greater than 2
-- However, these settings are pretty much standard after any
-- Oracle install, so most users need not worry about this.
--
-- Many other users (including the primary author of Quartz) have had success
-- runing in dedicated mode, so only consider the above as a hint ;-)
--

DELETE FROM qrtz_fired_triggers;
DELETE FROM qrtz_simple_triggers;
DELETE FROM qrtz_simprop_triggers;
DELETE FROM qrtz_cron_triggers;
DELETE FROM qrtz_blob_triggers;
DELETE FROM qrtz_triggers;
DELETE FROM qrtz_job_details;
DELETE FROM qrtz_calendars;
DELETE FROM qrtz_paused_trigger_grps;
DELETE FROM qrtz_locks;
DELETE FROM qrtz_scheduler_state;

DROP TABLE qrtz_calendars;
DROP TABLE qrtz_fired_triggers;
DROP TABLE qrtz_blob_triggers;
DROP TABLE qrtz_cron_triggers;
DROP TABLE qrtz_simple_triggers;
DROP TABLE qrtz_simprop_triggers;
DROP TABLE qrtz_triggers;
DROP TABLE qrtz_job_details;
DROP TABLE qrtz_paused_trigger_grps;
DROP TABLE qrtz_locks;
DROP TABLE qrtz_scheduler_state;
