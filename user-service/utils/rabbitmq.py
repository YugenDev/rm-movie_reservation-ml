import pika


class RabbitMQConnection:
    def __init__(self, host: str, port: int, user: str, password: str):
        self.host = host
        self.port = port
        self.user = user
        self.password = password
        self.connection = None
        self.channel = None

    def connect(self):
        credentials = pika.PlainCredentials(self.user, self.password)
        parameters = pika.ConnectionParameters(host=self.host, port=self.port, credentials=credentials)
        self.connection = pika.BlockingConnection(parameters)
        self.channel = self.connection.channel()

    def declare_queue(self, queue_name: str):
        if self.channel is None:
            raise Exception("No RabbitMQ channel available. Did you call connect()?")

        self.channel.queue_declare(queue=queue_name, durable=True)

    def publish_message(self, queue_name: str, message: str):
        if self.channel is None:
            raise Exception("No RabbitMQ channel available. Did you call connect()?")

        self.channel.basic_publish(
            exchange='',
            routing_key=queue_name,
            body=message,
            properties=pika.BasicProperties(
                delivery_mode=2,  # Make message persistent
            )
        )

    def close(self):
        if self.connection:
            self.connection.close()
