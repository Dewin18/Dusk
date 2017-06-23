package GameState;

import Entity.ObservableEntity;

public interface EntityObserver
{
    void reactToChange(ObservableEntity o);
}
