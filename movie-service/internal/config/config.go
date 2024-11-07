package config

import (
	"fmt"
	"github.com/YugenDev/rm-movie_service-ml/internal/models"
	"gorm.io/driver/postgres"
	"gorm.io/gorm"
	"log"
	"os"
	"time"
)

var DB *gorm.DB

var (
	DBUser     = os.Getenv("POSTGRES_USER")
	DBPassword = os.Getenv("POSTGRES_PASSWORD")
	DBName     = os.Getenv("POSTGRES_DB")
	DBHost     = os.Getenv("POSTGRES_SERVER")
	DBPort     = os.Getenv("POSTGRES_PORT")
)

func setDefaultValues() string {
	if DBUser == "" {
		DBUser = "admin"
	}
	if DBPassword == "" {
		DBPassword = "admin123"
	}
	if DBName == "" {
		DBName = "movies"
	}
	if DBHost == "" {
		DBHost = "movies-db"
	}
	if DBPort == "" {
		DBPort = "5432"
	}

	return fmt.Sprintf("host=%s user=%s password=%s dbname=%s port=%s sslmode=disable",
		DBHost, DBUser, DBPassword, DBName, DBPort)
}

func GetDBConnectionString() (string, error) {
	if DBUser == "" || DBPassword == "" || DBName == "" || DBHost == "" || DBPort == "" {
		return setDefaultValues(), nil
	}
	return fmt.Sprintf("host=%s user=%s password=%s dbname=%s port=%s sslmode=disable",
		DBHost, DBUser, DBPassword, DBName, DBPort), nil
}

func InitDB() (*gorm.DB, error) {
	var err error

	for i := 0; i < 15; i++ {
		connStr, err := GetDBConnectionString()
		if err != nil {
			return nil, fmt.Errorf("failed to get DB connection %v", err)
		}

		db, err := gorm.Open(postgres.Open(connStr), &gorm.Config{})
		if err != nil {
			log.Printf("Error opening database connection (attempt %d): %v", i+1, err)
			time.Sleep(3 * time.Second)
			continue
		}

		sqlDB, err := db.DB()
		if err != nil {
			return nil, err
		}

		if err := sqlDB.Ping(); err != nil {
			log.Printf("Error pinging database (attempt %d): %v", i+1, err)
			time.Sleep(2 * time.Second)
			continue
		}

		log.Println("Database connection established")

		err = db.AutoMigrate(&models.Movie{})
		if err != nil {
			return nil, fmt.Errorf("failed to migrate database: %v", err)
		}

		log.Println("Database connection established")
		return db, nil
	}
	return nil, fmt.Errorf("failed to connect to the database after 5 attempts: %v", err)
}

func CloseDB(db *gorm.DB) {
	sqlDB, err := db.DB()
	if err != nil {
		log.Println("Error getting sql.DB from gorm.DB:", err)
		return
	}
	if err := sqlDB.Close(); err != nil {
		log.Println("Error closing the database:", err)
	}
}
