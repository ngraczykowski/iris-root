{
  "accessCodeLifespan": 60,
  "accessCodeLifespanLogin": 1800,
  "accessCodeLifespanUserAction": 300,
  "accessTokenLifespan": 300,
  "accessTokenLifespanForImplicitFlow": 900,
  "actionTokenGeneratedByAdminLifespan": 43200,
  "actionTokenGeneratedByUserLifespan": 300,
  "adminEventsDetailsEnabled": false,
  "adminEventsEnabled": true,
  "attributes": {
    "_browser_header.contentSecurityPolicy": "frame-src 'self'; frame-ancestors 'self'; object-src 'none';",
    "_browser_header.contentSecurityPolicyReportOnly": "",
    "_browser_header.strictTransportSecurity": "max-age=31536000; includeSubDomains",
    "_browser_header.xContentTypeOptions": "nosniff",
    "_browser_header.xFrameOptions": "SAMEORIGIN",
    "_browser_header.xRobotsTag": "none",
    "_browser_header.xXSSProtection": "1; mode=block",
    "actionTokenGeneratedByAdminLifespan": "43200",
    "actionTokenGeneratedByUserLifespan": "300",
    "bruteForceProtected": "false",
    "displayName": "SENS",
    "displayNameHtml": "<h1>Silent Eight Name Screening</h1>",
    "failureFactor": "30",
    "maxDeltaTimeSeconds": "43200",
    "maxFailureWaitSeconds": "900",
    "minimumQuickLoginWaitSeconds": "60",
    "offlineSessionMaxLifespan": "5184000",
    "offlineSessionMaxLifespanEnabled": "false",
    "permanentLockout": "false",
    "quickLoginCheckMilliSeconds": "1000",
    "waitIncrementSeconds": "60",
    "webAuthnPolicyAttestationConveyancePreference": "not specified",
    "webAuthnPolicyAuthenticatorAttachment": "not specified",
    "webAuthnPolicyAvoidSameAuthenticatorRegister": "false",
    "webAuthnPolicyCreateTimeout": "0",
    "webAuthnPolicyRequireResidentKey": "not specified",
    "webAuthnPolicyRpEntityName": "keycloak",
    "webAuthnPolicyRpId": "",
    "webAuthnPolicySignatureAlgorithms": "ES256",
    "webAuthnPolicyUserVerificationRequirement": "not specified"
  },
  "authenticationFlows": [
    {
      "alias": "Account verification options",
      "authenticationExecutions": [
        {
          "authenticator": "idp-email-verification",
          "autheticatorFlow": false,
          "priority": 10,
          "requirement": "ALTERNATIVE",
          "userSetupAllowed": false
        },
        {
          "autheticatorFlow": true,
          "flowAlias": "Verify Existing Account by Re-authentication",
          "priority": 20,
          "requirement": "ALTERNATIVE",
          "userSetupAllowed": false
        }
      ],
      "builtIn": true,
      "description": "Method with which to verity the existing account",
      "providerId": "basic-flow",
      "topLevel": false
    },
    {
      "alias": "Authentication Options",
      "authenticationExecutions": [
        {
          "authenticator": "auth-spnego",
          "autheticatorFlow": false,
          "priority": 30,
          "requirement": "DISABLED",
          "userSetupAllowed": false
        },
        {
          "authenticator": "basic-auth",
          "autheticatorFlow": false,
          "priority": 10,
          "requirement": "REQUIRED",
          "userSetupAllowed": false
        },
        {
          "authenticator": "basic-auth-otp",
          "autheticatorFlow": false,
          "priority": 20,
          "requirement": "DISABLED",
          "userSetupAllowed": false
        }
      ],
      "builtIn": true,
      "description": "Authentication options.",
      "providerId": "basic-flow",
      "topLevel": false
    },
    {
      "alias": "Browser - Conditional OTP",
      "authenticationExecutions": [
        {
          "authenticator": "auth-otp-form",
          "autheticatorFlow": false,
          "priority": 20,
          "requirement": "REQUIRED",
          "userSetupAllowed": false
        },
        {
          "authenticator": "conditional-user-configured",
          "autheticatorFlow": false,
          "priority": 10,
          "requirement": "REQUIRED",
          "userSetupAllowed": false
        }
      ],
      "builtIn": true,
      "description": "Flow to determine if the OTP is required for the authentication",
      "providerId": "basic-flow",
      "topLevel": false
    },
    {
      "alias": "Direct Grant - Conditional OTP",
      "authenticationExecutions": [
        {
          "authenticator": "conditional-user-configured",
          "autheticatorFlow": false,
          "priority": 10,
          "requirement": "REQUIRED",
          "userSetupAllowed": false
        },
        {
          "authenticator": "direct-grant-validate-otp",
          "autheticatorFlow": false,
          "priority": 20,
          "requirement": "REQUIRED",
          "userSetupAllowed": false
        }
      ],
      "builtIn": true,
      "description": "Flow to determine if the OTP is required for the authentication",
      "providerId": "basic-flow",
      "topLevel": false
    },
    {
      "alias": "First broker login - Conditional OTP",
      "authenticationExecutions": [
        {
          "authenticator": "auth-otp-form",
          "autheticatorFlow": false,
          "priority": 20,
          "requirement": "REQUIRED",
          "userSetupAllowed": false
        },
        {
          "authenticator": "conditional-user-configured",
          "autheticatorFlow": false,
          "priority": 10,
          "requirement": "REQUIRED",
          "userSetupAllowed": false
        }
      ],
      "builtIn": true,
      "description": "Flow to determine if the OTP is required for the authentication",
      "providerId": "basic-flow",
      "topLevel": false
    },
    {
      "alias": "Handle Existing Account",
      "authenticationExecutions": [
        {
          "authenticator": "idp-confirm-link",
          "autheticatorFlow": false,
          "priority": 10,
          "requirement": "REQUIRED",
          "userSetupAllowed": false
        },
        {
          "autheticatorFlow": true,
          "flowAlias": "Account verification options",
          "priority": 20,
          "requirement": "REQUIRED",
          "userSetupAllowed": false
        }
      ],
      "builtIn": true,
      "description": "Handle what to do if there is existing account with same email/username like authenticated identity provider",
      "providerId": "basic-flow",
      "topLevel": false
    },
    {
      "alias": "Reset - Conditional OTP",
      "authenticationExecutions": [
        {
          "authenticator": "conditional-user-configured",
          "autheticatorFlow": false,
          "priority": 10,
          "requirement": "REQUIRED",
          "userSetupAllowed": false
        },
        {
          "authenticator": "reset-otp",
          "autheticatorFlow": false,
          "priority": 20,
          "requirement": "REQUIRED",
          "userSetupAllowed": false
        }
      ],
      "builtIn": true,
      "description": "Flow to determine if the OTP should be reset or not. Set to REQUIRED to force.",
      "providerId": "basic-flow",
      "topLevel": false
    },
    {
      "alias": "SAML login flow",
      "authenticationExecutions": [
        {
          "authenticator": "auth-script-based",
          "authenticatorConfig": "automatically-login-existing-user",
          "autheticatorFlow": false,
          "priority": 2,
          "requirement": "REQUIRED",
          "userSetupAllowed": false
        },
        {
          "authenticator": "idp-auto-link",
          "autheticatorFlow": false,
          "priority": 3,
          "requirement": "REQUIRED",
          "userSetupAllowed": false
        }
      ],
      "builtIn": false,
      "description": "",
      "providerId": "basic-flow",
      "topLevel": true
    },
    {
      "alias": "User creation or linking",
      "authenticationExecutions": [
        {
          "authenticator": "idp-create-user-if-unique",
          "authenticatorConfig": "create unique user config",
          "autheticatorFlow": false,
          "priority": 10,
          "requirement": "ALTERNATIVE",
          "userSetupAllowed": false
        },
        {
          "autheticatorFlow": true,
          "flowAlias": "Handle Existing Account",
          "priority": 20,
          "requirement": "ALTERNATIVE",
          "userSetupAllowed": false
        }
      ],
      "builtIn": true,
      "description": "Flow for the existing/non-existing user alternatives",
      "providerId": "basic-flow",
      "topLevel": false
    },
    {
      "alias": "Verify Existing Account by Re-authentication",
      "authenticationExecutions": [
        {
          "authenticator": "idp-username-password-form",
          "autheticatorFlow": false,
          "priority": 10,
          "requirement": "REQUIRED",
          "userSetupAllowed": false
        },
        {
          "autheticatorFlow": true,
          "flowAlias": "First broker login - Conditional OTP",
          "priority": 20,
          "requirement": "CONDITIONAL",
          "userSetupAllowed": false
        }
      ],
      "builtIn": true,
      "description": "Reauthentication of existing account",
      "providerId": "basic-flow",
      "topLevel": false
    },
    {
      "alias": "browser",
      "authenticationExecutions": [
        {
          "authenticator": "auth-cookie",
          "autheticatorFlow": false,
          "priority": 10,
          "requirement": "ALTERNATIVE",
          "userSetupAllowed": false
        },
        {
          "authenticator": "auth-spnego",
          "autheticatorFlow": false,
          "priority": 20,
          "requirement": "DISABLED",
          "userSetupAllowed": false
        },
        {
          "authenticator": "identity-provider-redirector",
          "autheticatorFlow": false,
          "priority": 25,
          "requirement": "ALTERNATIVE",
          "userSetupAllowed": false
        },
        {
          "autheticatorFlow": true,
          "flowAlias": "forms",
          "priority": 30,
          "requirement": "ALTERNATIVE",
          "userSetupAllowed": false
        }
      ],
      "builtIn": true,
      "description": "browser based authentication",
      "providerId": "basic-flow",
      "topLevel": true
    },
    {
      "alias": "clients",
      "authenticationExecutions": [
        {
          "authenticator": "client-jwt",
          "autheticatorFlow": false,
          "priority": 20,
          "requirement": "ALTERNATIVE",
          "userSetupAllowed": false
        },
        {
          "authenticator": "client-secret",
          "autheticatorFlow": false,
          "priority": 10,
          "requirement": "ALTERNATIVE",
          "userSetupAllowed": false
        },
        {
          "authenticator": "client-secret-jwt",
          "autheticatorFlow": false,
          "priority": 30,
          "requirement": "ALTERNATIVE",
          "userSetupAllowed": false
        },
        {
          "authenticator": "client-x509",
          "autheticatorFlow": false,
          "priority": 40,
          "requirement": "ALTERNATIVE",
          "userSetupAllowed": false
        }
      ],
      "builtIn": true,
      "description": "Base authentication for clients",
      "providerId": "client-flow",
      "topLevel": true
    },
    {
      "alias": "direct grant",
      "authenticationExecutions": [
        {
          "authenticator": "direct-grant-validate-password",
          "autheticatorFlow": false,
          "priority": 20,
          "requirement": "REQUIRED",
          "userSetupAllowed": false
        },
        {
          "authenticator": "direct-grant-validate-username",
          "autheticatorFlow": false,
          "priority": 10,
          "requirement": "REQUIRED",
          "userSetupAllowed": false
        },
        {
          "autheticatorFlow": true,
          "flowAlias": "Direct Grant - Conditional OTP",
          "priority": 30,
          "requirement": "CONDITIONAL",
          "userSetupAllowed": false
        }
      ],
      "builtIn": true,
      "description": "OpenID Connect Resource Owner Grant",
      "providerId": "basic-flow",
      "topLevel": true
    },
    {
      "alias": "docker auth",
      "authenticationExecutions": [
        {
          "authenticator": "docker-http-basic-authenticator",
          "autheticatorFlow": false,
          "priority": 10,
          "requirement": "REQUIRED",
          "userSetupAllowed": false
        }
      ],
      "builtIn": true,
      "description": "Used by Docker clients to authenticate against the IDP",
      "providerId": "basic-flow",
      "topLevel": true
    },
    {
      "alias": "first broker login",
      "authenticationExecutions": [
        {
          "authenticator": "idp-review-profile",
          "authenticatorConfig": "review profile config",
          "autheticatorFlow": false,
          "priority": 10,
          "requirement": "REQUIRED",
          "userSetupAllowed": false
        },
        {
          "autheticatorFlow": true,
          "flowAlias": "User creation or linking",
          "priority": 20,
          "requirement": "REQUIRED",
          "userSetupAllowed": false
        }
      ],
      "builtIn": true,
      "description": "Actions taken after first broker login with identity provider account, which is not yet linked to any Keycloak account",
      "providerId": "basic-flow",
      "topLevel": true
    },
    {
      "alias": "forms",
      "authenticationExecutions": [
        {
          "authenticator": "auth-username-password-form",
          "autheticatorFlow": false,
          "priority": 10,
          "requirement": "REQUIRED",
          "userSetupAllowed": false
        },
        {
          "autheticatorFlow": true,
          "flowAlias": "Browser - Conditional OTP",
          "priority": 20,
          "requirement": "CONDITIONAL",
          "userSetupAllowed": false
        }
      ],
      "builtIn": true,
      "description": "Username, password, otp and other auth forms.",
      "providerId": "basic-flow",
      "topLevel": false
    },
    {
      "alias": "http challenge",
      "authenticationExecutions": [
        {
          "authenticator": "no-cookie-redirect",
          "autheticatorFlow": false,
          "priority": 10,
          "requirement": "REQUIRED",
          "userSetupAllowed": false
        },
        {
          "autheticatorFlow": true,
          "flowAlias": "Authentication Options",
          "priority": 20,
          "requirement": "REQUIRED",
          "userSetupAllowed": false
        }
      ],
      "builtIn": true,
      "description": "An authentication flow based on challenge-response HTTP Authentication Schemes",
      "providerId": "basic-flow",
      "topLevel": true
    },
    {
      "alias": "registration",
      "authenticationExecutions": [
        {
          "authenticator": "registration-page-form",
          "autheticatorFlow": true,
          "flowAlias": "registration form",
          "priority": 10,
          "requirement": "REQUIRED",
          "userSetupAllowed": false
        }
      ],
      "builtIn": true,
      "description": "registration flow",
      "providerId": "basic-flow",
      "topLevel": true
    },
    {
      "alias": "registration form",
      "authenticationExecutions": [
        {
          "authenticator": "registration-password-action",
          "autheticatorFlow": false,
          "priority": 50,
          "requirement": "REQUIRED",
          "userSetupAllowed": false
        },
        {
          "authenticator": "registration-profile-action",
          "autheticatorFlow": false,
          "priority": 40,
          "requirement": "REQUIRED",
          "userSetupAllowed": false
        },
        {
          "authenticator": "registration-recaptcha-action",
          "autheticatorFlow": false,
          "priority": 60,
          "requirement": "DISABLED",
          "userSetupAllowed": false
        },
        {
          "authenticator": "registration-user-creation",
          "autheticatorFlow": false,
          "priority": 20,
          "requirement": "REQUIRED",
          "userSetupAllowed": false
        }
      ],
      "builtIn": true,
      "description": "registration form",
      "providerId": "form-flow",
      "topLevel": false
    },
    {
      "alias": "reset credentials",
      "authenticationExecutions": [
        {
          "authenticator": "reset-credential-email",
          "autheticatorFlow": false,
          "priority": 20,
          "requirement": "REQUIRED",
          "userSetupAllowed": false
        },
        {
          "authenticator": "reset-credentials-choose-user",
          "autheticatorFlow": false,
          "priority": 10,
          "requirement": "REQUIRED",
          "userSetupAllowed": false
        },
        {
          "authenticator": "reset-password",
          "autheticatorFlow": false,
          "priority": 30,
          "requirement": "REQUIRED",
          "userSetupAllowed": false
        },
        {
          "autheticatorFlow": true,
          "flowAlias": "Reset - Conditional OTP",
          "priority": 40,
          "requirement": "CONDITIONAL",
          "userSetupAllowed": false
        }
      ],
      "builtIn": true,
      "description": "Reset credentials for a user if they forgot their password or something",
      "providerId": "basic-flow",
      "topLevel": true
    },
    {
      "alias": "saml ecp",
      "authenticationExecutions": [
        {
          "authenticator": "http-basic-authenticator",
          "autheticatorFlow": false,
          "priority": 10,
          "requirement": "REQUIRED",
          "userSetupAllowed": false
        }
      ],
      "builtIn": true,
      "description": "SAML ECP Profile Authentication Flow",
      "providerId": "basic-flow",
      "topLevel": true
    }
  ],
  "authenticatorConfig": [
    {
      "alias": "automatically-login-existing-user",
      "config": {
        "scriptCode": "/*\n * Template for JavaScript based authenticator's.\n * See org.keycloak.authentication.authenticators.browser.ScriptBasedAuthenticatorFactory\n */\n\n// import enum for error lookup\nAuthenticationFlowError = Java.type(\"org.keycloak.authentication.AuthenticationFlowError\");\nSerializedBrokeredIdentityContext = Java.type(\"org.keycloak.authentication.authenticators.broker.util.SerializedBrokeredIdentityContext\");\nAbstractIdpAuthenticator = Java.type(\"org.keycloak.authentication.authenticators.broker.AbstractIdpAuthenticator\");\nUserModel = Java.type('org.keycloak.models.UserModel');\nExistingUserInfo = Java.type('org.keycloak.authentication.authenticators.broker.util.ExistingUserInfo');\n/**\n * An example authenticate function.\n *\n * The following variables are available for convenience:\n * user - current user {@see org.keycloak.models.UserModel}\n * realm - current realm {@see org.keycloak.models.RealmModel}\n * session - current KeycloakSession {@see org.keycloak.models.KeycloakSession}\n * httpRequest - current HttpRequest {@see org.jboss.resteasy.spi.HttpRequest}\n * script - current script {@see org.keycloak.models.ScriptModel}\n * authenticationSession - current authentication session {@see org.keycloak.sessions.AuthenticationSessionModel}\n * LOG - current logger {@see org.jboss.logging.Logger}\n *\n * You one can extract current http request headers via:\n * httpRequest.getHttpHeaders().getHeaderString(\"Forwarded\")\n *\n * @param context {@see org.keycloak.authentication.AuthenticationFlowContext}\n */\nfunction authenticate(context) {\n    var serializedCtx = SerializedBrokeredIdentityContext.readFromAuthenticationSession(authenticationSession, AbstractIdpAuthenticator.BROKERED_CONTEXT_NOTE);\n    var brokerIdentityCtx = serializedCtx.deserialize(session, authenticationSession);\n    \n    var idpUsername = brokerIdentityCtx.username;\n    \n    LOG.info(\"Broker context: \" + brokerIdentityCtx);\n    LOG.info(\"Trying to login: \" + idpUsername);\n\n    var existingUser = context.getSession().users().getUserByUsername(idpUsername, context.getRealm());\n    \n    \n    if (existingUser === null) {\n        LOG.info(\"Could not find user \" + idpUsername);\n        context.failure(AuthenticationFlowError.INVALID_USER);\n        return;\n    }\n\n    LOG.info(\"User \" + idpUsername + \" found.\")\n    \n    var existingUserInfo = new ExistingUserInfo(existingUser.id, UserModel.USERNAME, existingUser.username);\n    context.getAuthenticationSession().setAuthNote(AbstractIdpAuthenticator.EXISTING_USER_INFO,existingUserInfo.serialize());\n\n    context.success();\n    return;\n}"
      }
    },
    {
      "alias": "create unique user config",
      "config": {
        "require.password.update.after.registration": "false"
      }
    },
    {
      "alias": "review profile config",
      "config": {
        "update.profile.on.first.login": "missing"
      }
    }
  ],
  "browserFlow": "browser",
  "browserSecurityHeaders": {
    "contentSecurityPolicy": "frame-src 'self'; frame-ancestors 'self'; object-src 'none';",
    "contentSecurityPolicyReportOnly": "",
    "strictTransportSecurity": "max-age=31536000; includeSubDomains",
    "xContentTypeOptions": "nosniff",
    "xFrameOptions": "SAMEORIGIN",
    "xRobotsTag": "none",
    "xXSSProtection": "1; mode=block"
  },
  "bruteForceProtected": false,
  "clientAuthenticationFlow": "clients",
  "clientScopes": [
    {
      "attributes": {
        "consent.screen.text": "${offlineAccessScopeConsentText}",
        "display.on.consent.screen": "true"
      },
      "description": "OpenID Connect built-in scope: offline_access",
      "name": "offline_access",
      "protocol": "openid-connect"
    },
    {
      "attributes": {
        "consent.screen.text": "${samlRoleListScopeConsentText}",
        "display.on.consent.screen": "true"
      },
      "description": "SAML role list",
      "name": "role_list",
      "protocol": "saml",
      "protocolMappers": [
        {
          "config": {
            "attribute.name": "Role",
            "attribute.nameformat": "Basic",
            "single": "false"
          },
          "consentRequired": false,
          "name": "role list",
          "protocol": "saml",
          "protocolMapper": "saml-role-list-mapper"
        }
      ]
    },
    {
      "attributes": {
        "consent.screen.text": "",
        "display.on.consent.screen": "false",
        "include.in.token.scope": "false"
      },
      "description": "OpenID Connect scope for add allowed web origins to the access token",
      "name": "web-origins",
      "protocol": "openid-connect",
      "protocolMappers": [
        {
          "config": {},
          "consentRequired": false,
          "name": "allowed web origins",
          "protocol": "openid-connect",
          "protocolMapper": "oidc-allowed-origins-mapper"
        }
      ]
    },
    {
      "attributes": {
        "consent.screen.text": "${addressScopeConsentText}",
        "display.on.consent.screen": "true",
        "include.in.token.scope": "true"
      },
      "description": "OpenID Connect built-in scope: address",
      "name": "address",
      "protocol": "openid-connect",
      "protocolMappers": [
        {
          "config": {
            "access.token.claim": "true",
            "id.token.claim": "true",
            "user.attribute.country": "country",
            "user.attribute.formatted": "formatted",
            "user.attribute.locality": "locality",
            "user.attribute.postal_code": "postal_code",
            "user.attribute.region": "region",
            "user.attribute.street": "street",
            "userinfo.token.claim": "true"
          },
          "consentRequired": false,
          "name": "address",
          "protocol": "openid-connect",
          "protocolMapper": "oidc-address-mapper"
        }
      ]
    },
    {
      "attributes": {
        "consent.screen.text": "${emailScopeConsentText}",
        "display.on.consent.screen": "true",
        "include.in.token.scope": "true"
      },
      "description": "OpenID Connect built-in scope: email",
      "name": "email",
      "protocol": "openid-connect",
      "protocolMappers": [
        {
          "config": {
            "access.token.claim": "true",
            "claim.name": "email",
            "id.token.claim": "true",
            "jsonType.label": "String",
            "user.attribute": "email",
            "userinfo.token.claim": "true"
          },
          "consentRequired": false,
          "name": "email",
          "protocol": "openid-connect",
          "protocolMapper": "oidc-usermodel-property-mapper"
        },
        {
          "config": {
            "access.token.claim": "true",
            "claim.name": "email_verified",
            "id.token.claim": "true",
            "jsonType.label": "boolean",
            "user.attribute": "emailVerified",
            "userinfo.token.claim": "true"
          },
          "consentRequired": false,
          "name": "email verified",
          "protocol": "openid-connect",
          "protocolMapper": "oidc-usermodel-property-mapper"
        }
      ]
    },
    {
      "attributes": {
        "consent.screen.text": "${phoneScopeConsentText}",
        "display.on.consent.screen": "true",
        "include.in.token.scope": "true"
      },
      "description": "OpenID Connect built-in scope: phone",
      "name": "phone",
      "protocol": "openid-connect",
      "protocolMappers": [
        {
          "config": {
            "access.token.claim": "true",
            "claim.name": "phone_number",
            "id.token.claim": "true",
            "jsonType.label": "String",
            "user.attribute": "phoneNumber",
            "userinfo.token.claim": "true"
          },
          "consentRequired": false,
          "name": "phone number",
          "protocol": "openid-connect",
          "protocolMapper": "oidc-usermodel-attribute-mapper"
        },
        {
          "config": {
            "access.token.claim": "true",
            "claim.name": "phone_number_verified",
            "id.token.claim": "true",
            "jsonType.label": "boolean",
            "user.attribute": "phoneNumberVerified",
            "userinfo.token.claim": "true"
          },
          "consentRequired": false,
          "name": "phone number verified",
          "protocol": "openid-connect",
          "protocolMapper": "oidc-usermodel-attribute-mapper"
        }
      ]
    },
    {
      "attributes": {
        "consent.screen.text": "${profileScopeConsentText}",
        "display.on.consent.screen": "true",
        "include.in.token.scope": "true"
      },
      "description": "OpenID Connect built-in scope: profile",
      "name": "profile",
      "protocol": "openid-connect",
      "protocolMappers": [
        {
          "config": {
            "access.token.claim": "true",
            "claim.name": "birthdate",
            "id.token.claim": "true",
            "jsonType.label": "String",
            "user.attribute": "birthdate",
            "userinfo.token.claim": "true"
          },
          "consentRequired": false,
          "name": "birthdate",
          "protocol": "openid-connect",
          "protocolMapper": "oidc-usermodel-attribute-mapper"
        },
        {
          "config": {
            "access.token.claim": "true",
            "claim.name": "family_name",
            "id.token.claim": "true",
            "jsonType.label": "String",
            "user.attribute": "lastName",
            "userinfo.token.claim": "true"
          },
          "consentRequired": false,
          "name": "family name",
          "protocol": "openid-connect",
          "protocolMapper": "oidc-usermodel-property-mapper"
        },
        {
          "config": {
            "access.token.claim": "true",
            "claim.name": "gender",
            "id.token.claim": "true",
            "jsonType.label": "String",
            "user.attribute": "gender",
            "userinfo.token.claim": "true"
          },
          "consentRequired": false,
          "name": "gender",
          "protocol": "openid-connect",
          "protocolMapper": "oidc-usermodel-attribute-mapper"
        },
        {
          "config": {
            "access.token.claim": "true",
            "claim.name": "given_name",
            "id.token.claim": "true",
            "jsonType.label": "String",
            "user.attribute": "firstName",
            "userinfo.token.claim": "true"
          },
          "consentRequired": false,
          "name": "given name",
          "protocol": "openid-connect",
          "protocolMapper": "oidc-usermodel-property-mapper"
        },
        {
          "config": {
            "access.token.claim": "true",
            "claim.name": "locale",
            "id.token.claim": "true",
            "jsonType.label": "String",
            "user.attribute": "locale",
            "userinfo.token.claim": "true"
          },
          "consentRequired": false,
          "name": "locale",
          "protocol": "openid-connect",
          "protocolMapper": "oidc-usermodel-attribute-mapper"
        },
        {
          "config": {
            "access.token.claim": "true",
            "claim.name": "middle_name",
            "id.token.claim": "true",
            "jsonType.label": "String",
            "user.attribute": "middleName",
            "userinfo.token.claim": "true"
          },
          "consentRequired": false,
          "name": "middle name",
          "protocol": "openid-connect",
          "protocolMapper": "oidc-usermodel-attribute-mapper"
        },
        {
          "config": {
            "access.token.claim": "true",
            "claim.name": "nickname",
            "id.token.claim": "true",
            "jsonType.label": "String",
            "user.attribute": "nickname",
            "userinfo.token.claim": "true"
          },
          "consentRequired": false,
          "name": "nickname",
          "protocol": "openid-connect",
          "protocolMapper": "oidc-usermodel-attribute-mapper"
        },
        {
          "config": {
            "access.token.claim": "true",
            "claim.name": "picture",
            "id.token.claim": "true",
            "jsonType.label": "String",
            "user.attribute": "picture",
            "userinfo.token.claim": "true"
          },
          "consentRequired": false,
          "name": "picture",
          "protocol": "openid-connect",
          "protocolMapper": "oidc-usermodel-attribute-mapper"
        },
        {
          "config": {
            "access.token.claim": "true",
            "claim.name": "preferred_username",
            "id.token.claim": "true",
            "jsonType.label": "String",
            "user.attribute": "username",
            "userinfo.token.claim": "true"
          },
          "consentRequired": false,
          "name": "username",
          "protocol": "openid-connect",
          "protocolMapper": "oidc-usermodel-property-mapper"
        },
        {
          "config": {
            "access.token.claim": "true",
            "claim.name": "profile",
            "id.token.claim": "true",
            "jsonType.label": "String",
            "user.attribute": "profile",
            "userinfo.token.claim": "true"
          },
          "consentRequired": false,
          "name": "profile",
          "protocol": "openid-connect",
          "protocolMapper": "oidc-usermodel-attribute-mapper"
        },
        {
          "config": {
            "access.token.claim": "true",
            "claim.name": "updated_at",
            "id.token.claim": "true",
            "jsonType.label": "String",
            "user.attribute": "updatedAt",
            "userinfo.token.claim": "true"
          },
          "consentRequired": false,
          "name": "updated at",
          "protocol": "openid-connect",
          "protocolMapper": "oidc-usermodel-attribute-mapper"
        },
        {
          "config": {
            "access.token.claim": "true",
            "claim.name": "website",
            "id.token.claim": "true",
            "jsonType.label": "String",
            "user.attribute": "website",
            "userinfo.token.claim": "true"
          },
          "consentRequired": false,
          "name": "website",
          "protocol": "openid-connect",
          "protocolMapper": "oidc-usermodel-attribute-mapper"
        },
        {
          "config": {
            "access.token.claim": "true",
            "claim.name": "zoneinfo",
            "id.token.claim": "true",
            "jsonType.label": "String",
            "user.attribute": "zoneinfo",
            "userinfo.token.claim": "true"
          },
          "consentRequired": false,
          "name": "zoneinfo",
          "protocol": "openid-connect",
          "protocolMapper": "oidc-usermodel-attribute-mapper"
        },
        {
          "config": {
            "access.token.claim": "true",
            "id.token.claim": "true",
            "userinfo.token.claim": "true"
          },
          "consentRequired": false,
          "name": "full name",
          "protocol": "openid-connect",
          "protocolMapper": "oidc-full-name-mapper"
        }
      ]
    },
    {
      "attributes": {
        "consent.screen.text": "${rolesScopeConsentText}",
        "display.on.consent.screen": "true",
        "include.in.token.scope": "false"
      },
      "description": "OpenID Connect scope for add user roles to the access token",
      "name": "roles",
      "protocol": "openid-connect",
      "protocolMappers": [
        {
          "config": {},
          "consentRequired": false,
          "name": "audience resolve",
          "protocol": "openid-connect",
          "protocolMapper": "oidc-audience-resolve-mapper"
        },
        {
          "config": {
            "access.token.claim": "true",
            "claim.name": "realm_access.roles",
            "jsonType.label": "String",
            "multivalued": "true",
            "user.attribute": "foo"
          },
          "consentRequired": false,
          "name": "realm roles",
          "protocol": "openid-connect",
          "protocolMapper": "oidc-usermodel-realm-role-mapper"
        },
        {
          "config": {
            "access.token.claim": "true",
            "claim.name": "resource_access.${client_id}.roles",
            "jsonType.label": "String",
            "multivalued": "true",
            "user.attribute": "foo"
          },
          "consentRequired": false,
          "name": "client roles",
          "protocol": "openid-connect",
          "protocolMapper": "oidc-usermodel-client-role-mapper"
        }
      ]
    },
    {
      "attributes": {
        "display.on.consent.screen": "false",
        "include.in.token.scope": "true"
      },
      "description": "Microprofile - JWT built-in scope",
      "name": "microprofile-jwt",
      "protocol": "openid-connect",
      "protocolMappers": [
        {
          "config": {
            "access.token.claim": "true",
            "claim.name": "groups",
            "id.token.claim": "true",
            "jsonType.label": "String",
            "multivalued": "true",
            "user.attribute": "foo",
            "userinfo.token.claim": "true"
          },
          "consentRequired": false,
          "name": "groups",
          "protocol": "openid-connect",
          "protocolMapper": "oidc-usermodel-realm-role-mapper"
        },
        {
          "config": {
            "access.token.claim": "true",
            "claim.name": "upn",
            "id.token.claim": "true",
            "jsonType.label": "String",
            "user.attribute": "username",
            "userinfo.token.claim": "true"
          },
          "consentRequired": false,
          "name": "upn",
          "protocol": "openid-connect",
          "protocolMapper": "oidc-usermodel-property-mapper"
        }
      ]
    }
  ],
  "clients": [
    {
      "adminUrl": "",
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
      "clientId": "frontend",
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
      "redirectUris": [[#list clients.frontend.redirectUris?values as url]
        "[=url]"[#if !url?is_last],[/#if][/#list]
      ],
      "rootUrl": "[=clients.frontend.rootUrl]",
      "secret": "[=clients.frontend.secret]",
      "serviceAccountsEnabled": false,
      "standardFlowEnabled": true,
      "surrogateAuthRequired": false,
      "webOrigins": [
        "+"
      ]
    },
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
      "authorizationServicesEnabled": true,
      "authorizationSettings": {
        "allowRemoteResourceManagement": true,
        "decisionStrategy": "UNANIMOUS",
        "policies": [
          {
            "config": {
              "applyPolicies": "[\"Default Policy\"]",
              "defaultResourceType": "urn:backend:resources:default"
            },
            "decisionStrategy": "UNANIMOUS",
            "description": "A permission that applies to the default resource type",
            "logic": "POSITIVE",
            "name": "Default Permission",
            "type": "resource"
          },
          {
            "config": {
              "code": "// by default, grants any permission associated with this policy\n$evaluation.grant();\n"
            },
            "decisionStrategy": "AFFIRMATIVE",
            "description": "A policy that grants access only for users within this realm",
            "logic": "POSITIVE",
            "name": "Default Policy",
            "type": "js"
          }
        ],
        "policyEnforcementMode": "ENFORCING",
        "resources": [
          {
            "_id": "4a5dfa0c-a2ba-44e7-9b5a-378d9d0eb842",
            "attributes": {},
            "name": "Default Resource",
            "ownerManagedAccess": false,
            "type": "urn:backend:resources:default",
            "uris": [
              "/*"
            ]
          }
        ],
        "scopes": []
      },
      "bearerOnly": false,
      "clientAuthenticatorType": "client-secret",
      "clientId": "backend",
      "consentRequired": false,
      "defaultClientScopes": [
        "email",
        "profile",
        "role_list",
        "roles",
        "web-origins"
      ],
      "directAccessGrantsEnabled": false,
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
      "protocolMappers": [
        {
          "config": {
            "access.token.claim": "true",
            "claim.name": "clientAddress",
            "id.token.claim": "true",
            "jsonType.label": "String",
            "user.session.note": "clientAddress",
            "userinfo.token.claim": "true"
          },
          "consentRequired": false,
          "name": "Client IP Address",
          "protocol": "openid-connect",
          "protocolMapper": "oidc-usersessionmodel-note-mapper"
        },
        {
          "config": {
            "access.token.claim": "true",
            "claim.name": "clientHost",
            "id.token.claim": "true",
            "jsonType.label": "String",
            "user.session.note": "clientHost",
            "userinfo.token.claim": "true"
          },
          "consentRequired": false,
          "name": "Client Host",
          "protocol": "openid-connect",
          "protocolMapper": "oidc-usersessionmodel-note-mapper"
        },
        {
          "config": {
            "access.token.claim": "true",
            "claim.name": "clientId",
            "id.token.claim": "true",
            "jsonType.label": "String",
            "user.session.note": "clientId",
            "userinfo.token.claim": "true"
          },
          "consentRequired": false,
          "name": "Client ID",
          "protocol": "openid-connect",
          "protocolMapper": "oidc-usersessionmodel-note-mapper"
        }
      ],
      "publicClient": false,
      "secret": "[=clients.backend.secret]",
      "serviceAccountsEnabled": true,
      "standardFlowEnabled": false,
      "surrogateAuthRequired": false,
      "webOrigins": [
        ""
      ]
    },
    {
      "attributes": {},
      "authenticationFlowBindingOverrides": {},
      "baseUrl": "/realms/sens-webapp/account/",
      "bearerOnly": false,
      "clientAuthenticatorType": "client-secret",
      "clientId": "account",
      "consentRequired": false,
      "defaultClientScopes": [
        "email",
        "profile",
        "role_list",
        "roles",
        "web-origins"
      ],
      "defaultRoles": [
        "manage-account",
        "view-profile"
      ],
      "directAccessGrantsEnabled": false,
      "enabled": true,
      "frontchannelLogout": false,
      "fullScopeAllowed": false,
      "implicitFlowEnabled": false,
      "name": "${client_account}",
      "nodeReRegistrationTimeout": 0,
      "notBefore": 0,
      "optionalClientScopes": [
        "address",
        "microprofile-jwt",
        "offline_access",
        "phone"
      ],
      "protocol": "openid-connect",
      "publicClient": false,
      "redirectUris": [
        "/realms/sens-webapp/account/*"
      ],
      "rootUrl": "${authBaseUrl}",
      "serviceAccountsEnabled": false,
      "standardFlowEnabled": true,
      "surrogateAuthRequired": false,
      "webOrigins": []
    },
    {
      "attributes": {},
      "authenticationFlowBindingOverrides": {},
      "baseUrl": "/admin/sens-webapp/console/",
      "bearerOnly": false,
      "clientAuthenticatorType": "client-secret",
      "clientId": "security-admin-console",
      "consentRequired": false,
      "defaultClientScopes": [
        "email",
        "profile",
        "role_list",
        "roles",
        "web-origins"
      ],
      "directAccessGrantsEnabled": false,
      "enabled": true,
      "frontchannelLogout": false,
      "fullScopeAllowed": false,
      "implicitFlowEnabled": false,
      "name": "${client_security-admin-console}",
      "nodeReRegistrationTimeout": 0,
      "notBefore": 0,
      "optionalClientScopes": [
        "address",
        "microprofile-jwt",
        "offline_access",
        "phone"
      ],
      "protocol": "openid-connect",
      "protocolMappers": [
        {
          "config": {
            "access.token.claim": "true",
            "claim.name": "locale",
            "id.token.claim": "true",
            "jsonType.label": "String",
            "user.attribute": "locale",
            "userinfo.token.claim": "true"
          },
          "consentRequired": false,
          "name": "locale",
          "protocol": "openid-connect",
          "protocolMapper": "oidc-usermodel-attribute-mapper"
        }
      ],
      "publicClient": true,
      "redirectUris": [
        "/admin/sens-webapp/console/*"
      ],
      "rootUrl": "${authAdminUrl}",
      "serviceAccountsEnabled": false,
      "standardFlowEnabled": true,
      "surrogateAuthRequired": false,
      "webOrigins": [
        "+"
      ]
    },
    {
      "attributes": {},
      "authenticationFlowBindingOverrides": {},
      "bearerOnly": false,
      "clientAuthenticatorType": "client-secret",
      "clientId": "admin-cli",
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
      "fullScopeAllowed": false,
      "implicitFlowEnabled": false,
      "name": "${client_admin-cli}",
      "nodeReRegistrationTimeout": 0,
      "notBefore": 0,
      "optionalClientScopes": [
        "address",
        "microprofile-jwt",
        "offline_access",
        "phone"
      ],
      "protocol": "openid-connect",
      "publicClient": true,
      "redirectUris": [],
      "secret": "[=clients.adminCli.secret]",
      "serviceAccountsEnabled": false,
      "standardFlowEnabled": false,
      "surrogateAuthRequired": false,
      "webOrigins": []
    },
    {
      "attributes": {},
      "authenticationFlowBindingOverrides": {},
      "bearerOnly": false,
      "clientAuthenticatorType": "client-secret",
      "clientId": "broker",
      "consentRequired": false,
      "defaultClientScopes": [
        "email",
        "profile",
        "role_list",
        "roles",
        "web-origins"
      ],
      "directAccessGrantsEnabled": false,
      "enabled": true,
      "frontchannelLogout": false,
      "fullScopeAllowed": false,
      "implicitFlowEnabled": false,
      "name": "${client_broker}",
      "nodeReRegistrationTimeout": 0,
      "notBefore": 0,
      "optionalClientScopes": [
        "address",
        "microprofile-jwt",
        "offline_access",
        "phone"
      ],
      "protocol": "openid-connect",
      "publicClient": false,
      "redirectUris": [],
      "serviceAccountsEnabled": false,
      "standardFlowEnabled": true,
      "surrogateAuthRequired": false,
      "webOrigins": []
    },
    {
      "attributes": {},
      "authenticationFlowBindingOverrides": {},
      "bearerOnly": true,
      "clientAuthenticatorType": "client-secret",
      "clientId": "realm-management",
      "consentRequired": false,
      "defaultClientScopes": [
        "email",
        "profile",
        "role_list",
        "roles",
        "web-origins"
      ],
      "directAccessGrantsEnabled": false,
      "enabled": true,
      "frontchannelLogout": false,
      "fullScopeAllowed": false,
      "implicitFlowEnabled": false,
      "name": "${client_realm-management}",
      "nodeReRegistrationTimeout": 0,
      "notBefore": 0,
      "optionalClientScopes": [
        "address",
        "microprofile-jwt",
        "offline_access",
        "phone"
      ],
      "protocol": "openid-connect",
      "publicClient": false,
      "redirectUris": [],
      "serviceAccountsEnabled": false,
      "standardFlowEnabled": true,
      "surrogateAuthRequired": false,
      "webOrigins": []
    },
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
      "bearerOnly": false,
      "clientAuthenticatorType": "client-secret",
      "clientId": "report-cli",
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
      "fullScopeAllowed": false,
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
      "protocolMappers": [
        {
          "config": {
            "access.token.claim": "true",
            "claim.name": "clientAddress",
            "id.token.claim": "true",
            "jsonType.label": "String",
            "user.session.note": "clientAddress",
            "userinfo.token.claim": "true"
          },
          "consentRequired": false,
          "name": "Client IP Address",
          "protocol": "openid-connect",
          "protocolMapper": "oidc-usersessionmodel-note-mapper"
        },
        {
          "config": {
            "access.token.claim": "true",
            "claim.name": "clientHost",
            "id.token.claim": "true",
            "jsonType.label": "String",
            "user.session.note": "clientHost",
            "userinfo.token.claim": "true"
          },
          "consentRequired": false,
          "name": "Client Host",
          "protocol": "openid-connect",
          "protocolMapper": "oidc-usersessionmodel-note-mapper"
        },
        {
          "config": {
            "access.token.claim": "true",
            "claim.name": "clientId",
            "id.token.claim": "true",
            "jsonType.label": "String",
            "user.session.note": "clientId",
            "userinfo.token.claim": "true"
          },
          "consentRequired": false,
          "name": "Client ID",
          "protocol": "openid-connect",
          "protocolMapper": "oidc-usersessionmodel-note-mapper"
        }
      ],
      "publicClient": false,
      "redirectUris": [],
      "secret": "[=clients.reportCli.secret]",
      "serviceAccountsEnabled": true,
      "standardFlowEnabled": false,
      "surrogateAuthRequired": false,
      "webOrigins": []
    }
  ],
  "defaultDefaultClientScopes": [
    "email",
    "profile",
    "role_list",
    "roles",
    "web-origins"
  ],
  "defaultOptionalClientScopes": [
    "address",
    "microprofile-jwt",
    "offline_access",
    "phone"
  ],
  "defaultRoles": [
    "offline_access",
    "uma_authorization"
  ],
  "directGrantFlow": "direct grant",
  "displayName": "SENS",
  "displayNameHtml": "<h1>Silent Eight Name Screening</h1>",
  "dockerAuthenticationFlow": "docker auth",
  "duplicateEmailsAllowed": false,
  "editUsernameAllowed": false,
  "enabled": true,
  "enabledEventTypes": [
    "CLIENT_DELETE",
    "CLIENT_DELETE_ERROR",
    "CLIENT_INITIATED_ACCOUNT_LINKING",
    "CLIENT_INITIATED_ACCOUNT_LINKING_ERROR",
    "CLIENT_LOGIN",
    "CLIENT_LOGIN_ERROR",
    "CLIENT_REGISTER",
    "CLIENT_REGISTER_ERROR",
    "CLIENT_UPDATE",
    "CLIENT_UPDATE_ERROR",
    "CODE_TO_TOKEN",
    "CODE_TO_TOKEN_ERROR",
    "CUSTOM_REQUIRED_ACTION",
    "CUSTOM_REQUIRED_ACTION_ERROR",
    "EXECUTE_ACTIONS",
    "EXECUTE_ACTIONS_ERROR",
    "EXECUTE_ACTION_TOKEN",
    "EXECUTE_ACTION_TOKEN_ERROR",
    "FEDERATED_IDENTITY_LINK",
    "FEDERATED_IDENTITY_LINK_ERROR",
    "GRANT_CONSENT",
    "GRANT_CONSENT_ERROR",
    "IDENTITY_PROVIDER_FIRST_LOGIN",
    "IDENTITY_PROVIDER_FIRST_LOGIN_ERROR",
    "IDENTITY_PROVIDER_LINK_ACCOUNT",
    "IDENTITY_PROVIDER_LINK_ACCOUNT_ERROR",
    "IDENTITY_PROVIDER_POST_LOGIN",
    "IDENTITY_PROVIDER_POST_LOGIN_ERROR",
    "IMPERSONATE",
    "IMPERSONATE_ERROR",
    "LOGIN",
    "LOGIN_ERROR",
    "LOGOUT",
    "LOGOUT_ERROR",
    "PERMISSION_TOKEN",
    "REGISTER",
    "REGISTER_ERROR",
    "REMOVE_FEDERATED_IDENTITY",
    "REMOVE_FEDERATED_IDENTITY_ERROR",
    "REMOVE_TOTP",
    "REMOVE_TOTP_ERROR",
    "RESET_PASSWORD",
    "RESET_PASSWORD_ERROR",
    "RESTART_AUTHENTICATION",
    "RESTART_AUTHENTICATION_ERROR",
    "REVOKE_GRANT",
    "REVOKE_GRANT_ERROR",
    "SEND_IDENTITY_PROVIDER_LINK",
    "SEND_IDENTITY_PROVIDER_LINK_ERROR",
    "SEND_RESET_PASSWORD",
    "SEND_RESET_PASSWORD_ERROR",
    "SEND_VERIFY_EMAIL",
    "SEND_VERIFY_EMAIL_ERROR",
    "TOKEN_EXCHANGE",
    "TOKEN_EXCHANGE_ERROR",
    "UPDATE_CONSENT",
    "UPDATE_CONSENT_ERROR",
    "UPDATE_EMAIL",
    "UPDATE_EMAIL_ERROR",
    "UPDATE_PASSWORD",
    "UPDATE_PASSWORD_ERROR",
    "UPDATE_PROFILE",
    "UPDATE_PROFILE_ERROR",
    "UPDATE_TOTP",
    "UPDATE_TOTP_ERROR",
    "VERIFY_EMAIL",
    "VERIFY_EMAIL_ERROR"
  ],
  "eventsEnabled": true,
  "eventsListeners": [
    "jboss-logging"
  ],
  "failureFactor": 30,
  "groups": [],
  "identityProviderMappers": [
    {
      "config": {
        "template": "${ATTRIBUTE.username}"
      },
      "identityProviderAlias": "saml",
      "identityProviderMapper": "saml-username-idp-mapper",
      "name": "Map NAMEID to username"
    }
  ],
  "internationalizationEnabled": false,
  "keycloakVersion": "8.0.1",
  "loginWithEmailAllowed": true,
  "maxDeltaTimeSeconds": 43200,
  "maxFailureWaitSeconds": 900,
  "minimumQuickLoginWaitSeconds": 60,
  "notBefore": 1576261806,
  "offlineSessionIdleTimeout": 2592000,
  "offlineSessionMaxLifespan": 5184000,
  "offlineSessionMaxLifespanEnabled": false,
  "otpPolicyAlgorithm": "HmacSHA1",
  "otpPolicyDigits": 6,
  "otpPolicyInitialCounter": 0,
  "otpPolicyLookAheadWindow": 1,
  "otpPolicyPeriod": 30,
  "otpPolicyType": "totp",
  "otpSupportedApplications": [
    "FreeOTP",
    "Google Authenticator"
  ],
  "passwordPolicy": "regexPattern(^(?=.*?[0-9])(?=.*?[A-Za-z]).{8,}$)",
  "permanentLockout": false,
  "quickLoginCheckMilliSeconds": 1000,
  "realm": "sens-webapp",
  "refreshTokenMaxReuse": 0,
  "registrationAllowed": false,
  "registrationEmailAsUsername": false,
  "registrationFlow": "registration",
  "rememberMe": false,
  "requiredActions": [
    {
      "alias": "CONFIGURE_TOTP",
      "config": {},
      "defaultAction": false,
      "enabled": true,
      "name": "Configure OTP",
      "priority": 10,
      "providerId": "CONFIGURE_TOTP"
    },
    {
      "alias": "UPDATE_PASSWORD",
      "config": {},
      "defaultAction": false,
      "enabled": true,
      "name": "Update Password",
      "priority": 30,
      "providerId": "UPDATE_PASSWORD"
    },
    {
      "alias": "UPDATE_PROFILE",
      "config": {},
      "defaultAction": false,
      "enabled": true,
      "name": "Update Profile",
      "priority": 40,
      "providerId": "UPDATE_PROFILE"
    },
    {
      "alias": "VERIFY_EMAIL",
      "config": {},
      "defaultAction": false,
      "enabled": true,
      "name": "Verify Email",
      "priority": 50,
      "providerId": "VERIFY_EMAIL"
    },
    {
      "alias": "terms_and_conditions",
      "config": {},
      "defaultAction": false,
      "enabled": false,
      "name": "Terms and Conditions",
      "priority": 20,
      "providerId": "terms_and_conditions"
    }
  ],
  "requiredCredentials": [
    "password"
  ],
  "resetCredentialsFlow": "reset credentials",
  "resetPasswordAllowed": false,
  "revokeRefreshToken": false,
  "roles": {
    "client": {
      "account": [
        {
          "attributes": {},
          "clientRole": true,
          "composite": true,
          "composites": {
            "client": {
              "account": [
                "manage-account-links"
              ]
            }
          },
          "description": "${role_manage-account}",
          "name": "manage-account"
        },
        {
          "attributes": {},
          "clientRole": true,
          "composite": false,
          "description": "${role_manage-account-links}",
          "name": "manage-account-links"
        },
        {
          "attributes": {},
          "clientRole": true,
          "composite": false,
          "description": "${role_view-profile}",
          "name": "view-profile"
        }
      ],
      "admin-cli": [],
      "backend": [
        {
          "attributes": {},
          "clientRole": true,
          "composite": false,
          "name": "audit-generate-report"
        },
        {
          "attributes": {},
          "clientRole": true,
          "composite": false,
          "name": "uma_protection"
        },
        {
          "attributes": {},
          "clientRole": true,
          "composite": false,
          "name": "user-manage"
        },
        {
          "attributes": {},
          "clientRole": true,
          "composite": false,
          "name": "user-view"
        }
      ],
      "broker": [
        {
          "attributes": {},
          "clientRole": true,
          "composite": false,
          "description": "${role_read-token}",
          "name": "read-token"
        }
      ],
      "frontend": [
        {
          "attributes": {},
          "clientRole": true,
          "composite": false,
          "name": "audit_generate_report"
        }
      ],
      "realm-management": [
        {
          "attributes": {},
          "clientRole": true,
          "composite": true,
          "composites": {
            "client": {
              "realm-management": [
                "create-client",
                "impersonation",
                "manage-authorization",
                "manage-clients",
                "manage-events",
                "manage-identity-providers",
                "manage-realm",
                "manage-users",
                "query-clients",
                "query-groups",
                "query-realms",
                "query-users",
                "view-authorization",
                "view-clients",
                "view-events",
                "view-identity-providers",
                "view-realm",
                "view-users"
              ]
            }
          },
          "description": "${role_realm-admin}",
          "name": "realm-admin"
        },
        {
          "attributes": {},
          "clientRole": true,
          "composite": true,
          "composites": {
            "client": {
              "realm-management": [
                "query-clients"
              ]
            }
          },
          "description": "${role_view-clients}",
          "name": "view-clients"
        },
        {
          "attributes": {},
          "clientRole": true,
          "composite": true,
          "composites": {
            "client": {
              "realm-management": [
                "query-groups",
                "query-users"
              ]
            }
          },
          "description": "${role_view-users}",
          "name": "view-users"
        },
        {
          "attributes": {},
          "clientRole": true,
          "composite": false,
          "description": "${role_create-client}",
          "name": "create-client"
        },
        {
          "attributes": {},
          "clientRole": true,
          "composite": false,
          "description": "${role_impersonation}",
          "name": "impersonation"
        },
        {
          "attributes": {},
          "clientRole": true,
          "composite": false,
          "description": "${role_manage-authorization}",
          "name": "manage-authorization"
        },
        {
          "attributes": {},
          "clientRole": true,
          "composite": false,
          "description": "${role_manage-clients}",
          "name": "manage-clients"
        },
        {
          "attributes": {},
          "clientRole": true,
          "composite": false,
          "description": "${role_manage-events}",
          "name": "manage-events"
        },
        {
          "attributes": {},
          "clientRole": true,
          "composite": false,
          "description": "${role_manage-identity-providers}",
          "name": "manage-identity-providers"
        },
        {
          "attributes": {},
          "clientRole": true,
          "composite": false,
          "description": "${role_manage-realm}",
          "name": "manage-realm"
        },
        {
          "attributes": {},
          "clientRole": true,
          "composite": false,
          "description": "${role_manage-users}",
          "name": "manage-users"
        },
        {
          "attributes": {},
          "clientRole": true,
          "composite": false,
          "description": "${role_query-clients}",
          "name": "query-clients"
        },
        {
          "attributes": {},
          "clientRole": true,
          "composite": false,
          "description": "${role_query-groups}",
          "name": "query-groups"
        },
        {
          "attributes": {},
          "clientRole": true,
          "composite": false,
          "description": "${role_query-realms}",
          "name": "query-realms"
        },
        {
          "attributes": {},
          "clientRole": true,
          "composite": false,
          "description": "${role_query-users}",
          "name": "query-users"
        },
        {
          "attributes": {},
          "clientRole": true,
          "composite": false,
          "description": "${role_view-authorization}",
          "name": "view-authorization"
        },
        {
          "attributes": {},
          "clientRole": true,
          "composite": false,
          "description": "${role_view-events}",
          "name": "view-events"
        },
        {
          "attributes": {},
          "clientRole": true,
          "composite": false,
          "description": "${role_view-identity-providers}",
          "name": "view-identity-providers"
        },
        {
          "attributes": {},
          "clientRole": true,
          "composite": false,
          "description": "${role_view-realm}",
          "name": "view-realm"
        }
      ],
      "report-cli": [],
      "security-admin-console": []
    },
    "realm": [
      {
        "attributes": {
          "origin": [
            "webapp"
          ]
        },
        "clientRole": false,
        "composite": true,
        "composites": {
          "client": {
            "account": [
              "view-profile"
            ],
            "backend": [
              "audit-generate-report"
            ]
          }
        },
        "name": "Auditor"
      },
      {
        "attributes": {
          "origin": [
            "webapp"
          ]
        },
        "clientRole": false,
        "composite": true,
        "composites": {
          "realm": [
            "Analyst",
            "Auditor",
            "Business Operator"
          ]
        },
        "name": "Admin"
      },
      {
        "attributes": {},
        "clientRole": false,
        "composite": false,
        "description": "${role_offline-access}",
        "name": "offline_access"
      },
      {
        "attributes": {},
        "clientRole": false,
        "composite": false,
        "description": "${role_uma_authorization}",
        "name": "uma_authorization"
      },
      {
        "attributes": {
          "origin": [
            "webapp"
          ]
        },
        "clientRole": false,
        "composite": true,
        "composites": {
          "client": {
            "account": [
              "view-profile"
            ]
          }
        },
        "name": "Analyst"
      },
      {
        "attributes": {
          "origin": [
            "webapp"
          ]
        },
        "clientRole": false,
        "composite": true,
        "composites": {
          "client": {
            "account": [
              "view-profile"
            ]
          }
        },
        "name": "Business Operator"
      }
    ]
  },
  "scopeMappings": [
    {
      "client": "report-cli",
      "roles": [
        "Auditor",
        "uma_authorization"
      ]
    },
    {
      "clientScope": "offline_access",
      "roles": [
        "offline_access"
      ]
    }
  ],
  "smtpServer": {},
  "sslRequired": "external",
  "ssoSessionIdleTimeout": 1800,
  "ssoSessionIdleTimeoutRememberMe": 0,
  "ssoSessionMaxLifespan": 36000,
  "ssoSessionMaxLifespanRememberMe": 0,
  "supportedLocales": [],
  "userManagedAccessAllowed": false,
  "users": [
    {
      "clientRoles": {
        "account": [
          "manage-account",
          "view-profile"
        ]
      },
      "createdTimestamp": 1583246982209,
      "credentials": [],
      "disableableCredentialTypes": [],
      "emailVerified": false,
      "enabled": true,
      "groups": [],
      "notBefore": 0,
      "realmRoles": [
        "Auditor",
        "offline_access",
        "uma_authorization"
      ],
      "requiredActions": [],
      "serviceAccountClientId": "report-cli",
      "totp": false,
      "username": "service-account-report-cli"
    },
    {
      "clientRoles": {
        "account": [
          "manage-account",
          "view-profile"
        ],
        "backend": [
          "uma_protection"
        ],
        "realm-management": [
          "manage-authorization",
          "manage-users",
          "query-users",
          "realm-admin",
          "view-authorization",
          "view-events",
          "view-realm",
          "view-users"
        ]
      },
      "createdTimestamp": 1583246982010,
      "credentials": [],
      "disableableCredentialTypes": [],
      "emailVerified": false,
      "enabled": true,
      "groups": [],
      "notBefore": 0,
      "realmRoles": [
        "Admin",
        "offline_access",
        "uma_authorization"
      ],
      "requiredActions": [],
      "serviceAccountClientId": "backend",
      "totp": false,
      "username": "service-account-backend"
    }
  ],
  "verifyEmail": false,
  "waitIncrementSeconds": 60,
  "webAuthnPolicyAcceptableAaguids": [],
  "webAuthnPolicyAttestationConveyancePreference": "not specified",
  "webAuthnPolicyAuthenticatorAttachment": "not specified",
  "webAuthnPolicyAvoidSameAuthenticatorRegister": false,
  "webAuthnPolicyCreateTimeout": 0,
  "webAuthnPolicyRequireResidentKey": "not specified",
  "webAuthnPolicyRpEntityName": "keycloak",
  "webAuthnPolicyRpId": "",
  "webAuthnPolicySignatureAlgorithms": [
    "ES256"
  ],
  "webAuthnPolicyUserVerificationRequirement": "not specified"
}
