curl -X POST "http://localhost:9200/_opendistro/_sql" \
-u admin:admin \
-H 'Content-Type: application/json' \
-d \
"{
    \"query\": \"select s8_country, count(*) from local_production where (index_timestamp >= timestamp('2021-04-15 12:17:37.098') and index_timestamp < timestamp('2021-06-18 12:10:31.098')) group by s8_country\"
}"
