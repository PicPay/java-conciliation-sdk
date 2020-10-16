package com.picpay.javaconciliationsdk;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class OperationTest {

    private Operation operation;

    @BeforeEach
    public void setUp(){
        operation = new Operation(UUID.randomUUID(), BigDecimal.TEN);
    }

    @Test
    public void newOperation(){
        assertThat(operation.getCurrentState(), is(equalTo(State.NEW)));
    }

    @Test
    public void markAsSending(){
        operation.markAsSending();

        assertThat(operation.getCurrentState(), is(equalTo(State.SENDING)));
    }

    @Test
    public void markAsSent(){
        operation.markAsSending();
        operation.markAsSent();

        assertThat(operation.getCurrentState(), is(equalTo(State.SENT)));
    }

    @Test
    public void markAsError(){
        operation.markAsSending();
        operation.markErrorSending();

        assertThat(operation.getCurrentState(), is(equalTo(State.ERROR)));
    }
}
