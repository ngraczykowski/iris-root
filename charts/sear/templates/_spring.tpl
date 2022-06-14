{{/*
Create the JDBC connection string
*/}}
{{- define "sear.spring.jdbcUrl" -}}
jdbc:postgresql://{{ include "sear.postgresqlService" . }}:{{ include "sear.postgresqlServicePort" . }}/{{ .component.db.name }}?{{ .component.db.options }}
{{- end -}}
