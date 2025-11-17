# Thirteen Stones 🪨

This is a strategic logic game for two players. The rules are deceptive: you take turns removing 1-3 stones from a pile of 13. The player stuck with the last stone loses. It sounds like luck, but there is a mathematical strategy to win every time.

## 🎮 Features
* **Visual Feedback:** The pile of stones updates dynamically on the screen as players take turns.
* **Customizable Rules:** I added a Settings menu where you can flip the win condition (e.g., "Winner is the last player to pick"), which completely changes the strategy.
* **Persistence:** You can close the app in the middle of a game, and when you reopen it, the board state (whose turn it is, how many stones are left) is restored exactly as you left it.
* **Statistics:** Tracks wins per player and total games played.

## 🛠️ Technical Details
* **State Management:** I used **JSON serialization (Gson)** to save the complex game object to `SharedPreferences`.
* **Logic Separation:** The game rules are isolated in a dedicated Model class, keeping the Activity code clean.
* **Tech:** Java, Android Jetpack, Gson.

## 📸 Gameplay
<img width="365" height="754" alt="Screenshot 2025-07-31 210251" src="https://github.com/user-attachments/assets/03c43c60-1950-48ba-91ad-405a63c8ab1b" />
<img width="281" height="539" alt="Screenshot 2025-07-31 215711" src="https://github.com/user-attachments/assets/7766247a-c4c5-4683-9451-ece34f9d0437" />
<img width="356" height="754" alt="Screenshot 2025-07-31 203900" src="https://github.com/user-attachments/assets/d800f9d8-94ba-4b94-802a-c36386565e13" />
