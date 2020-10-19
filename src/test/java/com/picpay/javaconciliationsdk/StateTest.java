package com.picpay.javaconciliationsdk;

import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

public class StateTest {

    @Test
    public void selectTheNextState(){
        assertThat(State.selectNextState(State.NEW, State.SENDING), is(State.SENDING));
        assertThat(State.selectNextState(State.SENDING, State.SENT), is(State.SENT));
        assertThat(State.selectNextState(State.SENT, State.SENT), is(State.SENT));
        assertThat(State.selectNextState(State.ERROR, State.SENDING), is(State.SENDING));
    }

    @Test
    public void throwsExceptionIfErrorChangingState(){
        try {
            State.selectNextState(State.NEW, State.SENT);
            fail("Should be throws Exception");
        }catch(IllegalArgumentException e){
            assertThat(e.getMessage(), containsString(State.SENDING.name()));
            assertThat(e.getMessage(), containsString(State.SENT.name()));
        }
    }

    @Test
    public void selectErrorState(){
        assertThat(State.selectNextState(State.NEW, State.ERROR), is(State.ERROR));
        assertThat(State.selectNextState(State.SENDING, State.ERROR), is(State.ERROR));
        assertThat(State.selectNextState(State.SENT, State.ERROR), is(State.SENT));
        assertThat(State.selectNextState(State.ERROR, State.ERROR), is(State.ERROR));
    }

    @Test
    public void stateIsUnsentOrNot(){
        assertThat(State.NEW.isUnsent(), is(true));
        assertThat(State.SENDING.isUnsent(), is(true));
        assertThat(State.SENT.isUnsent(), is(false));
        assertThat(State.ERROR.isUnsent(), is(true));
    }
}
