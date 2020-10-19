package com.picpay.javaconciliationsdk;

import java.math.*;
import java.time.*;
import java.util.*;

public class Operation implements Comparable<Operation> {

    private UUID correlationId;
    private BigDecimal valor;
    private State currentState;
    private Instant date;

    public Operation(UUID correlationId, BigDecimal valor, Instant date){
        this.correlationId = correlationId;
        this.valor = valor;
        this.date = date;
        this.currentState = State.NEW;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void markAsSending() {
        this.currentState = State.selectNextState(currentState, State.SENDING);
    }

    public void markAsSent() {
        this.currentState = State.selectNextState(currentState, State.SENT);
    }

    public void markErrorSending(){
        this.currentState = State.selectNextState(currentState, State.ERROR);
    }

    public UUID getCID() {
        return correlationId;
    }

    public boolean isUnsent() {
        return currentState.isUnsent();
    }

    @Override
    public int compareTo(Operation o) {
        return date.compareTo(o.date);
    }
}
