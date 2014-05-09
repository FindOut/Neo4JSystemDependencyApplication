package com.findout.dependency.event.resource;

import com.findout.dependency.event.ReadEvent;

import java.util.Collection;
import java.util.Collections;

/**
 * Created by magnusdep on 12/03/14.
 */
public class AllResourcesEvent  extends ReadEvent {
    private final Collection<ResourceDetails> resourceDetails;

    public AllResourcesEvent(Collection<ResourceDetails> resourceDetails) {
        this.resourceDetails = Collections.unmodifiableCollection(resourceDetails);
    }

    public Collection<ResourceDetails> getResourceDetails() {
        return resourceDetails;
    }
}
