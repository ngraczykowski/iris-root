apiVersion: v1
kind: ConfigMap
metadata:
  name: {{ include "sear.componentName" . }}-init-scripts
  labels:
    {{- include "sear.componentLabels" . | nindent 4 }}
data:
  000mkApplicationYaml.sh: |
    #!/bin/sh
    echo "Generating application.yaml"
    cat > /tmp/application.yaml << EOF
    agent:
      processes: 10
      agent-exchange:
        request:
          exchange: 'agent.request'
          routing-key: '{{ .component.amqp.routingKey }}'
          queue-name: '{{ .component.amqp.queueName }}'
        response:
          exchange: 'agent.response'
      grpc:
        port: 9090
    grpc:
      client:
        data-source:
          address: $UDS_ADDRESS
          timeout: 15
    rabbitmq:
      host: $RABBITMQ_HOST
      port: $RABBITMQ_PORT
      login: $RABBITMQ_USERNAME
      password: $RABBITMQ_PASSWORD
      virtualhost: /
    sentry:
      turn_on: false
      environment: unknown
      release: unknown
      sample_rate: 1.0
    {{- if .component.additionalConfig }}
    {{- .component.additionalConfig | toYaml | nindent 4}}
    {{- end }}
    EOF
