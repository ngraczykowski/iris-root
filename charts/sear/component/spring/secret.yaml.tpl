{{- if .component.db.external }}
apiVersion: v1
kind: Secret
metadata:
  name: {{ include "sear.postgresqlSecretName" . }}
  labels:
    {{- include "sear.componentLabels" . | nindent 4 }}
data:
  password: {{ .component.db.password | b64enc }}
  username: {{ .component.db.username | b64enc }}
type: Opaque
{{- end }}
