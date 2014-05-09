package com.findout.dependency.rest.controller;

import com.findout.dependency.event.resource.CreateResourceEvent;
import com.findout.dependency.rest.controller.fixture.RestDataFixture;
import com.findout.dependency.rest.domain.RestResource;
import com.findout.dependency.service.ResourceRestService;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import org.hamcrest.Matchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.findout.dependency.rest.controller.fixture.RestDataFixture.*;
import static com.findout.dependency.rest.controller.fixture.RestEventFixtures.*;
import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

/**
 * Created by magnusdep on 13/03/14.
 */
@Test(groups = "integration")
public class CreateResourceIntegrationTest {

    @InjectMocks
    ResourceCommandController controller;

    @Mock
    ResourceRestService service;

    @BeforeMethod
    public void setup(){
        MockitoAnnotations.initMocks(this);
        RestAssuredMockMvc.reset();
        RestAssuredMockMvc.standaloneSetup(controller);

        when(service.addResource(any(CreateResourceEvent.class)))
                .thenReturn(resourceCreated(RestDataFixture.ID_1));
    }

    @Test
    public void testThatCreateResourceUsesHttpCreated() throws Exception {
        given()
                .header("Accept", ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(newResourceJSON())
                .when()
                .post("/write/resources")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value()); //rest assured version
    }

    @Test
    public void testThatCreateResourceRendersAsJson() throws Exception {
        given()
                .header("Accept", ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(newResourceJSON())
                .when()
                .post("/write/resources")
                .then()
                .assertThat()
                .contentType(ContentType.JSON)
                .body("resourceId", equalTo(RestDataFixture.ID_1.toString()))
                .body("name", equalTo(RestDataFixture.NAME_1))
                .body("description", equalTo(RestDataFixture.DESCRIPTION_1)); //rest assured version
    }

    @Test
    public void testThatCreateResourcePassesLocationHeader() throws Exception {
        given()
                .header("Accept", ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(newResourceJSON())
                .when()
                .post("/write/resources")
                .then()
                .assertThat()
                .header("Location", Matchers.endsWith("read/resources/" + RestDataFixture.ID_1)); //rest assured version
    }

    @Test
    public void testThatCreateResourceHaveCorrectHateoasLinks() {

        RestResource result = given()
                .header("Accept", ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(newResourceJSON())
                .when()
                .post("/write/resources")
                .as(RestResource.class);

        assertNotNull(result);

        String resourceBase = "/read/resources/" + result.getResourceId();

        assertTrue(result.getLink("self").getHref().endsWith(resourceBase));
    }
}
