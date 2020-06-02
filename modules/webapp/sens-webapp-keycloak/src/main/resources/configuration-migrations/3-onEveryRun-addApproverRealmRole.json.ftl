{
  "enabled": true,
  "realm": "sens-webapp",
  "roles": {
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
            ]
          }
        },
        "name": "Approver"
      }
    ]
  }
}
