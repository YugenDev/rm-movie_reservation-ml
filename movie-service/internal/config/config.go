package config

import (
	"database/sql"
	"fmt"
	"log"
	"os"
	"time"
)

var DB *sql.DB

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

func InitDB() error {
	var err error

	for i := 0; i < 15; i++ {
		connStr, err := GetDBConnectionString()
		if err != nil {
			return fmt.Errorf("failed to get DB connection %v", err)
		}

		DB, err := sql.Open("postgres", connStr)
		if err != nil {
			log.Printf("Error opening database connection (attempt %d): %v", i+1, err)
			time.Sleep(3 * time.Second)
			continue
		}

		if err = DB.Ping(); err != nil {
			log.Printf("Error pinging database (attempt %d): %v", i+1, err)
			time.Sleep(2 * time.Second)
			continue
		}

		log.Println("Database connection established")
		return nil
	}
	return fmt.Errorf("failed to connect to the database after 5 attempts: %v", err)
}

func CloseDB() {
	if err := DB.Close(); err != nil {
		log.Println("Error closing the database:", err)
	}
}
