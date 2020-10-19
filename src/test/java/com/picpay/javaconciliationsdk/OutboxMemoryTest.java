package com.picpay.javaconciliationsdk;

import org.junit.jupiter.api.*;

import java.math.*;
import java.time.*;
import java.util.*;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.core.Is.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class OutboxMemoryTest {

    private Outbox outbox;
    private Operation operation;
    private Operation anotherOperation;
    private SortedSet<Operation> operations;
    private Conciliation conciliation;
    private static final int maxOperationsToPoll = 2;

    @BeforeEach
    public void setUp(){
        operations = new TreeSet<>();
        conciliation = mock(Conciliation.class);
        outbox = new OutboxMemory(operations, this.conciliation, maxOperationsToPoll);
        operation = new Operation(UUID.randomUUID(), BigDecimal.TEN, Instant.now().minusSeconds(2));
        anotherOperation = new Operation(UUID.randomUUID(), BigDecimal.ONE, Instant.now().minusSeconds(1));
    }

    @Test
    public void sendAnOperation() {
        when(conciliation.send(operation)).thenReturn(true);
        outbox.send(operation);

        assertThat(operations.contains(operation), is(true));
        assertThat(operation.getCurrentState(), is(State.SENT));
    }

    @Test
    public void errorOnSendingAnOperation(){
        when(conciliation.send(operation)).thenReturn(false);
        outbox.send(operation);

        assertThat(operations.contains(operation), is(true));
        assertThat(operation.getCurrentState(), is(State.ERROR));
    }

    @Test
    public void whenOutboxIsEmptyItDoesNotSendUnsentOperations(){
        outbox.sendUnsentOperations();
        
        verify(conciliation, never()).send(any(Operation.class));
    }

    @Test
    public void whenOutboxContainsOnlySentOperationsItDoesNotSendAnyOperation(){
        operation.markAsSending();
        operation.markAsSent();
        operations.add(operation);

        outbox.sendUnsentOperations();

        verify(conciliation, never()).send(any(Operation.class));
    }

    @Test
    public void sendNewOperation(){
        operations.add(operation);
        when(conciliation.send(operation)).thenReturn(true);
        outbox.sendUnsentOperations();

        verify(conciliation).send(operation);
    }

    @Test
    public void sendOperationWithErrorWhenSendPreviously(){
        operation.markErrorSending();
        operations.add(operation);
        when(conciliation.send(operation)).thenReturn(true);

        outbox.sendUnsentOperations();

        verify(conciliation).send(operation);
    }

    @Test
    public void getANumberOfOperationsEqualsToTheMaximum(){
        operations.add(operation);
        operations.add(anotherOperation);

        outbox.sendUnsentOperations();

        verify(conciliation).send(operation);
        verify(conciliation).send(anotherOperation);
    }

    @Test
    public void getANumberOfOperationsAboveMaximum_sendOldestOperations(){
        Operation thirdOperation = new Operation(UUID.randomUUID(), BigDecimal.ONE.add(BigDecimal.TEN), Instant.now());

        operations.add(operation);
        operations.add(anotherOperation);
        operations.add(thirdOperation);

        outbox.sendUnsentOperations();

        verify(conciliation).send(operation);
        verify(conciliation).send(anotherOperation);
        verify(conciliation, never()).send(thirdOperation);
    }
}
