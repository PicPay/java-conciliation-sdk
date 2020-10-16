package com.picpay.javaconciliationsdk;

import java.math.BigDecimal;
import java.util.UUID;

public class Operation {

    private UUID correlationId;
    private BigDecimal valor;
    private State currentState;

    public Operation(UUID correlationId, BigDecimal valor){
        this.correlationId = correlationId;
        this.valor = valor;
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
}
