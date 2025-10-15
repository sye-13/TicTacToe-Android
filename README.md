# Simple Tic Tac Toe

A simple Tic Tac Toe implementation built with Kotlin and Jetpack Compose, emphasizing clean architecture principles such as separation of concerns, modularity, and testability.

---

## Setup Instructions

1. Clone the repository:
    ```bash
    git clone https://github.com/sye-13/TicTacToe-Android.git
    ```
2. Open the project in Android Studio.
3. Build the project and run it on an emulator or physical device.
4. To run unit tests:
    ```bash
    ./gradlew test
    ```

---

## Tech Stack and Tools

- **Kotlin**: primary language for app development
- **Jetpack Compose**: declarative UI framework
- **MVVM + Unidirectional Data Flow**: architecture pattern for predictable state management
- **StateFlow**: reactive UI state handling
- **Hilt**: dependency injection
- **Jetpack Compose Navigation**: for screen navigation
- **JUnit & MockK**: testing and mocking
- **Multi-module Project**: isolates core and feature modules
- **Localization**: supports English and French
- **Accessibility**: uses content descriptions and semantics for screen reader support

---

## Project Structure

The project follows a modular architecture with the following main modules at the root level:

```
.
├── core
│   └── designsystem
│       └── ui/theme
├── feature
│   └── tictactoe
│       ├── di
│       ├── domain
│       └── ui
└── README.md
```

- `core/designsystem`: shared theming and design components
- `feature/tictactoe`: game feature including domain logic, UI, and DI setup

This modular structure allows better maintainability, testability and clean architectural boundaries.

---

## Architecture & Design Decisions

The project is structured using clean architecture principles, separating concerns across the following layers:

- **Domain**: encapsulates business logic and use cases, independent of the Android framework
- **Presentation**: handles UI and state management using MVVM and StateFlow
- **Dependency Injection**: managed by Hilt for lifecycle-aware injection and loose coupling

A unidirectional data flow ensures predictable state transitions and simplifies debugging.

---

## Testing Strategy

Unit tests are included to validate use cases and ViewModel behavior. Dependencies are mocked to isolate logic and ensure tests remain focused and deterministic.
