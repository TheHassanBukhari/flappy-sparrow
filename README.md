# Flappy Sparrow

Flappy Sparrow is a simple Java Swing–based parody game inspired by the classic *Flappy Bird*.
This project was created as a learning exercise to practice Java GUI programming, event handling, graphics rendering, and audio playback.

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

```
Flappy_Sparrow/
├── graphics/
│   ├── flappysparrowbg.png
│   ├── flappysparrow.png
│   ├── toppipe.png
│   ├── bottompipe.png
│   ├── GameOver1.png
│   └── GameOver2.png
├── audios/
│   ├── bgAudio.wav
│   └── gameOver.wav
├── src/
│   └── App.java
├── bin/
└── run.txt
```

---

## How to Run

Make sure you are in the project root directory.

Using the provided script:

```
bash run.txt
```

Or manually:

```
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

## ⚠️ Disclaimer

This is a **parody project** inspired by *Flappy Bird* and created purely for fun and educational purposes.
No commercial use is intended.
