{
  "enabled": true,
  "realm": "sens-webapp",
  "clients": [
    {
      "attributes": {
        "display.on.consent.screen": "false",
        "exclude.session.state.from.auth.response": "false",
        "saml.assertion.signature": "false",
        "saml.authnstatement": "false",
        "saml.client.signature": "false",
        "saml.encrypt": "false",
        "saml.force.post.binding": "false",
        "saml.multivalued.roles": "false",
        "saml.onetimeuse.condition": "false",
        "saml.server.signature": "false",
        "saml.server.signature.keyinfo.ext": "false",
        "saml_force_name_id_format": "false",
        "tls.client.certificate.bound.access.tokens": "false"
      },
      "authenticationFlowBindingOverrides": {},
      "baseUrl": "[=clients.frontend.baseUrl]",
      "bearerOnly": false,
      "clientAuthenticatorType": "client-secret",
      "clientId": "chrome-extension",
      "consentRequired": false,
      "defaultClientScopes": [
        "email",
        "profile",
        "role_list",
        "roles",
        "web-origins"
      ],
      "directAccessGrantsEnabled": true,
      "enabled": true,
      "frontchannelLogout": false,
      "fullScopeAllowed": true,
      "implicitFlowEnabled": false,
      "nodeReRegistrationTimeout": -1,
      "notBefore": 0,
      "optionalClientScopes": [
        "address",
        "microprofile-jwt",
        "offline_access",
        "phone"
      ],
      "protocol": "openid-connect",
      "publicClient": true,
      "redirectUris": ["*"],
      "rootUrl": "[=clients.frontend.rootUrl]",
      "serviceAccountsEnabled": false,
      "standardFlowEnabled": true,
      "surrogateAuthRequired": false,
      "webOrigins": [
       "+"
      ]
    }
  ]
}
