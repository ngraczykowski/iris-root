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
        silenteight.com/config-checksum: {{ tpl ($.Files.Get "component/spring/configmap.yaml.tpl") . | sha256sum }}
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
      {{- if .component.initScripts }}
      initContainers:
        - name: init-scripts
          image: busybox
          command: ['sh', '-c', "run-parts /var/run/initScripts"]
          volumeMounts:
            - name: init-scripts
              mountPath: "/var/run/initScripts"
              readOnly: true
            - name: tmp
              mountPath: "/tmp"
              readOnly: false
          env:
            - name: RABBITMQ_HOST
              valueFrom:
                secretKeyRef:
                  name: {{ include "sear.rabbitmqSecretName" . }}
                  key: host
            - name: RABBITMQ_PORT
              valueFrom:
                secretKeyRef:
                  name: {{ include "sear.rabbitmqSecretName" . }}
                  key: port
            - name: RABBITMQ_USERNAME
              valueFrom:
                secretKeyRef:
                  name: {{ include "sear.rabbitmqSecretName" . }}
                  key: username
            - name: RABBITMQ_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: {{ include "sear.rabbitmqSecretName" . }}
                  key: password
            - name: UDS_ADDRESS
              value: {{ include "sear.fullname" . }}-universal-data-source.{{ .Release.Namespace }}.svc:9090
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
            {{- if .component.containerPorts.grpc.enabled }}
            - name: grpc
              containerPort: {{ .component.containerPorts.grpc.port }}
              protocol: TCP
            {{- end }}
          {{- with .component.resources }}
          resources:
            {{- toYaml . | nindent 12 }}
          {{- end }}
          {{- if .component.command }}
          command:
            {{- toYaml .component.command | nindent 12 }}
          {{- end }}
          args:
            {{- if .component.args }}
              {{- toYaml .component.args | nindent 12 }}
            {{- end }}
          volumeMounts:
            - name: config
              mountPath: "/var/run/configs/"
              readOnly: true
            - name: secret-rabbitmq
              mountPath: "/var/run/secrets/rabbitmq"
              readOnly: true
            {{- if .component.initScripts }}
            - name: init-scripts
              mountPath: "/var/run/initScripts"
              readOnly: true
            - name: tmp
              mountPath: "/tmp"
              readOnly: false
            - name: tmp
              mountPath: "/app/config/application.yaml"
              readOnly: true
              subPath: application.yaml
            {{- end }}
          env:
      volumes:
        - name: config
          configMap:
            name: {{ include "sear.componentName" . }}
        - name: secret-rabbitmq
          secret:
            secretName: {{ include "sear.rabbitmqSecretName" . }}
        {{- if .component.initScripts }}
        - name: init-scripts
          configMap:
            defaultMode: 0777
            name: {{ include "sear.componentName" . }}-init-scripts
        - name: tmp
          emptyDir: {}
        {{- end }}
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
