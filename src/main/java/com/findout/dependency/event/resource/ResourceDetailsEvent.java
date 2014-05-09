package com.findout.dependency.event.resource;

import com.findout.dependency.event.ReadEvent;

/**
 * Created by magnusdep on 10/03/14.
 */
public class ResourceDetailsEvent<T> extends ReadEvent {
    private T id;
    private ResourceDetails resourceDetails;

    public ResourceDetailsEvent(T id) {
        this.id = id;
    }

    public ResourceDetailsEvent(T id, ResourceDetails resourceDetails) {
        this.id = id;
        this.resourceDetails = resourceDetails;
    }

    public T getId() {
        return id;
    }

    public ResourceDetails getResourceDetails() {
        return resourceDetails;
    }

    public static <T> ResourceDetailsEvent notFound(T id) {
        ResourceDetailsEvent ev = new ResourceDetailsEvent(id);
        ev.entityFound = false;
        return ev;
    }
}
