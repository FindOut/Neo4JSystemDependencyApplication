package com.findout.dependency.event.resource;

import com.findout.dependency.event.UpdatedEvent;

/**
 * Created by magnusdep on 17/03/14.
 */
public class ResourceUpdatedEvent extends UpdatedEvent {
    private final Long id;
    private ResourceDetails details;

    public ResourceUpdatedEvent(Long id) {
        this.id = id;
    }

    public ResourceUpdatedEvent(Long id, ResourceDetails details) {
        this.id = id;
        this.details = details;
    }

    public Long getId() {
        return id;
    }

    public ResourceDetails getDetails() {
        return details;
    }

    public static ResourceUpdatedEvent notFound(Long id){
        ResourceUpdatedEvent event = new ResourceUpdatedEvent(id);
        event.setEntityFound(false);
        return event;
    }
}
