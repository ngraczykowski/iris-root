### Setup

1. Clone Common Docker Infra repo: https://gitlab.silenteight.com/sens/common-docker-infrastructure.
2. Start docker services from Common Docker Infra repo (follow README in repo).
3. Start docker services from this repo using:

       docker-compose up -d

### Enable mocks

To test application with mocked dependencies activate following Spring Profiles:

    mock-governance
