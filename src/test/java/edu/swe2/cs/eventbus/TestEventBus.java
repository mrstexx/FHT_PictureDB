package edu.swe2.cs.eventbus;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestEventBus {

    private EventBusSubscriberTest eventBusSubscriberTest = new EventBusSubscriberTest();

    @Before
    public void registerEvents() {
        EventBusFactory.createSharedEventBus().register(eventBusSubscriberTest);
    }

    @Test
    public void testFireAndHandleEventBus() {
        EventBusFactory.createSharedEventBus().fire(new OnEventBusTestA());
        Assert.assertTrue(eventBusSubscriberTest.isValue());

        EventBusFactory.createSharedEventBus().fire(new OnEventBusTestB());
        Assert.assertFalse(eventBusSubscriberTest.isValue());
    }

    @Test
    public void testEventBusSubscriber() {
        // two times initialized
        Assert.assertEquals(2, EventBusFactory.createSharedEventBus().getSubscribers().size());
    }

}
