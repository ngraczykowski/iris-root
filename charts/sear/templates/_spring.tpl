{{/*
Create the JDBC connection string
*/}}
{{- define "sear.spring.jdbcUrl" -}}
jdbc:postgresql://{{ include "sear.postgresqlService" . }}:5432/{{ .component.dbName }}?sslmode=require&sslfactory=org.postgresql.ssl.NonValidatingFactory
{{- end -}}

{{- define "sear.spring.authServer" -}}
{{ .Values.keycloak.authServerUrl }}/auth/realms/{{ .Values.keycloak.realm }}
{{- end -}}
