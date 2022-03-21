# importing the module

import base64
import os
import psycopg2
import psycopg2.extras
import requests
from psycopg2.extras import Json


SIMULATION_TABLE_NAME = 'warehouse_simulation_alert'
CREATE_PARITION_TEMPLATE = "CREATE TABLE IF NOT EXISTS warehouse_simulation_alert_%s PARTITION OF warehouse_simulation_alert FOR VALUES IN ('%s')"
INSERT_TEMPLATE = 'INSERT INTO warehouse_simulation_alert (name, analysis_name, created_at, payload) VALUES (%s, %s, %s, %s);'
FETCH_SIMULATION_PARTITION = '''
	SELECT
	    child.relname       AS partition_name
	FROM pg_inherits
	     JOIN pg_class parent        ON pg_inherits.inhparent = parent.oid
	     JOIN pg_class child         ON pg_inherits.inhrelid   = child.oid
	WHERE parent.relname = 'warehouse_simulation_alert';
'''

CREATE_TIME_FIELD = 'index_timestamp'
ALERT_NAME_FIELD = 's8_alert_name'
DISCRIMINATOR_FIELD = 's8_discriminator'
ES_PROPERTY_PREFIX = 'alert_'


ES_ADDRESS = os.environ['ES_URL']
ES_USER = os.environ['ES_USERNAME']
ES_PASSWORD = os.environ['ES_PASSWORD']

POSTGRES_HOST = os.environ['WAREHOUSE_DB_HOST']
POSTGRES_PORT = os.environ['WAREHOUSE_DB_PORT']
DB_NAME = os.environ['WAREHOUSE_DB_NAME']
DB_USERNAME = os.environ['WAREHOUSE_DB_USERNAME']
DB_PASSWORD = os.environ['WAREHOUSE_DB_PASSWORD']

CREDENTIALS = "%s:%s" %(ES_USER, ES_PASSWORD)
AUTH_HEADER = {'Authorization' : 'Basic %s' %base64.b64encode(CREDENTIALS.encode()).decode('ascii')}

conn = psycopg2.connect(host=POSTGRES_HOST, port=POSTGRES_PORT, dbname=DB_NAME, user=DB_USERNAME, password=DB_PASSWORD)

def fetch_rows_for_simulation(es_index):
	simulations = []
	index_name = es_index['index']
	response = requests.get(url = '%s/%s/_search?scroll=10m&size=10000' %(ES_ADDRESS, index_name), headers=AUTH_HEADER)
	data = response.json()
	simulations.extend(data['hits']['hits'])
	scroll_id = data['_scroll_id'];
	while True:
		print('Executing another scroll_id, current number of fetched rows: %s' %(len(simulations)))
		response = requests.get(url = '%s/_search/scroll?scroll=10m&scroll_id=%s' %(ES_ADDRESS, scroll_id), headers=AUTH_HEADER)
		data = response.json()
		new_simulations = data['hits']['hits']
		if (len(new_simulations) == 0 ):
			break
		simulations.extend(new_simulations)
	return simulations

def migrate_row(es_row, analysis_name, cur):
	simulation = es_row['_source']
	alert_name = simulation.pop(ALERT_NAME_FIELD)
	create_time = simulation.pop(CREATE_TIME_FIELD)
	simulation.pop(DISCRIMINATOR_FIELD)
	new_payload = {}
	for name, val in simulation.items():
		if name.startswith(ES_PROPERTY_PREFIX):
			new_payload[name.replace(ES_PROPERTY_PREFIX, "")] = val
	cur.execute(INSERT_TEMPLATE, [alert_name, analysis_name, create_time, Json(new_payload)])

def migrate_index(es_index, analysis_id):
	analysis_name = 'analysis/%s' %analysis_id
	cur = conn.cursor(cursor_factory=psycopg2.extras.DictCursor)
	cur.execute(CREATE_PARITION_TEMPLATE %(analysis_id, analysis_name));
	rows = fetch_rows_for_simulation(es_index)
	print("Starting migration for analysis_id: %s, there is rows: %s to be migrated" %(analysis_id, len(rows)))
	for row in rows:
		migrate_row(row, analysis_name, cur)
	conn.commit()
	print("Migration for analysis_id: %s finished and number of rows: %s were migrated" %(analysis_id, len(rows)))

def fetch_migrated_simulations():
	cur = conn.cursor(cursor_factory=psycopg2.extras.RealDictCursor)
	cur.execute(FETCH_SIMULATION_PARTITION)
	partitions = cur.fetchall()
	cur.close()	
	return partitions

def was_partition_migrated(migrated_partitions, partition_to_migrate):
	return any(partition['partition_name'] == partition_to_migrate for partition in migrated_partitions)

if __name__ == '__main__':
	response = requests.get(url = '%s/_cat/indices?format=json' %ES_ADDRESS, headers=AUTH_HEADER)
	es_indicies = response.json()
	migrated_simulations = fetch_migrated_simulations()
	for es_index in es_indicies:
		index_name = es_index['index']
		if '_simulation_' in index_name:
			analysis_id = index_name.split("_")[-1]
			partition_name_to_migrate = '%s_%s' %(SIMULATION_TABLE_NAME, analysis_id)
			if was_partition_migrated(migrated_simulations, partition_name_to_migrate):
				print ("Partition %s exists in psql, migration is not needed" %partition_name_to_migrate)
				continue
			migrate_index(es_index, analysis_id)
	print("All simulations were migrated")
	conn.close()
