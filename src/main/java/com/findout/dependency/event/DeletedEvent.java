package com.findout.dependency.event;

/**
 * Created by magnusdep on 14/03/14.
 */
public class DeletedEvent {
    protected boolean entityFound = true;

    public boolean isEntityFound() {
        return entityFound;
    }

    public void setEntityFound(boolean entityFound) {
        this.entityFound = entityFound;
    }
}
