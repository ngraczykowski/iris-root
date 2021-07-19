curl -X PUT "https://localhost:9200/_index_template/analysis" \
  -u admin:admin \
  --cacert "${CURRENTDIR}/root-ca.pem" \
  -H 'Content-Type: application/json' \
  -d "@index-template-analysis.json"
