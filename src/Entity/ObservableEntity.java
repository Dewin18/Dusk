package Entity;

import GameState.EntityObserver;

import java.util.ArrayList;
import java.util.List;

/** PTP 2017
 * Observable class for the observer pattern.
 *
 * @author Ali Popa
 * @version 15.08.
 * @since 23.06.
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

    public void notifyObservers(MapObject mapObject) {
        for (EntityObserver o : observers) {
            o.reactToChange(mapObject);
        }
    }
}
