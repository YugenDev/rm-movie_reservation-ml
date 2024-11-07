package routes

import (
	"github.com/YugenDev/rm-movie_service-ml/internal/handlers"
	"github.com/labstack/echo"
)

func RegisterMovieRoutes(e *echo.Echo) {

	e.GET("/movies", handlers.GetMovies)

}
