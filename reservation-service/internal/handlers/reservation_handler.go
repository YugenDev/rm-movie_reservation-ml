package handlers

import (
	"github.com/YugenDev/rm-movie_reservation-ml/internal/models"
	"github.com/YugenDev/rm-movie_reservation-ml/internal/services"
	"github.com/google/uuid"
	"github.com/labstack/echo/v4"
)

type ReservationHandler struct {
	Service *services.ReservationService
}

func NewReservationHandler(service *services.ReservationService) *ReservationHandler {
	return &ReservationHandler{Service: service}
}

func (handler *ReservationHandler) CreateReservation(c echo.Context) error {
	var reservation models.Reservation
	if err := c.Bind(&reservation); err != nil {
		return echo.NewHTTPError(400, err.Error())
	}

	createdReservation, err := handler.Service.CreateReservation(reservation)
	if err != nil {
		return echo.NewHTTPError(500, err.Error())
	}

	return c.JSON(201, createdReservation)
}

func (handler *ReservationHandler) GetReservationByUserId(c echo.Context) error {
	id := c.Param("user_id")

	userId, err := uuid.Parse(id)
	if err != nil {
		return echo.NewHTTPError(400, err.Error())
	}

	reservation, err := handler.Service.GetReservationByUserId(userId)
	if err != nil {
		return echo.NewHTTPError(500, err.Error())
	}

	return c.JSON(200, reservation)
}

func (handler *ReservationHandler) GetReservationByShowtimeId(c echo.Context) error {
	id := c.Param("showtime_id")

	showtimeId, err := uuid.Parse(id)
	if err != nil {
		return echo.NewHTTPError(400, err.Error())
	}

	reservation, err := handler.Service.GetReservationByShowtimeId(showtimeId)
	if err != nil {
		return echo.NewHTTPError(500, err.Error())
	}

	return c.JSON(200, reservation)
}

func (handler *ReservationHandler) UpdateReservation(c echo.Context) error {
	var reservation models.Reservation
	if err := c.Bind(&reservation); err != nil {
		return echo.NewHTTPError(400, err.Error())
	}

	updatedReservation, err := handler.Service.UpdateReservation(reservation)
	if err != nil {
		return echo.NewHTTPError(500, err.Error())
	}

	return c.JSON(200, updatedReservation)
}

func (handler *ReservationHandler) DeleteReservation(c echo.Context) error {
	id := c.Param("id")

	reservationId, err := uuid.Parse(id)
	if err != nil {
		return echo.NewHTTPError(400, err.Error())
	}

	err = handler.Service.DeleteReservation(reservationId)
	if err != nil {
		return echo.NewHTTPError(500, err.Error())
	}

	return c.NoContent(204)
}
