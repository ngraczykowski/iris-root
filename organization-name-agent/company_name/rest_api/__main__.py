import argparse

import uvicorn
from fastapi import FastAPI

from company_name.rest_api.endpoints import router

app = FastAPI(
    title="Organization Name Agent",
    docs_url="/organization_name_agent/docs",
    openapi_url="/organization_name_agent/openapi.json",
)

app.include_router(router, prefix="/organization_name_agent")


def main():
    parser = argparse.ArgumentParser(description="Organization name agent")
    parser.add_argument(
        "--port", type=int, default=5000, help="Port on which service is to be available"
    )
    args = parser.parse_args()
    uvicorn.run(app, host="0.0.0.0", port=args.port)


if __name__ == "__main__":
    main()
