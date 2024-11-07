package utils

import (
	"fmt"
	"github.com/YugenDev/rm-movie_service-ml/internal/config"
	"github.com/rabbitmq/amqp091-go"
	"log"
)

func DeclareQueue(queueName string) (amqp091.Queue, error) {
	queue, err := config.RabbitMQChannel.QueueDeclare(
		queueName, // queue name
		true,      // durable - RabbitMQ will never lose the queue if a crash occurs.
		false,     // autoDelete - the queue will be deleted once the channel is closed.
		false,     // exclusive - used by only one connection and the queue will be deleted once that connection closes.
		false,     // noWait - will block the channel from sending other commands until this command is complete.
		nil,       // arguments - optional; used by plugins and broker-specific features such as message TTL, queue length limit, etc.
	)
	if err != nil {
		log.Printf("Failed to declare a queue: %v", err)
		return amqp091.Queue{}, fmt.Errorf("failed to declare a queue: %v", err)
	}

	return queue, nil
}

func PublishMessage(queueName string, message string) error {
	queue, err := DeclareQueue(queueName)
	if err != nil {
		return err
	}

	err = config.RabbitMQChannel.Publish(
		"",
		queue.Name,
		false,
		false,
		amqp091.Publishing{
			ContentType: "text/plain",
			Body:        []byte(message),
		},
	)
	if err != nil {
		log.Printf("Failed to publish a message: %v", err)
		return fmt.Errorf("failed to publish a message: %v", err)
	}

	log.Printf("Message sent to queue: %s", message)
	return nil
}

func ConsumeMessages(queueName string) (<-chan amqp091.Delivery, error) {
	queue, err := DeclareQueue(queueName)
	if err != nil {
		return nil, err
	}

	messages, err := config.RabbitMQChannel.Consume(
		queue.Name, // queue name
		"",         // consumer
		true,       // autoAck - acknowledge the message once it is received
		false,      // exclusive - used by only one connection and the queue will be deleted once that connection closes.
		false,      // noLocal - if true, the server will not send messages to the connection that published them.
		false,      // noWait - will block the channel from sending other commands until this command is complete.
		nil,        // arguments - optional; used by plugins and broker-specific features such as message TTL, queue length limit, etc.
	)
	if err != nil {
		log.Printf("Failed to consume messages: %v", err)
		return nil, fmt.Errorf("failed to consume messages: %v", err)
	}

	return messages, nil
}
