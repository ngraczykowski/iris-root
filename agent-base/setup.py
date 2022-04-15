from setuptools import find_packages, setup

install_require = [
    "agents-api==0.7.0.33",
    "aio_pika==7.1.2",
    "aiohttp==3.7.4.post0",
    "data-source-api==0.18.0",
    "googleapis-common-protos==1.53.0",
    "grpcio==1.39.0",
    "grpcio-health-checking==1.39.0",
    "grpcio-reflection==1.39.0",
    "grpcio-tools==1.39.0",
    "lz4==3.1.3",
    "protocol-agents==0.18.0.90",
    "python-consul2==0.1.5",
    "pyyaml==5.4.1",
]

tests_require = [
    "black==22.3.0",
    "flake8-bugbear>=20.11.1",
    "flake8-comprehensions>=3.3.0",
    "flake8-import-order",
    "flake8-junit-report>=2.1.0",
    "flake8>=3.8.4",
    "isort",
    "mypy==0.790",
    "pytest-asyncio",
    "pytest>=6.1.2",
    "tox>=3.24.4",
]

setup(
    name="agent-base",
    version="0.18.0-dev",
    description="",
    author="Silent Eight Pte. Ltd.",
    author_email="support@silenteight.com",
    url="https://silenteight.com",
    license="Proprietary",
    packages=find_packages(exclude=("tests",)),
    classifiers=[
        "License :: Other/Proprietary License",
    ],
    python_requires=">=3.7",
    install_requires=install_require,
    extras_require={"tests": tests_require},
    setup_requires=[],
    tests_require=tests_require,
    entry_points={
        "console_scripts": [],
    },
    package_data={
        "": ["resources/*"],
    },
)
