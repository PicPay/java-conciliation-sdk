package com.picpay.javaconciliationsdk;

import org.junit.jupiter.api.*;

import java.math.*;
import java.time.*;
import java.util.*;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

public class OperationTest {

    private Operation operation;

    @BeforeEach
    public void setUp(){
        operation = new Operation(UUID.randomUUID(), BigDecimal.TEN, Instant.now());
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

    
    @Test
    public void markAsSendingAfterError(){
        operation.markAsSending();
        operation.markErrorSending();
        operation.markAsSending();

        assertThat(operation.getCurrentState(), is(equalTo(State.SENDING)));
    }
}
