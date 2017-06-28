package Entity;

import GameState.EntityObserver;

import java.util.ArrayList;
import java.util.List;

/**
 * Observable class for the observer pattern.
 */
public abstract class ObservableEntity
{
    List<EntityObserver> observers = new ArrayList<>();

    public void addObserver(EntityObserver o) {
        observers.add(o);
    }

    public void removeObserver(EntityObserver o) {
        observers.remove(o);
    }

    public void notifyObservers() {
        for (EntityObserver o : observers) {
            o.reactToChange(this);
        }
    }
}
