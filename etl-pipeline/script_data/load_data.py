import json

import psycopg2

ALERT_NAME = 50430

conn = psycopg2.connect(
    database="mike-adjudication-engine",
    user="mike-adjudication-engine",
    password="mRgETm0CSYGwu0goSUARWD8Ne0ybDZu6",
    host="10.8.0.4",
    port="20764",
)
c = conn.cursor()


c.execute(f"SELECT match_contexts FROM ae_recommendation WHERE alert_id={ALERT_NAME}")
with open("reason.json", "w") as f:
    json.dump(c.fetchall()[0], f, indent=4)
conn.close()

conn = psycopg2.connect(
    database="mike-universal-data-source",
    user="mike-universal-data-source",
    password="BwfeZRQIXGqumcDPGt7ZalCfDlydc3dc",
    host="10.8.0.4",
    port="25227",
)
c = conn.cursor()
c.execute(
    f"SELECT agent_input FROM uds_feature_input WHERE alert_name LIKE '%alerts/{ALERT_NAME}%' ORDER BY created_at LIMIT 10"
)
with open("feature_input.json", "w") as f:
    for line in c.fetchall():
        f.write(str(line) + "\n")
conn.close()


conn = psycopg2.connect(
    database="mike-core-bridge",
    user="mike-core-bridge",
    password="EW8F304xyIHDzLBASE9xz9Zp",
    host="10.8.0.3",
    port="27746",
)
c = conn.cursor()

c.execute(f"SELECT alert_id FROM core_bridge_alerts WHERE name='alerts/{ALERT_NAME}'")
customer_alert_name = c.fetchall()[0][0]
conn.close()

conn = psycopg2.connect(
    database="mike-ms-bridge",
    user="mike-ms-bridge",
    password="wUeFGAPB184s9vHAZjBk6qoM",
    host="10.8.0.3",
    port="29724",
)
c = conn.cursor()


c.execute(
    f"SELECT payload, supplemental_info FROM ms_bridge_customer_alerts where alert_id = '{customer_alert_name}'"
)
xml, supplemental_info = c.fetchall()[0]

with open("data.xml", "w") as f:
    f.write(xml)

with open("supplemental_info.json", "w") as f:
    json.dump(supplemental_info, f, indent=4)
