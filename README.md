# Flappy Sparrow

Flappy Sparrow is a simple Java Swing based parody game inspired by the classic *Flappy Bird*, built to get hands on with Java GUI programming, event handling, graphics rendering, and audio playback.

**Project Type:** Personal Project (Learning)

**Portfolio:** [hassanbukhari.is-a.dev](https://hassanbukhari.is-a.dev/) <br>
**LinkedIn:** [Syed Hassan Ali Bukhari](https://www.linkedin.com/in/syedhassanalibukhari/)

## Gameplay

- Press **SPACE** to make the sparrow flap
- Avoid the pipes
- Score increases as you pass obstacles
- Includes background music and game-over sound
- Press **SPACE** to restart after game over

## Technologies Used

- Java (Core Java)
- Java Swing (GUI)
- Java AWT (Graphics & Events)
- Java Sound API

## Project Structure

```text
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

## How to Run

Make sure you are in the project root directory.

Using the provided script:

```bash
bash run.txt
```

Or manually:

```bash
javac -d bin src/App.java
java -cp bin App
```

## Learning Outcomes

- Java Swing GUI development
- Game loops using `Timer`
- Keyboard input handling
- Collision detection
- Image and audio loading
- Basic Java project structuring

## Disclaimer

This is a parody project inspired by *Flappy Bird* and created purely for fun and educational purposes. No commercial use is intended.

## Author

[Syed Hassan Ali Bukhari](https://hassanbukhari.is-a.dev/)

## License

This project is licensed under the [MIT License](./LICENSE).
