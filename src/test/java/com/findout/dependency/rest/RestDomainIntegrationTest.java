package com.findout.dependency.rest;

import com.findout.dependency.config.MVCConfig;
import com.findout.dependency.config.Neo4JDevConfig;
import com.findout.dependency.config.Neo4jTestConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.http.MediaType;
import org.springframework.mock.env.MockPropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Using pure Spring MVC testing since we're testing the spring configuration integration.
 * To set active profile for system: System.setProperty("spring.active.profiles", "test");
 */

@WebAppConfiguration
@ContextConfiguration(classes = {Neo4jTestConfig.class, Neo4JDevConfig.class, MVCConfig.class},
        initializers = RestDomainIntegrationTest.PropertyMockingApplicationContextInitializer.class)
@Test(groups = "integration")
@ActiveProfiles("test")
public class RestDomainIntegrationTest extends AbstractTestNGSpringContextTests {
    @Autowired
    WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeMethod
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void getAllResourcesInSystem() throws Exception {
        this.mockMvc.perform(
            get("/read/resources")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk()
                );

    }

    public static class PropertyMockingApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            MutablePropertySources propertySources = applicationContext.getEnvironment().getPropertySources();
            MockPropertySource mockEnvVars = new MockPropertySource().withProperty("spring.profiles.active", "test");
            propertySources.replace(StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME, mockEnvVars);
        }
    }
}
