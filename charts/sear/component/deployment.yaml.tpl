apiVersion: apps/v1
kind: Deployment
metadata:
  name: {{ include "sear.componentName" . }}
  labels:
    {{- include "sear.componentLabels" . | nindent 4 }}
spec:
  {{- if not .component.autoscaling.enabled }}
  replicas: {{ .component.replicaCount }}
  {{- end }}
  selector:
    matchLabels:
      {{- include "sear.componentSelectorLabels" . | nindent 6 }}
  template:
    metadata:
      annotations:
        fluentbit.io/parser: spring-logback-json
        {{- with .Values.podAnnotations }}
          {{- toYaml . | nindent 8 }}
        {{- end }}
      labels:
        {{- include "sear.componentSelectorLabels" . | nindent 8 }}
    spec:
      {{- with .component.image.pullSecrets }}
      imagePullSecrets:
        {{- toYaml . | nindent 8 }}
      {{- end }}
      serviceAccountName: {{ include "sear.serviceAccountName" . }}
      {{- with .Values.podSecurityContext }}
      securityContext:
        {{- toYaml . | nindent 8 }}
      {{- end }}
      containers:
        - name: {{ .componentName }}
          {{- with .Values.securityContext }}
          securityContext:
            {{- toYaml . | nindent 12 }}
          {{- end }}
          image: "{{ .component.image.repository }}:{{ .component.image.tag | default .Chart.AppVersion }}"
          imagePullPolicy: {{ .component.image.pullPolicy }}
          ports:
            - name: http
              containerPort: {{ .component.containerPorts.http.port }}
              protocol: TCP
            - name: management
              containerPort: {{ .component.containerPorts.management.port }}
              protocol: TCP
            {{- if .component.containerPorts.grpc.enabled }}
            - name: grpc
              containerPort: {{ .component.containerPorts.grpc.port }}
              protocol: TCP
            {{- end }}
          {{- if .component.startupProbe.enabled }}
          startupProbe:
            httpGet:
              path: /management/actuator/health/liveness
              port: management
            initialDelaySeconds: {{ .component.startupProbe.initialDelaySeconds }}
            periodSeconds: {{ .component.startupProbe.periodSeconds }}
            timeoutSeconds: {{ .component.startupProbe.timeoutSeconds }}
            failureThreshold: {{ .component.startupProbe.failureThreshold }}
          {{- end }}
          {{- if .component.livenessProbe.enabled }}
          livenessProbe:
            httpGet:
              path: /management/actuator/health/liveness
              port: management
            periodSeconds: {{ .component.livenessProbe.periodSeconds }}
            timeoutSeconds: {{ .component.livenessProbe.timeoutSeconds }}
            failureThreshold: {{ .component.livenessProbe.failureThreshold }}
          {{- end }}
          {{- if .component.readinessProbe.enabled }}
          readinessProbe:
            httpGet:
              path: /management/actuator/health/readiness
              port: management
            periodSeconds: {{ .component.readinessProbe.periodSeconds }}
            timeoutSeconds: {{ .component.readinessProbe.timeoutSeconds }}
            successThreshold: {{ .component.readinessProbe.successThreshold }}
            failureThreshold: {{ .component.readinessProbe.failureThreshold }}
          {{- end }}
          {{- with .component.resources }}
          resources:
            {{- toYaml . | nindent 12 }}
          {{- end }}
          args:
            - --spring.application.name={{ .componentName }}
            - --server.servlet.context-path=/rest/{{ .component.webPath }}
            - --spring.webflux.base-path=/rest/{{ .component.webPath }}
            - --spring.config.additional-location=file:/etc/spring/config/,optional:file:/etc/spring/config/secrets.yml
            - --logging.config=file:/etc/spring/config/logback.xml
            - --server.port={{ .component.containerPorts.http.port }}
            - --management.server.port={{ .component.containerPorts.management.port }}
            - --management.server.base-path=/management
            - --management.endpoints.web.base-path=/actuator
            - --spring.datasource.url={{ include "sear.spring.jdbcUrl" . }}
            # NOTE(ahaczewski): After Tadeusz Kleszcz recommendation, disable unhealthy on exceptions when communicating via gRPC.
            - --spring.autoconfigure.exclude=net.devh.boot.grpc.client.autoconfigure.GrpcClientHealthAutoConfiguration
            {{- if .component.containerPorts.grpc.enabled }}
            - --grpc.server.port={{ .component.containerPorts.grpc.port }}
            {{- end }}
            {{- if .component.args }}
              {{- toYaml .component.args | nindent 12 }}
            {{- end }}
          volumeMounts:
            - name: config
              mountPath: "/etc/spring/config"
              readOnly: true
            - name: secret-rabbitmq
              mountPath: "/var/run/secrets/spring/rabbitmq"
              readOnly: true
            - name: secret-postgres
              mountPath: "/var/run/secrets/spring/postgres"
              readOnly: true
      volumes:
        - name: config
          configMap:
            name: {{ include "sear.componentName" . }}
        - name: secret-rabbitmq
          secret:
            secretName: {{ include "sear.rabbitmqSecretName" . }}
            items:
              - key: host
                path: spring/rabbitmq/host
              - key: port
                path: spring/rabbitmq/port
              - key: username
                path: spring/rabbitmq/username
              - key: password
                path: spring/rabbitmq/password
        - name: secret-postgres
          secret:
            secretName: {{ include "sear.postgresqlSecretName" . }}
            items:
              - key: username
                path: spring/datasource/username
              - key: password
                path: spring/datasource/password
      {{- with .component.nodeSelector }}
      nodeSelector:
        {{- toYaml . | nindent 8 }}
      {{- end }}
      {{- with .component.affinity }}
      affinity:
        {{- toYaml . | nindent 8 }}
      {{- end }}
      {{- with .component.tolerations }}
      tolerations:
        {{- toYaml . | nindent 8 }}
      {{- end }}
