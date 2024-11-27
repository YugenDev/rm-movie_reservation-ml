package broker_service

import (
	"github.com/YugenDev/rm-movie_reservation-ml/internal/utils"
	"log"
)

func StartConsumers() {
	userMessages, err := utils.ConsumeMessages("user_notifications")
	if err != nil {
		log.Fatalf("Failed to start User consumer %v", err)
	}

	showtimeMessages, err := utils.ConsumeMessages("showtime_notifications")
	if err != nil {
		log.Fatalf("Failed to start Showtime consumer %v", err)
	}

	go func() {
		for msg := range userMessages {
			log.Printf("Received a message: %s", msg.Body)
		}
	}()

	go func() {
		for msg := range showtimeMessages {
			log.Printf("Received a message: %s", msg.Body)
		}
	}()
}
