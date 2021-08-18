from setuptools import setup, find_packages

install_require = [
    "agents-api",
    "aio_pika",
    "aiohttp",
    "data-source-api",
    "googleapis-common-protos",
    "grpcio",
    "grpcio-health-checking",
    "grpcio-reflection",
    "grpcio-tools",
    "lz4",
    "protocol-agents",
    "python-consul2",
    "pyyaml",
]

tests_require = [
    "black>=20.8b1",
    "flake8-bugbear>=20.11.1",
    "flake8-comprehensions>=3.3.0",
    "flake8-import-order",
    "flake8-junit-report>=2.1.0",
    "flake8>=3.8.4",
    "isort",
    "mypy==0.790",
    "pytest-asyncio",
    "pytest>=6.1.2",
    "tox>=3.20.1",
]

setup(
    name="agent-base",
    version="0.2.0-dev",
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
