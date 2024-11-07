package main

import (
	"github.com/YugenDev/rm-movie_service-ml/internal/config"
	"github.com/YugenDev/rm-movie_service-ml/internal/handlers"
	"github.com/YugenDev/rm-movie_service-ml/internal/repositories"
	"github.com/YugenDev/rm-movie_service-ml/internal/routes"
	"github.com/YugenDev/rm-movie_service-ml/internal/services"
	"github.com/labstack/echo"
	"log"
	"net/http"
)

func main() {

	db, err := config.InitDB()
	if err != nil {
		log.Fatal("Error initializing database: ", err)
	}
	defer config.CloseDB(db)

	movieRepo := repositories.NewMovieRepository(db)
	movieService := services.NewMovieService(movieRepo)
	movieHandler := handlers.NewMovieHandler(movieService)

	e := echo.New()
	e.GET("/", func(c echo.Context) error {
		return c.String(http.StatusOK, "Movie service is up! 🐹🎥🚀")
	})

	routes.RegisterMovieRoutes(e, movieHandler)

	e.Logger.Fatal(e.Start(":8081"))
}
