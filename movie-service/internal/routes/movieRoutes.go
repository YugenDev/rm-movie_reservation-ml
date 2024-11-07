package routes

import (
	"github.com/YugenDev/rm-movie_service-ml/internal/handlers"
	"github.com/labstack/echo"
)

func RegisterMovieRoutes(e *echo.Echo, handler *handlers.MovieHandler) {

	e.GET("/movies", handler.GetMovies)

	e.POST("/movies", handler.CreateMovie)

}
