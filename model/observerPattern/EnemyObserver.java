package model.observerPattern;

import java.awt.Color;

import view.GameBoard;
import view.TextDraw;

public class EnemyObserver implements Observer {

	private GameBoard gameBoard;

	public EnemyObserver(GameBoard gameBoard) {
		this.gameBoard = gameBoard;
	}

	@Override
	public void shotEnemy() {
		int score = gameBoard.getScore();
		score += 10;
		int bombScore = gameBoard.getBombScore();
		System.out.println("score " + score + "bombsore " + bombScore);
		gameBoard.setScore(score);
		gameBoard.getScoreDisplay().setText("" + score);
		if(score - bombScore == 200) {
		gameBoard.getCanvas().getGameElements().add(new TextDraw("Game Over - You Beat the Game | Score: " + score, 60, 150, Color.green, 17));
		gameBoard.setGameOver(true);
		gameBoard.getCanvas().repaint();
		}
	}

	@Override
	public void enemyReachedEnd() {
		int score = gameBoard.getScore();
		gameBoard.getCanvas().getGameElements().add(new TextDraw("Game Over | Enemy Reached the End | Score: " + score, 60, 150, Color.red, 17));
		gameBoard.setGameOver(true);
		gameBoard.getCanvas().repaint();
	}

	@Override
	public void allDead() {
		int score = gameBoard.getScore();
		
		gameBoard.getCanvas().getGameElements().add(new TextDraw("Game Over | You Were Killed | Score: " + score, 60, 150, Color.red, 17));
		gameBoard.setGameOver(true);
		gameBoard.getCanvas().repaint();

	}

	@Override
	public void shotBomb() {
		int score = gameBoard.getScore();
		score += 3;
		int bombScore = gameBoard.getBombScore();
		bombScore += 3;
		System.out.println("score " + score + "bombsore " + bombScore);
		gameBoard.setScore(score);
		gameBoard.setBombScore(bombScore);
		gameBoard.getScoreDisplay().setText("" + score);
		if(score - bombScore == 200) {
		gameBoard.getCanvas().getGameElements().add(new TextDraw("Game Over - You Beat the Game | Score: " +  score, 60, 150, Color.green, 17));
		gameBoard.setGameOver(true);
		gameBoard.getCanvas().repaint();
		}

	}
	
}
