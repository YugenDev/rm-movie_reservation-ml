package main

import (
	"github.com/YugenDev/rm-movie_reservation-ml/internal/config"
	"github.com/YugenDev/rm-movie_reservation-ml/internal/services/broker_service"
	"github.com/labstack/echo/v4"
)

func main() {
	db, err := config.InitDB()
	if err != nil {
		panic(err)
	}
	defer config.CloseDB(db)

	broker_service.StartConsumers()

	e := echo.New()
	e.GET("/", func(c echo.Context) error {
		return c.String(200, "I love hamsters 🐹!")
	})

	e.Logger.Fatal(e.Start(":8083"))
}
