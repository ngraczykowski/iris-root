Application uses locust framework to load-test.

It uses externally available JSON with input data. Variable `TBT_REQUEST_FILES_DIR` directs to JSON files.

You may also provide `TBT_DUMP_RESPONSE=1` to dump all responses.

Invoke locust to test it.

## Installation

```
cd load-test
python3 -m venv venv
source venv/bin/activate
pip install --upgrade pip setuptools wheel
pip install -r requirements.txt
```

## Example

Example how to test locally:

```
TBT_DUMP_RESPONSE=0 TBT_REQUEST_FILES_DIR=../docs/api/hq-messages locust -H http://localhost:24602/rest/pb -u 1 -r 1
```

To test in on `preprod` or `dev` environment you need to pass a token:


```
# 1 with CLIENT_SECRET -> getting TOKEN 
TBT_DUMP_RESPONSE=0 TBT_REQUEST_FILES_DIR=../docs/api/hq-messages CLIENT_SECRET=XXXXX locust -H https://sierra-dev.silent8.cloud/rest/pb -u 1 -r 1
# 2. or with TOKEN (this ovveride CLIENT_SECRET processing)
TBT_DUMP_RESPONSE=0 TBT_REQUEST_FILES_DIR=../docs/api/hq-messages TOKEN=eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICIyaE5ZMTF4OHNETWczc09uNnVZVkl3LThxdXM2aEJ2Q2NES2pXUXYwOFNZIn0.eyJleHAiOjE2NDg4MDkwMTMsImlhdCI6MTY0ODgwODcxMywianRpIjoiNzY1M2RiMzAtMmQ3Mi00ZTVmLWE0YjAtMGM2OGU4YzhjNWZhIiwiaXNzIjoiaHR0cHM6Ly9hdXRoLnNpbGVudDguY2xvdWQvcmVhbG1zL3NpZXJyYSIsInN1YiI6IjFhMTUxYjdlLTJjM2UtNGRmMi1iMDZhLWIzYWY5NjRhYjJiOSIsInR5cCI6IkJlYXJlciIsImF6cCI6InNpZXJyYS1kZXYtYXBpIiwiYWNyIjoiMSIsInJlc291cmNlX2FjY2VzcyI6eyJzaWVycmEtZGV2LWFwaSI6eyJyb2xlcyI6WyJ3cml0ZV9hbGVydCJdfX0sInNjb3BlIjoicHJvZmlsZSBjbWFwaSIsImNsaWVudElkIjoic2llcnJhLWRldi1hcGkiLCJjbGllbnRIb3N0IjoiMTAuMTAwLjEuMTYxIiwicm9sZXMiOlsid3JpdGVfYWxlcnQiXSwicHJlZmVycmVkX3VzZXJuYW1lIjoic2VydmljZS1hY2NvdW50LXNpZXJyYS1kZXYtYXBpIiwiY2xpZW50QWRkcmVzcyI6IjEwLjEwMC4xLjE2MSJ9.FjbJfCXl7P2O9DxXszA7yJEMiEvJv8YU1oK63j10dIeXZaXR4AYVYMp0AFRjuaRZDUkASQT42D215YrTP7oxIVCmchcJTiMNtArayWsvQuijb5UpijGWE8krxBMkVii9_Z2LJsXK1imrk5-2wsvkfcpngnTWfRUOQhH42-QiScr5HXNJ9lC_gfcEzMOmWbEGIG7wATWAa01mpKCQuOo_ZuqWNYC43-C_uNk1w0Kce63AFNISRyiS0NdopKrI-mx0vLr1lwjHwGcoLfdUhzvZNYTaOQ9BahRmv48UbElXWC71RoJBLa3seMuZ0c0zcvDBxWJECrErG4wXjBq_OrbUyQ locust -H https://sierra-dev.silent8.cloud/rest/pb -u 1 -r 1
TBT_DUMP_RESPONSE=0 TBT_REQUEST_FILES_DIR=../docs/api/hq-messages TOKEN=XXXXX locust -H https://sierra-preprod.silent8.cloud/rest/pb -u 1 -r 1
```

If you want to learn more, try:
```
TBT_DUMP_RESPONSE=0 TBT_REQUEST_FILES_DIR=../docs/api/hq-messages locust --help
```
