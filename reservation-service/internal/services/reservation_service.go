package services

import (
	"github.com/YugenDev/rm-movie_reservation-ml/internal/models"
	"github.com/YugenDev/rm-movie_reservation-ml/internal/repositories"
	"github.com/google/uuid"
)

type ReservationService struct {
	Repo *repositories.ReservationRepository
}

func NewReservationService(repo *repositories.ReservationRepository) *ReservationService {
	return &ReservationService{Repo: repo}
}

// TODO: Publish a rabbitmq message
func (service *ReservationService) CreateReservation(reservation models.Reservation) (models.Reservation, error) {
	return service.Repo.CreateReservation(reservation)
}

func (service *ReservationService) GetReservationByUserId(id uuid.UUID) (models.Reservation, error) {
	return service.Repo.GetReservationByUserId(id)
}

func (service *ReservationService) GetReservationByShowtimeId(id uuid.UUID) (models.Reservation, error) {
	return service.Repo.GetReservationByShowtimeId(id)
}

// TODO: Publish a rabbitmq message
func (service *ReservationService) UpdateReservation(reservation models.Reservation) (models.Reservation, error) {
	return service.Repo.UpdateReservation(reservation)
}

// TODO: Publish a rabbitmq message
func (service *ReservationService) DeleteReservation(id uuid.UUID) error {
	return service.Repo.DeleteReservation(id)
}
