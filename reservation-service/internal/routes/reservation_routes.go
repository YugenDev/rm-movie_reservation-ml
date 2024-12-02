package routes

import (
	"github.com/YugenDev/rm-movie_reservation-ml/internal/handlers"
	"github.com/labstack/echo/v4"
)

func RegisterReservationRoutes(e *echo.Echo, handler *handlers.ReservationHandler) {

	e.POST("/reservations", handler.CreateReservation)
	e.PUT("/reservations/:id", handler.UpdateReservation)
	e.GET("/reservations/user/:user_id", handler.GetReservationByUserId)
	e.GET("/reservations/showtime/:showtime_id", handler.GetReservationByShowtimeId)
	e.DELETE("/reservations/:id", handler.DeleteReservation)

}
