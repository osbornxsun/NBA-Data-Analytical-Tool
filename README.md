# Project Name

## Team Information

### Team Members
-Osborn Sun
-Shivanshu Rajeev
-Jiahong Zheng
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
As a basketball fan, I want to enter the names of two competing teams and the date of their match so that I can view each team's predicted probability of winning.

### User Story 3 (Quinton Gao)
As a basketball fan, I want to enter the names of two competing teams so I can view the potential MVP for their next matchup.

### User Story 4 - Match Statistics (Jiahong Zheng)
As a basketball fan, I want to select a completed NBA game and view both teams' statistics so that I can compare their performances and understand the outcome of the match.
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

### Use Case 2 - Predict Match Outcome (Shivanshu Rajeev)
Name: Predict match outcome

Primary actor: User

Preconditions: The application is running and the API key is configured and properly returns data.

Trigger: The user clicks Predict after entering two competing team names and selecting a match date.

Main success scenario:
1. The user enters the names of two competing teams and selects the date of their match.
2. The user clicks Predict.
3. The system looks up both teams in the API to retrieve their team IDs.
4. The system retrieves relevant team statistics and historical performance data.
5. The system calculates the predicted probability of each team winning.
6. The system displays both teams' predicted win probabilities to the user.

### Use Case 3 - Predict Next Most Valuable Player (Quinton Gao)
Name: Predict next MVP

Primary actor: User

Preconditions: The application is running and the API key is configured and properly returns data.

Trigger: The user clicks Find MVP after entering the names of two teams.

Main success scenario:

1. The user enters the names of two teams.
2. The user clicks Find MVP.
3. The system looks up both teams in the API to retrieve their team IDs.
4. The system finds the next scheduled game between the two teams. 
5. The system retrieves the per-game player statistics for both teams for the current season.
6. The system calculates the predicted player with the highest points for next game.
7. The system displays the date of the next matchup, the potential MVP’s name, team, and points per game.

(Add additional use cases as needed.)

### Use Case 4 - View Match Statistics (Jiahong Zheng)

Name: View match statistics

Primary actor: User

Preconditions: The application is running, the API key is configured, and completed NBA game data is available.

Trigger: The user selects a completed NBA game from the list of games.

Main success scenario:

1. The user selects an NBA team and a season year.
2. The system retrieves completed games involving the selected team for that season.
3. The system displays a list of completed games.
4. The user selects a game from the list.
5. The system retrieves the selected game's information and team statistics using its game ID.
6. The system displays the final score, quarter-by-quarter scores, and both teams' statistics, including rebounds, assists, steals, blocks, turnovers, and shooting percentages.
7. The user can explore the game statistics through tables and comparison charts.

Alternative scenarios:

- If no completed games are found, the system displays an appropriate message.
- If the selected game's statistics are unavailable, the system informs the user.
- If the API request fails, the system displays an error message instead of invalid data.
---

## Minimal Viable Product (MVP)

| Team Member | MVP Use Case |
|-------------|--------------|
| Osborn Sun | Search Team Season Stats (interactor, controller, presenter, and the Team Stats view with its results table) |
| Shivanshu Rajeev | Predict Match Outcome (interactor, controller, presenter, and the Match Prediction view displaying each team's predicted win probability) |
| Quinton Gao | Predict next MVP (interactor, controller, presenter, and the MVP Prediction view displaying the next match's date, the potential MVP) |
| Jiahong Zheng | View Match Statistics (interactor, controller, presenter, and the Match Statistics view displaying completed games, scores, and team statistics) |

---

## Entities

### Entity 1 – Team
Represents a basketball team participating in a match.

Attributes:
- Team ID
- Team name

### Entity 2 – MatchPrediction
Represents the predicted outcome of a match between two basketball teams.

Attributes:
- Home team
- Away team
- Match date
- Home team win probability
- Away team win probability
Responsibilities:
- Store the two competing teams and their predicted win probabilities.
- Ensure the probabilities are valid (between 0 and 1).
- Represent the prediction results returned by the prediction algorithm.

### Entity 3 - MVP_Prediction
Represents the predicted MVP for the next matchup between two basketball teams.

Attributes:
- Player ID
- Player name
- Average points per game
- Games played

Responsibilities:
- Store a player's scoring average for the current season.
- Ensure the average is not negative and that at least one game has been played
- Store the upcoming game, the chosen MVP, and the top-scoring candidates.


### Entity 4 - Game

Represents a completed NBA game between two basketball teams.

Attributes:
- Game ID
- Home team
- Away team
- Game date
- Game status
- Home team final score
- Away team final score
- Home team quarter-by-quarter scores
- Away team quarter-by-quarter scores

Responsibilities:
- Store information about an NBA game.
- Determine whether the game has been completed.
- Identify the winning team based on the final scores.
- Provide game information and quarter-by-quarter results.

### Entity 5 - GameStats

Represents one team's statistical performance in a specific NBA game.

Attributes:
- Game ID
- Team ID
- Points
- Total rebounds
- Assists
- Steals
- Blocks
- Turnovers
- Field goal percentage
- Three-point percentage
- Free throw percentage

Responsibilities:
- Store a team's statistical performance in a particular game.
- Provide statistical data that can be used by other components for comparison and visualization.
- Represent unavailable statistics appropriately.

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

