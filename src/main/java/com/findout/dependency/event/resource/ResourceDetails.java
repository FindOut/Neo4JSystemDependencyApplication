package com.findout.dependency.event.resource;

import com.findout.dependency.domain.DependencyType;
import com.findout.dependency.domain.Resource;
import org.springframework.beans.BeanUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by magnusdep on 05/03/14.
 */
public class ResourceDetails {
    private Long id;
    private String name;
    private String description;

    private Map<Long, DependencyType> dependencies = new LinkedHashMap<>();

    public ResourceDetails() {
        id = null;
    }

    public ResourceDetails(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void addDependency(Long id, DependencyType type) {
        dependencies.put(id, type);
    }

    public Map<Long, DependencyType> getDependencies() {
        return dependencies;
    }

    public Resource toResource(){
        Resource resource = new Resource();
        BeanUtils.copyProperties(this, resource);

        return resource;
    }
}
