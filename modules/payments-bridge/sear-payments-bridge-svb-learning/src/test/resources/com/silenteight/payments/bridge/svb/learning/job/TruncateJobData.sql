DELETE
FROM pb_batch_job_execution_context;
DELETE
FROM pb_batch_step_execution_context;
DELETE
FROM pb_batch_step_execution;
DELETE
FROM pb_batch_job_execution_params;
DELETE
FROM pb_batch_job_execution;
DELETE
FROM pb_batch_job_instance;

ALTER SEQUENCE pb_batch_job_execution_seq RESTART WITH 1;
ALTER SEQUENCE pb_batch_job_seq RESTART WITH 1;
ALTER SEQUENCE pb_batch_step_execution_seq RESTART WITH 1;

DELETE
FROM pb_learning_csv_row;
DELETE
FROM pb_learning_alert;
DELETE
FROM pb_learning_hit;
DELETE
FROM pb_learning_action;
DELETE
FROM pb_learning_file;
