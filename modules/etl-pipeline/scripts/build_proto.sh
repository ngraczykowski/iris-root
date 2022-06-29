cp -R etl-pipeline-api/etl-api/src/main/proto/silenteight/datascience/etlpipeline/v1/api/ etl_component/proto
cd etl_component/proto
python -m grpc_tools.protoc --proto_path=./ --python_out=./ --grpc_python_out=./ etl_pipeline.proto
touch __init__.py