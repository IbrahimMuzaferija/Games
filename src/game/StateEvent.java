package game;

import java.util.*;

public class StateEvent extends EventObject {

    public StateEvent(GameState source) {
        super(source);
    }
}
