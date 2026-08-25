# Flappy Sparrow
Flappy Sparrow is a simple Java Swing–based parody game inspired by the classic *Flappy Bird*.
This project was created as a learning exercise to practice Java GUI programming, event handling, graphics rendering, and audio playback.

**Portfolio:** [hassanbukhari.is-a.dev](https://hassanbukhari.is-a.dev/)

---
## Gameplay
* Press **SPACE** to make the sparrow flap
* Avoid the pipes
* Score increases as you pass obstacles
* Includes background music and game-over sound
* Press **SPACE** to restart after game over
---
## Technologies Used
* Java (Core Java)
* Java Swing (GUI)
* Java AWT (Graphics & Events)
* Java Sound API
---
## Project Structure
```text
Flappy_Sparrow/<br>
├── graphics/<br>
│ ├── flappysparrowbg.png<br>
│ ├── flappysparrow.png<br>
│ ├── toppipe.png<br>
│ ├── bottompipe.png<br>
│ ├── GameOver1.png<br>
│ └── GameOver2.png<br>
├── audios/<br>
│ ├── bgAudio.wav<br>
│ └── gameOver.wav<br>
├── src/<br>
│ └── App.java<br>
├── bin/<br>
└── run.txt<br>
```
---
## How to Run
Make sure you are in the project root directory.
Using the provided script:
```text
bash run.txt
```
Or manually:
```text
javac -d bin src/App.java
java -cp bin App
```

---
## Purpose of the Project
This project was built for **learning purposes only**.
Through this project, I practiced:
* Java Swing GUI development
* Game loops using `Timer`
* Keyboard input handling
* Collision detection
* Image and audio loading
* Basic Java project structuring
---
## Disclaimer
This is a **parody project** inspired by *Flappy Bird* and created purely for fun and educational purposes.
No commercial use is intended.

---
## Author
[Syed Hassan Ali Bukhari](https://hassanbukhari.is-a.dev/)
