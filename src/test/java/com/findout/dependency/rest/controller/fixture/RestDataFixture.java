package com.findout.dependency.rest.controller.fixture;

import com.findout.dependency.domain.DependencyType;
import com.findout.dependency.event.resource.ResourceDetails;

/**
 * Created by magnusdep on 05/03/14.
 */
public class RestDataFixture {
    public static final String NAME_1 = "SystemZZZ";
    public static final String DESCRIPTION_1 = "Description of the system";
    public static final Long ID_1 = 666l;
    public static final Long ID_2 = 777l;

    public static ResourceDetails customResourceDetails(String name) {
        ResourceDetails details = new ResourceDetails(ID_1);
        details.setName(name);
        details.setDescription(DESCRIPTION_1);
        return details;
    }

    public static ResourceDetails customResourceDetails(Long id) {
        ResourceDetails details = new ResourceDetails(id);
        details.setName(NAME_1);
        details.setDescription(DESCRIPTION_1);
        return details;
    }

    public static ResourceDetails customResourceDetails(Long id, Long dependsOnId, DependencyType type) {
        ResourceDetails details = new ResourceDetails(id);
        details.setName(NAME_1);
        details.setDescription(DESCRIPTION_1);
        details.addDependency(dependsOnId, type);
        return details;
    }

    public static ResourceDetails standardResourceDetails() {
        ResourceDetails details = new ResourceDetails(ID_1);
        details.setName(NAME_1);
        details.setDescription(DESCRIPTION_1);
        return details;
    }

    public static String newResourceJSON() {
        return "{ \"name\": \""+NAME_1+"\", \"description\":\""+DESCRIPTION_1+"\"}";
    }

    public static String standardResourceJSON() {
        return "{ \"resourceId\": \""+ID_1+"\", \"name\": \""+NAME_1+"\", \"description\":\""+DESCRIPTION_1+"\"}";
    }

    public static String customResourceJSON(ResourceDetails details) {
        return "{ \"resourceId\": \""+details.getId()+"\", \"name\": \""+details.getName()+"\", \"description\":\""+details.getDescription()+"\"}";
    }
}
