{{/*
Expand the name of the chart.
*/}}
{{- define "sear.name" -}}
  {{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "sear.fullname" -}}
  {{- if .Values.fullnameOverride }}
    {{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
  {{- else }}
    {{- $name := default .Chart.Name .Values.nameOverride }}
    {{- if contains $name .Release.Name }}
      {{- .Release.Name | trunc 63 | trimSuffix "-" }}
    {{- else }}
      {{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
    {{- end }}
  {{- end }}
{{- end }}

{{/*
Create a default fully qualified component name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
*/}}
{{- define "sear.componentName" -}}
  {{- printf "%s-%s" (include "sear.fullname" .) .componentName | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create chart name and version as used by the chart label.
*/}}
{{- define "sear.chart" -}}
  {{- printf "%s-%s" .Chart.Name .Chart.Version | replace "+" "_" | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "sear.labels" -}}
helm.sh/chart: {{ include "sear.chart" . }}
{{ include "sear.selectorLabels" . }}
{{- if .Chart.AppVersion }}
app.kubernetes.io/version: {{ .Chart.AppVersion | quote }}
{{- end }}
app.kubernetes.io/managed-by: {{ .Release.Service }}
{{- end }}

{{/*
Selector labels
*/}}
{{- define "sear.selectorLabels" -}}
app.kubernetes.io/name: {{ include "sear.name" . }}
app.kubernetes.io/instance: {{ .Release.Name }}
{{- end }}

{{/*
Component labels
*/}}
{{- define "sear.componentLabels" -}}
{{- include "sear.labels" . }}
app.kubernetes.io/component: {{ .componentName }}
{{- end }}

{{/*
Component selector labels
*/}}
{{- define "sear.componentSelectorLabels" -}}
{{- include "sear.selectorLabels" . }}
app.kubernetes.io/component: {{ .componentName }}
{{- end }}

{{/*
Create the name of the service account to use
*/}}
{{- define "sear.serviceAccountName" -}}
  {{- if .Values.serviceAccount.create }}
    {{- default (include "sear.fullname" .) .Values.serviceAccount.name }}
  {{- else }}
    {{- default "default" .Values.serviceAccount.name }}
  {{- end }}
{{- end }}

{{/*
Creates the default name of host
*/}}
{{- define "sear.defaultHost" -}}
  {{- printf "%s-%s.prv.dev.s8ops.com" (include "sear.fullname" $) $.Release.Namespace }}
{{- end }}

{{/*
Creates the name of Sentry environment
*/}}
{{- define "sear.sentryEnvironment" -}}
  {{- default (printf "%s-%s.prv.dev.s8ops.com" (include "sear.fullname" $) $.Release.Namespace) .component.sentry.environment }}
{{- end }}

{{- define "sear.rabbitmqSecretName" -}}
{{- if .component.rabbit.external -}}
{{ include "sear.fullname" . }}-rabbitmq.external
{{- else -}}
{{ include "sear.fullname" . }}-rabbitmq-default-user
{{- end }}{{ end -}}

{{- define "sear.postgresqlSecretName" -}}
{{- if .component.db.external -}}
{{ printf "%s.%s" .component.db.name (include "sear.fullname" .) }}-postgres.external
{{- else -}}
{{ printf "%s.%s" .component.db.name (include "sear.fullname" .) }}-postgres.credentials.postgresql.acid.zalan.do
{{- end }}{{ end -}}
{{- define "sear.postgresqlService" }}{{ if .component.db.external }}{{ .component.db.host }}{{ else }}{{ include "sear.fullname" . }}-postgres.{{ .Release.Namespace }}.svc{{ end }}{{ end -}}
{{- define "sear.postgresqlServicePort" }}{{ if .component.db.external }}{{ .component.db.port }}{{- else }}5432{{ end }}{{ end -}}

{{- define "sear.mergedComponents" }}

  {{- $output := dict}}

  {{- range $key, $value := $.Values.components }}
    {{- $common := $.Values.common }}
    {{- $concatenated := dict }}
    {{- range $toConcatenate := list "args" "command" "profiles" }}
      {{- $_ := set $concatenated $toConcatenate (concat (get $common $toConcatenate) (default list (get $value $toConcatenate) ) ) }}
    {{- end }}
    {{- range $toMerge := list "properties" }}
      {{- $_ := set $concatenated $toMerge (mergeOverwrite (deepCopy (get $common $toMerge)) (default dict (get $value $toMerge) ) ) }}
    {{- end }}
    {{- $mergedValue := mergeOverwrite (deepCopy $common) $value $concatenated }}
    {{- $mergedValue = mergeOverwrite (deepCopy $mergedValue) (dict "db" (default $key $mergedValue.db) "webPath" (default $key $mergedValue.webPath) ) }}
    {{- if $mergedValue.enabled -}}
      {{- $_ := set $output $key $mergedValue }}
    {{- end }}
    {{- if not $mergedValue.db.name -}}
      {{- $_ := set $mergedValue.db "name" $key }}
    {{- end }}
  {{- end }}

  {{- range $key, $value := $.Values.agents }}
    {{- $common := mergeOverwrite (deepCopy $.Values.common) $.Values.agentsCommon }}
    {{- if hasKey $value "from" }}
      {{- $value = mergeOverwrite (deepCopy $value) (get $.Values.agents $value.from) }}
      {{- $_ := unset $value "from" }}
    {{- end }}
    {{- $concatenated := dict }}
    {{- range $toConcatenate := list "args" "command" "profiles" }}
      {{- $_ := set $concatenated $toConcatenate (concat (get $common $toConcatenate) (default list (get $value $toConcatenate) ) ) }}
    {{- end }}
    {{- range $toMerge := list "properties" }}
      {{- $_ := set $concatenated $toMerge (mergeOverwrite (deepCopy (get $common $toMerge)) (default dict (get $value $toMerge) ) ) }}
    {{- end }}
    {{- $mergedValue := mergeOverwrite (deepCopy $common) $value $concatenated }}
    {{- $mergedValue = mergeOverwrite (deepCopy $mergedValue) (dict "db" (default $key $mergedValue.db) "webPath" (default $key $mergedValue.webPath) ) }}
    {{- if $mergedValue.enabled -}}
      {{- $_ := set $output $key $mergedValue }}
    {{- end }}
    {{- if not $mergedValue.db.name -}}
      {{- $_ := set $mergedValue.db "name" $key }}
    {{- end }}
  {{- end }}
  {{- $output | mustToJson }}
{{- end }}
{{- define "checkRabbitMqInitContainer" }}
- name: check-rabbitmq-ready
  image: public.ecr.aws/docker/library/busybox
  command:
     - sh
     - -c
     - >-
       until wget -q http://$RABBITMQ_USERNAME:$RABBITMQ_PASSWORD@$RABBITMQ_HOST:15672/api/aliveness-test/%2F; do
         echo waiting for rabbitmq;
         sleep 2;
       done;
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
{{- end }}
{{- define "checkPostgresReadyInitContainer" }}
- name: check-db-ready
  image: "postgres:{{ .Values.database.postgresql.version }}"
  command:
    - sh
    - -c
    - >-
      until pg_isready -h {{ include "sear.postgresqlService" . }} -p {{ include "sear.postgresqlServicePort" . }}; do
        echo waiting for database;
        sleep 2;
      done;
{{- end }}
{{- define "initScripts" }}
- name: init-scripts
  image: public.ecr.aws/docker/library/busybox
  command: ['sh', '-c', "run-parts /var/run/initScripts"]
  volumeMounts:
    - name: init-scripts
      mountPath: "/var/run/initScripts"
      readOnly: true
    - name: generated
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
{{- define "sear.configServer" -}}
configserver:http://{{ include "sear.fullname" . }}-config-server.{{ .Release.Namespace }}.svc:{{ .component.containerPorts.http.port }}/rest/config-server
{{- end -}}
