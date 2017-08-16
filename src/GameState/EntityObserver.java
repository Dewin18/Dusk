package GameState;

import Entity.ObservableEntity;

/** PTP 2017
 * Observer interface for the observer pattern.
 *
 * @author Ali Popa
 * @version 28.06.
 * @since 23.06.
 */
public interface EntityObserver
{
    void reactToChange(ObservableEntity o);
}
