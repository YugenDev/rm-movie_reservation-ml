package models

import (
	"github.com/google/uuid"
	"time"
)

type Reservation struct {
	ReservationID uuid.UUID `gorm:"type:uuid;default:uuid_generate_v4();primary_key"`
	UserID        uuid.UUID `gorm:"type:uuid;not null"`
	ShowtimeID    uuid.UUID `gorm:"type:uuid;not null"`
	ReservedSeats string    `gorm:"type:jsonb;not null"`
	CreatedAt     time.Time `gorm:"default:CURRENT_TIMESTAMP"`
	UpdatedAt     time.Time `gorm:"default:CURRENT_TIMESTAMP"`
}

func (Reservation) TableName() string {
	return "reservations"
}
