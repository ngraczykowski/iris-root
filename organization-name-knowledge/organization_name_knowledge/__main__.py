import argparse

import uvicorn
from fastapi import FastAPI

from organization_name_knowledge.endpoints import router

app = FastAPI(
    title="Organization Name Knowledge",
    docs_url="/organization_name_knowledge/docs",
    openapi_url="/organization_name_knowledge/openapi.json",
)

app.include_router(router, prefix="/organization_name_knowledge")


def main():
    parser = argparse.ArgumentParser(description="Organization name knowledge agent")
    parser.add_argument(
        "--port", type=int, default=9090, help="Port on which service is to be available"
    )
    args = parser.parse_args()
    uvicorn.run(app, host="0.0.0.0", port=args.port)


if __name__ == "__main__":
    main()
