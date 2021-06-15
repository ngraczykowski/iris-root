curl -X PUT "http://localhost:9200/_opendistro/_security/api/tenants/local_production_ai_reasoning" \
-u admin:admin \
-H 'Content-Type: application/json' \
-d '{"description": "AI reasoning"}'

curl -X PUT "http://localhost:9200/_opendistro/_security/api/tenants/local_production_accuracy" \
-u admin:admin \
-H 'Content-Type: application/json' \
-d '{"description": "Accuracy"}'

curl -X PUT "http://localhost:9200/_opendistro/_security/api/tenants/local_production_periodic" \
-u admin:admin \
-H 'Content-Type: application/json' \
-d '{"description": "Reports created periodically"}'

curl -X PUT "http://localhost:9200/_opendistro/_security/api/tenants/local_simulation_master" \
-u admin:admin \
-H 'Content-Type: application/json' \
-d '{"description": "Master tenant for new simulations"}'
