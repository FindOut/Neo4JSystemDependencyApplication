package com.findout.dependency.event.resource;

import com.findout.dependency.event.ReadEvent;
import com.findout.dependency.event.RequestReadEvent;

/**
 * Created by magnusdep on 11/03/14.
 */
public class FindResourceByNameEvent implements RequestReadEvent {
    private String name;

    public FindResourceByNameEvent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
