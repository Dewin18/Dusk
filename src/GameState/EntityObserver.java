package GameState;

import Entity.ObservableEntity;

/**
 * Observer interface for the observer pattern.
 */
public interface EntityObserver
{
    void reactToChange(ObservableEntity o);
}
