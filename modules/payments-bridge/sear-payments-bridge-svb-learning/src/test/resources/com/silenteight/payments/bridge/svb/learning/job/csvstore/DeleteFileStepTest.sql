DELETE
FROM pb_learning_file
WHERE file_name LIKE 'analystdecison-2-hits.csv';
INSERT INTO pb_learning_file(learning_file_id, file_name, bucket_name, created_at, status)
VALUES (345, 'analystdecison-2-hits.csv', 'bucket', now(), 'TRIGGERED');
