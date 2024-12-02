package repositories

import (
	"github.com/YugenDev/rm-movie_reservation-ml/internal/models"
	"github.com/google/uuid"
	"gorm.io/gorm"
)

type ReservationRepository struct {
	DB *gorm.DB
}

func NewReservationRepository(db *gorm.DB) *ReservationRepository {
	return &ReservationRepository{DB: db}
}

func (repo *ReservationRepository) CreateReservation(reservation models.Reservation) (models.Reservation, error) {
	if err := repo.DB.Create(&reservation).Error; err != nil {
		return reservation, err
	}
	return reservation, nil
}

func (repo *ReservationRepository) GetReservationByUserId(id uuid.UUID) (models.Reservation, error) {
	var reservation models.Reservation
	if err := repo.DB.Where("user_id = ?", id).Find(&reservation).Error; err != nil {
		return reservation, err
	}
	return reservation, nil
}

func (repo *ReservationRepository) GetReservationByShowtimeId(id uuid.UUID) (models.Reservation, error) {
	var reservation models.Reservation
	if err := repo.DB.Where("showtime_id = ?", id).First(&reservation).Error; err != nil {
		return reservation, err
	}
	return reservation, nil
}

func (repo *ReservationRepository) UpdateReservation(reservation models.Reservation) (models.Reservation, error) {
	if err := repo.DB.Save(&reservation).Error; err != nil {
		return reservation, err
	}
	return reservation, nil
}

func (repo *ReservationRepository) DeleteReservation(id uuid.UUID) error {
	if err := repo.DB.Where("reservation_id = ?", id).Delete(&models.Reservation{}).Error; err != nil {
		return err
	}
	return nil
}
