package handlers

import (
	"github.com/YugenDev/rm-movie_service-ml/internal/models"
	"github.com/YugenDev/rm-movie_service-ml/internal/services"
	"github.com/labstack/echo"
	"net/http"
)

type MovieHandler struct {
	Service *services.MovieService
}

func NewMovieHandler(service *services.MovieService) *MovieHandler {
	return &MovieHandler{Service: service}
}

func (h *MovieHandler) GetMovies(c echo.Context) error {
	movies, err := h.Service.GetAllMovies()
	if err != nil {
		return echo.NewHTTPError(http.StatusInternalServerError, "failed to get movies")
	}
	return c.JSON(http.StatusOK, movies)
}

func (h *MovieHandler) CreateMovie(c echo.Context) error {
	var movie models.Movie
	if err := c.Bind(&movie); err != nil {
		return echo.NewHTTPError(http.StatusBadRequest, "Invalid movie data")
	}

	createdMovie, err := h.Service.CreateMovie(movie)
	if err != nil {
		return err
	}

	return c.JSON(http.StatusCreated, createdMovie)
}
