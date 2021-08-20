curl -X PUT "http://localhost:9200/_index_template/analysis" \
  -u admin:admin \
  -H 'Content-Type: application/json' \
  -d "@index-template-analysis.json"

