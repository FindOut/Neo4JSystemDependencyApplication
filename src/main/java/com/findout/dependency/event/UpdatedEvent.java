package com.findout.dependency.event;

/**
 * Created by magnusdep on 17/03/14.
 */
public class UpdatedEvent {
    protected boolean entityFound = true;

    public boolean isEntityFound() {
        return entityFound;
    }

    public void setEntityFound(boolean entityFound) {
        this.entityFound = entityFound;
    }
}
