package view;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import controller.KeyController;
import controller.TimerListener;
import model.EnemyComposite;
import model.Shooter;
import model.ShooterElement;

import java.awt.Container;
import java.awt.Color;
import java.awt.BorderLayout;

import model.observerPattern.EnemyObserver;

public class GameBoard {

	public static final int WIDTH = 600;
	public static final int HEIGHT = 300;

	public static final int FPS = 20;
	public static final int DELAY = 1000 / FPS;
	
	private JFrame window;
	private MyCanvas canvas;
	private Shooter shooter;
	private EnemyComposite enemyComposite;
	private Timer timer;
	private TimerListener timerListener;
	private int score = 0;
	private boolean gameOver;
	private int bombScore = 0;

	private JLabel scoreDisplay = new JLabel();

	public GameBoard(JFrame window) {
		this.window = window;
	}

	public void init() {
		Container cp = window.getContentPane();

		canvas = new MyCanvas(this, WIDTH, HEIGHT);
		cp.add(BorderLayout.CENTER, canvas);

		canvas.addKeyListener(new KeyController(this));
		canvas.requestFocusInWindow();
		canvas.setFocusable(true);

		JPanel northPanel = new JPanel();
		JLabel label = new JLabel("Score: ");
		northPanel.add(label);
		scoreDisplay.setText("" + (score + bombScore));
		northPanel.add(scoreDisplay);
		cp.add(BorderLayout.NORTH, northPanel);

		JButton startButton = new JButton("Start");
		JButton quitButton = new JButton("Quit");
		startButton.setFocusable(false);
		quitButton.setFocusable(false);

		JPanel southPanel = new JPanel();
		southPanel.add(startButton);
		southPanel.add(quitButton);
		cp.add(BorderLayout.SOUTH, southPanel);

		canvas.getGameElements().add(new TextDraw("Click <Start> to Play", 100, 100, Color.yellow, 30));


		timerListener = new TimerListener(this);
		timer = new Timer(DELAY, timerListener);

		startButton.addActionListener(event -> {
			shooter = new Shooter(GameBoard.WIDTH / 2, GameBoard.HEIGHT - ShooterElement.SIZE);
			EnemyObserver observer = new EnemyObserver(this);
			enemyComposite = new EnemyComposite();
			enemyComposite.addEnemyListener(observer);
			canvas.getGameElements().clear();
			canvas.getGameElements().add(shooter);
			canvas.getGameElements().add(enemyComposite);
			score = 0;
			bombScore = 0;
			timer.start();
		});

		quitButton.addActionListener(event -> System.exit(0));

	}

	public MyCanvas getCanvas() {
		return canvas;
	}

	public Timer getTimer() {
		return timer;
	}

	public TimerListener getTimerListener() {
		return timerListener;
	}
	public Shooter getShooter() {
		return shooter;
	}
	public EnemyComposite getEnemyComposite() {
		return enemyComposite;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public JLabel getScoreDisplay() {
		return scoreDisplay;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public int getBombScore() {
		return bombScore;
	}

	public void setBombScore(int bombScore) {
		this.bombScore = bombScore;
	}
}
