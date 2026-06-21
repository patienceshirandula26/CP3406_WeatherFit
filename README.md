# WeatherFit 🌦️

## Overview

WeatherFit is a utility-style Android application developed for **CP3406 Mobile Computing**.

The application provides real-time weather information and clothing recommendations to help users quickly decide what to wear before leaving home. By combining live weather data with practical outfit suggestions, WeatherFit delivers focused, at-a-glance information that supports everyday decision-making.

---

## Getting Started

### How to Run

1. Clone or download this repository.
2. Open the project in Android Studio.
3. Allow Gradle to sync all dependencies.
4. Run the application on an Android emulator or physical Android device (API 26+ recommended).

---

## Features

### Current Weather

* Displays current temperature.
* Displays current weather conditions.
* Displays feels-like temperature.
* Displays humidity information.
* Retrieves live weather data from the Open-Meteo API.

### Fit Check

* Provides clothing recommendations based on current weather conditions.
* Suggests appropriate outerwear.
* Suggests suitable footwear.
* Provides umbrella recommendations during wet weather conditions.

### 3-Day Forecast

* Displays forecast temperatures for upcoming days.
* Displays weather condition icons.
* Provides users with a quick overview of upcoming weather trends.

### Settings

* Toggle between Celsius and Fahrenheit temperature units.
* Select appearance preferences (System, Light, or Dark mode).
* Enable or disable severe weather sound alerts.

---

## Technologies Used

| Component               | Technology                       |
| ----------------------- | -------------------------------- |
| Programming Language    | Kotlin                           |
| User Interface          | Jetpack Compose                  |
| Design Framework        | Material Design 3                |
| Architecture            | ViewModel and Repository Pattern |
| Networking              | Retrofit                         |
| Weather Service         | Open-Meteo API                   |
| Development Environment | Android Studio                   |

---

## App Architecture

WeatherFit follows a modern Android architecture to separate presentation, business logic, and data retrieval responsibilities.

```text
UI (Jetpack Compose)
        ↓
WeatherViewModel
        ↓
WeatherRepository
        ↓
Retrofit API Service
        ↓
Open-Meteo API
```

### Architecture Components

#### UI Layer

The user interface is built using Jetpack Compose and is responsible for displaying weather information, forecasts, outfit recommendations, and settings.

#### ViewModel Layer

The ViewModel manages application state and prepares weather information for display within the user interface.

#### Repository Layer

The Repository acts as an intermediary between the ViewModel and the weather service, managing data retrieval operations.

#### API Layer

Retrofit is used to communicate with the Open-Meteo API and retrieve weather data from the internet.

---

## Key Concepts Covered

| Week   | Concept                   | Implementation                            |
| ------ | ------------------------- | ----------------------------------------- |
| Week 1 | Kotlin and Android Studio | MainActivity and project setup            |
| Week 2 | Jetpack Compose Layouts   | Utility Screen and Settings Screen        |
| Week 3 | Material Design 3         | WeatherFit custom UI theme and styling    |
| Week 4 | ViewModel Architecture    | WeatherViewModel                          |
| Week 5 | Retrofit API Integration  | Open-Meteo weather service implementation |

---

## Future Improvements

Potential future enhancements include:

* GPS-based location detection.
* Support for multiple cities.
* Persistent user preferences using DataStore.
* Extended weather forecasts.
* Additional clothing recommendation categories.
* Weather notifications and alerts.

---

## Author

**Patience Shirandula**

CP3406 Mobile Computing

James Cook University
