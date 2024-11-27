package config

import (
	"github.com/rabbitmq/amqp091-go"
	"log"
	"os"
	"time"
)

var RabbitMQChannel *amqp091.Channel
var RabbitMQConnection *amqp091.Connection

var rabbitMQURL = os.Getenv("RABBITMQ_URL")

func SetDefaultValues() string {
	if rabbitMQURL == "" {
		rabbitMQURL = "amqp://guest:guest@rabbitmq:5672/"
	}
	return rabbitMQURL
}

func init() {
	url := SetDefaultValues()

	for i := 0; i < 15; i++ {
		conn, err := amqp091.Dial(url)
		if err != nil {
			log.Printf("error connecting to rabbitMQ (attempt %d): %v", i+1, err)
			time.Sleep(3 * time.Second)
			continue
		}

		RabbitMQConnection = conn

		channel, err := conn.Channel()
		if err != nil {
			log.Printf("Error opening queue connection (attempt %d): %v", i+1, err)
			time.Sleep(3 * time.Second)
			continue
		}

		RabbitMQChannel = channel
	}

	log.Println("RabbitMQ connection established")
}

func CloseRabbitMQ() {
	if err := RabbitMQChannel.Close(); err != nil {
		log.Fatalf("Failed to close RabbitMQ channel: %v", err)
	}
	if err := RabbitMQConnection.Close(); err != nil {
		log.Fatalf("Failed to close RabbitMQ connection: %v", err)
	}
}
