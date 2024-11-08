package handlers

import (
	"net/http"

	"github.com/YugenDev/rm-movie_service-ml/internal/models"
	"github.com/YugenDev/rm-movie_service-ml/internal/services"
	"github.com/labstack/echo"
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

func (h *MovieHandler) GetMovieByID(c echo.Context) error {
	id := c.Param("movie_id")
	movie, err := h.Service.GetMovieByID(id)
	if err != nil {
		return err
	}
	return c.JSON(http.StatusOK, movie)
}

func (h *MovieHandler) GetMovieByTitle(c echo.Context) error {
	title := c.Param("title")
	movie, err := h.Service.GetMovieByTitle(title)
	if err != nil {
		return err
	}
	return c.JSON(http.StatusOK, movie)
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

func (h *MovieHandler) UpdateMovie(c echo.Context) error {
	id := c.Param("movie_id")
	var movie models.Movie
	if err := c.Bind(&movie); err != nil {
		return echo.NewHTTPError(http.StatusBadRequest, "Invalid movie data")
	}

	movie.MovieId = id
	updatedMovie, err := h.Service.UpdateMovie(id, movie)
	if err != nil {
		return err
	}

	return c.JSON(http.StatusOK, updatedMovie)
}
