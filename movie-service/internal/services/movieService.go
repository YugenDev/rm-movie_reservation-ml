package services

import (
	"fmt"
	"net/http"

	"github.com/YugenDev/rm-movie_service-ml/internal/models"
	"github.com/YugenDev/rm-movie_service-ml/internal/repositories"
	"github.com/YugenDev/rm-movie_service-ml/internal/utils"
	"github.com/labstack/echo"
)

type MovieService struct {
	Repo *repositories.MovieRepository
}

func NewMovieService(repo *repositories.MovieRepository) *MovieService {
	return &MovieService{Repo: repo}
}

func (service *MovieService) GetAllMovies() ([]models.Movie, error) {
	return service.Repo.GetAllMovies()
}

func (service *MovieService) GetMovieByID(id string) (models.Movie, error) {
	return service.Repo.GetMovieByID(id)
}

func (service *MovieService) GetMoviesByTitle(title string) ([]models.Movie, error) {
	return service.Repo.GetMoviesByTitle(title)
}

func (service *MovieService) CreateMovie(movie models.Movie) (models.Movie, error) {
	movies, err := service.Repo.GetAllMovies()
	if err != nil {
		return models.Movie{}, echo.NewHTTPError(http.StatusInternalServerError, "Failed to fetch existing movies")
	}

	for _, m := range movies {
		if m.Title == movie.Title && m.Description == movie.Description {
			return models.Movie{}, echo.NewHTTPError(http.StatusBadRequest, "Movie already exists")
		}
	}

	if movie.Title == "" {
		return models.Movie{}, echo.NewHTTPError(http.StatusBadRequest, "title is required")
	}
	if movie.Description == "" {
		return models.Movie{}, echo.NewHTTPError(http.StatusBadRequest, "description is required")
	}

	message := fmt.Sprintf("Movie '%s' has been created", movie.Title)
	err = utils.PublishMessage("movie_created", message)
	if err != nil {
		return models.Movie{}, echo.NewHTTPError(http.StatusInternalServerError, "failed to publish message")
	}

	return service.Repo.CreateMovie(movie)
}

func (service *MovieService) UpdateMovie(id string, movie models.Movie) (models.Movie, error) {
	m, err := service.Repo.GetMovieByID(id)
	if err != nil {
		return models.Movie{}, echo.NewHTTPError(http.StatusNotFound, "movie not found")
	}

	if movie.Title == "" {
		movie.Title = m.Title
	}
	if movie.Description == "" {
		movie.Description = m.Description
	}
	if movie.PosterImageUrl == "" {
		movie.PosterImageUrl = m.PosterImageUrl
	}
	if movie.Genre == "" {
		movie.Genre = m.Genre
	}

	movie.MovieId = m.MovieId
	movie.CreatedAt = m.CreatedAt

	return service.Repo.UpdateMovie(movie)
}
