package model.observerPattern;

import model.EnemyComposite;

public interface Subject {

	void addEnemyListener(Observer o);
	void notifyObservers(EnemyComposite.Event event);
	
}
