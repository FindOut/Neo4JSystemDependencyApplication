package com.findout.dependency.event.resource;

import com.findout.dependency.event.RequestReadEvent;

/**
 * Created by magnusdep on 10/03/14.
 */
public class RequestResourceDetailsEvent<T> implements RequestReadEvent {
    private T id;

    public RequestResourceDetailsEvent(T id) {
        this.id = id;
    }

    public T getId() {
        return id;
    }
}
