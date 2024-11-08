package routes

import (
	"github.com/YugenDev/rm-movie_service-ml/internal/handlers"
	"github.com/labstack/echo"
)

func RegisterMovieRoutes(e *echo.Echo, handler *handlers.MovieHandler) {

	e.GET("/movies", handler.GetMovies)
	e.GET("/movies/:id", handler.GetMovieByID)
	e.GET("/movies/title/:title", handler.GetMoviesByTitle)
	e.GET("/movies/genre/:genre", handler.GetMoviesByGenre)

	e.POST("/movies", handler.CreateMovie)

	e.PUT("/movies/:id", handler.UpdateMovie)

}
