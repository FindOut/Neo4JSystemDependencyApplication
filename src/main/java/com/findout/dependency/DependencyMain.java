package com.findout.dependency;

import com.findout.dependency.config.MVCConfig;
import com.findout.dependency.config.Neo4jTestConfig;
import com.findout.dependency.domain.DependencyType;
import com.findout.dependency.domain.Resource;
import com.findout.dependency.service.ResourceService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by magnusdep on 27/02/14.
 */
public class DependencyMain {
    public static void main(String... args){

        final AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(MVCConfig.class);
        ctx.registerShutdownHook();
        //addShutdownHook(ctx);

        ResourceService resourceService = ctx.getBean(ResourceService.class);
        Resource a = resourceService.addResource("SystemA", "Describing SystemA");
        Resource b = resourceService.addResource("SystemB", "Describing SystemB");
        Resource c = resourceService.addResource("SystemC", "Describing SystemC");
        Resource d = resourceService.addResource("SystemD", "Describing SystemD");
        Resource e = resourceService.addResource("SystemE", "Describing SystemE");
        Resource f = resourceService.addResource("SystemF", "Describing SystemF");
        Resource g = resourceService.addResource("SystemG", "Describing SystemG");
        Resource h = resourceService.addResource("SystemH", "Describing SystemH");
        Resource x = resourceService.addResource("SystemX", "Describing SystemX");
        resourceService.connectResources(a,b, DependencyType.FILE);
        resourceService.connectResources(a,c, DependencyType.DATABASE);
        resourceService.connectResources(b,c, DependencyType.SERVICE);
        resourceService.connectResources(d,a, DependencyType.SERVICE);
        resourceService.connectResources(d,f, DependencyType.DATABASE);
        resourceService.connectResources(e,d, DependencyType.FILE);
        resourceService.connectResources(g,b, DependencyType.SERVICE);
        resourceService.connectResources(h,g, DependencyType.FILE);
        resourceService.connectResources(h,x, DependencyType.SERVICE);
    }

    private static void addShutdownHook(final ClassPathXmlApplicationContext ctx) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                ctx.close();
            }
        });
    }
}
