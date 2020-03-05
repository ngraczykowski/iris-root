{
  "frontend": [
    {
      "firstBaseUrl": "[=FRONTEND_BASE_URL.first]",
      "secondBaseUrl": "[=FRONTEND_BASE_URL.second]",
      "secret": [[#list FRONTEND_REDIRECT_URLS as url]
        "[=url]"[#if !url?is_last],[/#if][/#list]
      ]
    }
  ]
}
