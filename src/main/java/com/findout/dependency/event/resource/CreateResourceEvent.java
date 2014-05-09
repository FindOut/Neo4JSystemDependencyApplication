package com.findout.dependency.event.resource;

import com.findout.dependency.event.CreateEvent;

/**
 * Created by magnusdep on 05/03/14.
 */
public class CreateResourceEvent implements CreateEvent {
    private ResourceDetails details;

    public CreateResourceEvent(ResourceDetails details) {
        this.details = details;
    }

    public ResourceDetails getDetails() {
        return details;
    }
}
