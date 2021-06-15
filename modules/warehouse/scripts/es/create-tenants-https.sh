curl -X PUT "https://localhost:9200/_opendistro/_security/api/tenants/local_production_ai_reasoning" \
-u admin:admin \
--cacert "${CURRENTDIR}/root-ca.pem" \
-H 'Content-Type: application/json' \
-d '{"description": "AI reasoning"}'

curl -X PUT "https://localhost:9200/_opendistro/_security/api/tenants/local_production_accuracy" \
-u admin:admin \
--cacert "${CURRENTDIR}/root-ca.pem" \
-H 'Content-Type: application/json' \
-d '{"description": "Accuracy"}'

curl -X PUT "https://localhost:9200/_opendistro/_security/api/tenants/local_production_periodic" \
-u admin:admin \
--cacert "${CURRENTDIR}/root-ca.pem" \
-H 'Content-Type: application/json' \
-d '{"description": "Reports created periodically"}'

curl -X PUT "https://localhost:9200/_opendistro/_security/api/tenants/local_simulation_master" \
--cacert "${CURRENTDIR}/root-ca.pem" \
-u admin:admin \
-H 'Content-Type: application/json' \
-d '{"description": "Master tenant for new simulations"}'
