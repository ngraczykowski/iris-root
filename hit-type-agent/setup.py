from setuptools import find_packages, setup

install_require = [
    "agent-base==0.23.0",
    "agents-api>=0.27.0",
    "aio-pika==7.1.2",
    "aiohttp==3.7.4.post0",
    "data-source-api==0.23.0.12",
    "googleapis-common-protos==1.53.0",
    "grpcio>=1.46.3",
    "grpcio-tools>=1.46.3",
    "importlib_resources==5.2.2",
    "psutil==5.9.0",
    "python-consul2==0.1.5",
    "sentry_sdk==1.5.4",
]

tests_require = [
    "black>=22.1.0",
    "flake8-bugbear>=20.11.1",
    "flake8-comprehensions>=3.3.0",
    "flake8-import-order",
    "flake8-junit-report>=2.1.0",
    "flake8>=3.8.4",
    "isort",
    "mypy==0.790",
    "pytest-asyncio",
    "pytest>=6.1.2",
    "shiv",
    "tox>=3.20.1",
]

setup(
    name="hit-type",
    version="0.4.0",
    description="",
    author="Silent Eight Pte. Ltd.",
    author_email="support@silenteight.com",
    url="https://silenteight.com",
    license="Proprietary",
    packages=find_packages(exclude=("tests",)),
    classifiers=[
        "License :: Other/Proprietary License",
    ],
    python_requires=">=3.7.*",
    install_requires=install_require,
    extras_require={"tests": tests_require},
    setup_requires=[],
    tests_require=tests_require,
    entry_points={
        "console_scripts": [],
    },
)
