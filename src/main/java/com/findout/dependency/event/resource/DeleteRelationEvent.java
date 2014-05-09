package com.findout.dependency.event.resource;

import com.findout.dependency.domain.DependencyType;
import com.findout.dependency.event.DeleteEvent;

/**
 * Created by magnusdep on 21/03/14.
 */
public class DeleteRelationEvent implements DeleteEvent {
    private final Long resourceId;
    private final Long dependsOnId;
    private final DependencyType type;

    public DeleteRelationEvent(Long resourceId, Long dependsOnId, DependencyType type) {
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
