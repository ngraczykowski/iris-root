.PHONY: up
up:
	docker-compose --compatibility up --detach

.PHONY: down
down:
	docker-compose --compatibility down --timeout 60 --volumes --remove-orphans
