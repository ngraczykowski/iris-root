{{/*
Create the JDBC connection string
*/}}
{{- define "sear.spring.jdbcUrl" -}}
jdbc:postgresql://{{ include "sear.postgresqlService" . }}:5432/{{ .component.dbName }}?sslmode=require&sslfactory=org.postgresql.ssl.NonValidatingFactory
{{- end -}}
