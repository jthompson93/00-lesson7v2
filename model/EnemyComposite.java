package model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;

import model.observerPattern.Subject;
import model.observerPattern.Observer;
import model.observerPattern.EnemyObserver;
import view.GameBoard;

public class EnemyComposite extends GameElement implements Subject {

	public static final int NROWS = 2;
	public static final int NCOLS = 10;
	public static final int ENEMY_SIZE = 20;
	public int UNIT_MOVE = 5;

	private ArrayList<ArrayList<GameElement>> rows;
	private ArrayList<Observer> observers = new ArrayList<>();
	private ArrayList<GameElement> bombs;
	private boolean movingToRight = true;
	private boolean movingDown = false;
	private Random random = new Random();
	private int count = 0;
	private int livesLeft = 4;
	private int enemiesHit = 0;

	public enum Event {
		ShotEnemy, MeetEnemy, Dead, ShotBomb
	}

	public EnemyComposite() {
		rows = new ArrayList<>();
		bombs = new ArrayList<>();

		for(int r = 0; r < NROWS; r++) {
			
			var oneRow = new ArrayList<GameElement>();
			rows.add(oneRow);

			for(int c = 0; c < NCOLS; c++) {
				oneRow.add(new Enemys(
					c * ENEMY_SIZE * 2, r * ENEMY_SIZE * 2, ENEMY_SIZE, Color.yellow, true
					));
			}
		}
	}

	@Override
	//render enemy array
	public void render(Graphics2D g2) {
		for(var r: rows) {
			for(var e: r) {
				e.render(g2);
			}
		}

		//render bombs
		for(var b: bombs) {
			b.render(g2);
		}
		
	}
	
	@Override
	public void animate() {
		int dx = UNIT_MOVE;
		int dy = ENEMY_SIZE;
		if(movingToRight) {
			movingDown = false;
			if(rightEnd() >= GameBoard.WIDTH) {
				dx = -dx;
				movingDown = true;
				count += 1;
				movingToRight = false;
				
			}
		} else {
			dx = -dx;
			movingDown = false;
			if(leftEnd() <= 0) {
				dx = -dx;
				movingDown = true;
				count += 1;
				movingToRight = true;
			}
		}

		for(var row: rows) {

			for(var e: row) {
				e.x += dx;
				if(movingDown) {
					e.y += dy;
				}
				if(count >= 12) {
					notifyObservers(Event.MeetEnemy);
				
				}
			}
		}
		
		for (var b: bombs) {
			b.animate();
		}
	}

	private int rightEnd() {
		int xEnd = -100;

		for(var row: rows) {
			if(row.size() == 0) continue;
			int x = row.get(row.size() - 1).x + ENEMY_SIZE;
			if(x > xEnd) xEnd = x;
		}
		return xEnd;
	}

	private int leftEnd() {
		int xEnd = 9000;

		for(var row: rows) {
			if(row.size() == 0) continue;
			int x = row.get(0).x;
			if(x < xEnd) xEnd = x;
		}
		return xEnd;
	}

	public void dropBombs() {
		for(var row: rows) {
			for(var e: row) {
				if(random.nextFloat() < 0.1F) {
					bombs.add(new Bomb(e.x, e.y));
				}
			}
		}
	}

	public void removeBombsOutOfBound() {
		var remove = new ArrayList<GameElement>();
		for(var b: bombs) {
			if(b.y >= GameBoard.HEIGHT) {
				remove.add(b);
			}
		}
		bombs.removeAll(remove);
	}


	public void processCollision(Shooter shooter) {
		var removeBullets = new ArrayList<GameElement>();
		for(var row: rows) {
			var removeEnemies = new ArrayList<GameElement>();
			for(var enemy: row) {
				
				for(var bullet: shooter.getWeapons()) {
					if(enemy.collideWith(bullet)) {
						enemiesHit += 1;
						notifyObservers(Event.ShotEnemy);
						removeBullets.add(bullet);
						removeEnemies.add(enemy);
						if(enemiesHit % 5 == 0) {
							shooter.MAX_BULLETS += 1;
						} else if(enemiesHit % 2 == 0) {
							UNIT_MOVE += 1;	
						}
						
						System.out.println(shooter.MAX_BULLETS);
		
						
						System.out.println("Enemy remaining: " + row.size());
					}
				}
			}
			row.removeAll(removeEnemies);
		}

		shooter.getWeapons().removeAll(removeBullets);

		var removeBombs = new ArrayList<GameElement>();
		var removeShooter = new ArrayList<GameElement>();
		removeBullets.clear();

		for (var b: bombs) {
			for(var bullet: shooter.getWeapons()) {
				if(b.collideWith(bullet)) {
					removeBombs.add(b);
					removeBullets.add(bullet);
					notifyObservers(Event.ShotBomb);
				}
			}
			for(var c: shooter.getComponents()) {
				if(b.collideWith(c)) {
					removeShooter.add(c);
					removeBombs.add(b);
					livesLeft -= 1;
					if(livesLeft == 0) {
						notifyObservers(Event.Dead);
					}
				}
			}
		}

		shooter.getWeapons().removeAll(removeBullets);
		bombs.removeAll(removeBombs);
		shooter.getComponents().removeAll(removeShooter);
		
		

	}

	@Override
	public void addEnemyListener(Observer o) {
		observers.add(o);
	}
	@Override
	public void notifyObservers(Event event) {
		switch(event) {
			case ShotEnemy:
			for(var o: observers) {
				o.shotEnemy();
			}
			break;
			case MeetEnemy:
			for(var o: observers) {
				o.enemyReachedEnd();
			}
			break;
			case Dead:
			for(var o: observers) {
				o.allDead();
			}
			break;
			case ShotBomb:
			for(var o: observers) {
				o.shotBomb();
			}
			break;


	}
}
	
}
