package models

import (
	"gorm.io/gorm"
	"time"
)

type Movie struct {
	MovieId        string         `gorm:"type:uuid;default:gen_random_uuid();primaryKey" json:"movie_id"`
	Title          string         `gorm:"type:varchar(255);not null" json:"title"`
	Description    string         `gorm:"type:text;not null" json:"description"`
	PosterImageUrl string         `gorm:"type:varchar(255)" json:"poster_image_url"`
	Genre          string         `gorm:"type:varchar(100)" json:"genre"`
	CreatedAt      time.Time      `gorm:"type:timestamp;default:CURRENT_TIMESTAMP" json:"created_at"`
	UpdatedAt      time.Time      `gorm:"type:timestamp;default:CURRENT_TIMESTAMP" json:"updated_at"`
	DeletedAt      gorm.DeletedAt `gorm:"index" json:"-"`
}
