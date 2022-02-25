FROM docker.io/library/rabbitmq:3.8.16-management

COPY docker/rabbitmq/definitions.json /etc/rabbitmq/
COPY docker/rabbitmq/rabbitmq_delayed_message_exchange-3.9.0.ez /opt/rabbitmq/plugins/
RUN rabbitmq-plugins enable rabbitmq_delayed_message_exchange
