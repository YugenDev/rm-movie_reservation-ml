package services

import (
	"github.com/YugenDev/rm-movie_service-ml/internal/models"
	"github.com/YugenDev/rm-movie_service-ml/internal/repositories"
	"github.com/labstack/echo"
	"net/http"
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

func (service *MovieService) CreateMovie(movie models.Movie) (models.Movie, error) {
	movies, _ := service.Repo.GetAllMovies()
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

	return service.Repo.CreateMovie(movie)
}
