package com.findout.dependency.event.resource;

import com.findout.dependency.domain.DependencyType;
import com.findout.dependency.event.UpdateEvent;

/**
 * Created by magnusdep on 18/03/14.
 */
public class ConnectResourcesEvent implements UpdateEvent {
    private final Long resourceId;
    private final Long dependsOnId;
    private final DependencyType type;

    public ConnectResourcesEvent(Long resourceId, Long dependsOnId, DependencyType type) {
        this.resourceId = resourceId;
        this.dependsOnId = dependsOnId;
        this.type = type;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public Long getDependsOnId() {
        return dependsOnId;
    }

    public DependencyType getType() {
        return type;
    }
}
