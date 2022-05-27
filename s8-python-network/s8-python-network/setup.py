from setuptools import find_packages, setup

install_require = [
    "aiohttp==3.7.4.post0",
    "grpcio==1.39.0",
    "grpcio-health-checking==1.39.0",
    "grpcio-reflection==1.39.0",
    "grpcio-tools==1.39.0",
    "protobuf==3.19.4",
    "psutil==5.9.0",
    "python-consul2==0.1.5",
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
    name="s8-python-network",
    version="0.0.2",
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
