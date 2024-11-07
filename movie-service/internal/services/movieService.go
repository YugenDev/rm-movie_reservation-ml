package services

import "github.com/YugenDev/rm-movie_service-ml/internal/models"

func GetAllMovies() ([]models.Movie, error) {
	return movieRepo.GetAllMovies()
}
