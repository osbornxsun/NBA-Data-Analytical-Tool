# Project Name

## Team Information

### Team Members
-Osborn Sun

---

## Project Description

### Domain
Sports analytics (NBA). The application pulls real NBA data from an external API (api-sports.io)  and lets users explore team performance through summary tables and game outcome predictions.

[Project description]

---

## User Stories

### User Story 1 - Team Season Summary (Osborn Sun)
As a basketball fan, I want to enter an NBA team's name and a season year so that I can see a summary of that team's performance that season in a clean table.

### User Story 2

### User Story 3

(Add additional user stories as needed.)

---

## Use Cases

### Use Case 1 - Search Team Season Statistics (Osborn Sun)
Name: Search team season stats

Primary actor: User

Preconditions: The application is running and the API key is configured and properly returns data.

Trigger: The user clicks Search after entering a team name and a season year.

Main success scenario:
1. The user enters a team name and selects a season year.
2. The user clicks Search.
3. The system looks up the team in the API to get its team ID.
4. The system requests that team's games for the chosen season.
5. The system displays the summary in a table.

### Use Case 2

### Use Case 3

(Add additional use cases as needed.)

---

## Minimal Viable Product (MVP)

| Team Member | MVP Use Case |
|-------------|--------------|
| Osborn Sun | Search Team Season Stats (interactor, controller, presenter, and the Team Stats view with its results table) |
| Name | Use Case |
| Name | Use Case |

---

## Entities

### Entity 1

### Entity 2

(Add additional entities as needed.)

---

## API Usage

**API Name:** API-NBA (API-SPORTS)

**Documentation:** [https://api-sports.io/documentation/nba/v2]

**How it is used in the program:**
Requests go to https://v2.nba.api-sports.io, with the API key sent in the x-apisports-key header. The key is read from an environment variable and is not committed to the repo.

---

## Design Diagrams

### UML Class Diagram


### Sequence Diagrams

---

## How to Run

[Instructions for running the program.]

---

## Notes

[Any additional project information.]

