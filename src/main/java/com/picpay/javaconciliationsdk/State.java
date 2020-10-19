package com.picpay.javaconciliationsdk;

import java.util.EnumSet;
import java.util.Iterator;

public enum State {
    ERROR, NEW, SENDING, SENT;

    public static State selectNextState(State currentState, State nextState) {
        if(nextState == State.ERROR){
            if(currentState == SENT) return currentState;
            else return nextState;
        }
        if(currentState == State.ERROR && nextState == State.SENDING) return nextState;

        Iterator<State> states = EnumSet.of(NEW, SENDING, SENT).iterator();
        State ret = State.SENT;
        boolean found = false;
        while(states.hasNext() && found == false){
            State s = states.next();
            if(s == currentState && states.hasNext()){
                ret = states.next();
                found = true;
            }
            if(ret != nextState){
                throw new IllegalArgumentException(String.format("Error on changing operation state. Expected: %s. Found: %s", ret, nextState));
            }
        }
        return ret;
    }

    public boolean isUnsent() {
        return EnumSet.of(NEW, SENDING, ERROR).contains(this);
    }
}
