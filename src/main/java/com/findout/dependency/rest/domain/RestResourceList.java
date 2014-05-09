package com.findout.dependency.rest.domain;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Used to handle XML generating behaviour in regards to lists. Wrapping list in root object makes it work both for
 * JSON and XML generation.
 */
@XmlRootElement
public class RestResourceList implements Serializable {
    private List<RestResource> data = new ArrayList<>();

    public RestResourceList() {
    }

    public RestResourceList(List<RestResource> data) {
        this.data = data;
    }

    public List<RestResource> getData() {
        return data;
    }

    public void setData(List<RestResource> data) {
        this.data = data;
    }
}
