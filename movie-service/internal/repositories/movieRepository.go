package repositories

import (
	"github.com/YugenDev/rm-movie_service-ml/internal/models"
	"gorm.io/gorm"
)

type MovieRepository struct {
	DB *gorm.DB
}

func NewMovieRepository(db *gorm.DB) *MovieRepository {
	return &MovieRepository{DB: db}
}

func (repo *MovieRepository) GetAllMovies() ([]models.Movie, error) {
	var movies []models.Movie
	if err := repo.DB.Find(&movies).Error; err != nil {
		return nil, err
	}
	return movies, nil
}

func (repo *MovieRepository) CreateMovie(movie models.Movie) (models.Movie, error) {
	result := repo.DB.Create(&movie)
	if result.Error != nil {
		return models.Movie{}, result.Error
	}
	return movie, nil
}
