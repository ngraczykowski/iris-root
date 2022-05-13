.DEFAULT_GOAL := build

.PHONY: help
help:
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-30s\033[0m %s\n", $$1, $$2}'

.PHONY: up
up: ## Starts up all the required services using Docker Compose
	docker-compose --compatibility up --detach

.PHONY: down
down: ## Stops Docker Compose services and removes all the data
	docker-compose --compatibility down --timeout 60 --volumes --remove-orphans

.PHONY: build
build: ## Builds the project
	./gradlew build

.PHONY: quick-build
quick-build: ## Builds the project without running tests
	./gradlew build -x test

.PHONY: full-rebuild
full-rebuild: ## Re-builds the project cleanly
	./gradlew clean build --no-build-cache
