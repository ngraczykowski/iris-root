{
  "enabled": true,
  "realm": "sens-webapp",
  "clients": [
    {
      "clientId": "frontend",
      "enabled": true,
      "baseUrl": "[=clients.frontend.baseUrl]",
      "clientAuthenticatorType": "client-secret",
      "redirectUris": [[#list clients.frontend.redirectUris?values as url]
        "[=url]"[#if !url?is_last],[/#if][/#list]
      ],
      "rootUrl": "[=clients.frontend.rootUrl]",
      "secret": "[=clients.frontend.secret]"
    }
  ]
}
