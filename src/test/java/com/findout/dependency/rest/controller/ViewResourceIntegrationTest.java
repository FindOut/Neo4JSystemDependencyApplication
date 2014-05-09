package com.findout.dependency.rest.controller;

import com.findout.dependency.event.resource.FindResourceByNameEvent;
import com.findout.dependency.event.resource.RequestAllResourcesEvent;
import com.findout.dependency.event.resource.RequestResourceDetailsEvent;
import com.findout.dependency.rest.controller.fixture.RestDataFixture;
import com.findout.dependency.rest.domain.RestResourceList;
import com.findout.dependency.service.ResourceRestService;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.module.mockmvc.RestAssuredMockMvc;
import com.jayway.restassured.module.mockmvc.response.MockMvcResponse;
import com.jayway.restassured.path.json.JsonPath;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.findout.dependency.rest.controller.fixture.RestEventFixtures.*;
import static com.jayway.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;


/**
 * Created by magnusdep on 10/03/14.
 */
@Test(groups = "integration")
public class ViewResourceIntegrationTest {

    @InjectMocks
    ResourceQueryController controller;

    @Mock
    ResourceRestService service;

    @BeforeMethod
    public void setup(){
        MockitoAnnotations.initMocks(this);
        RestAssuredMockMvc.standaloneSetup(controller);
    }

    @Test
    public void thatViewResourceByIdUsesHttpNotFound() throws Exception {
        when(service.requestResourceDetails(any(RequestResourceDetailsEvent.class)))
                .thenReturn(resourceDetailsNotFound(RestDataFixture.ID_1));

        given()
                .header("Accept", ContentType.JSON)
                .when()
                .get("/read/resources/{id}", RestDataFixture.ID_1)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value()); //rest assured version
    }

    @Test
    public void thatViewResourceByNameUsesHttpNotFound() throws Exception {
        when(service.findByName(any(FindResourceByNameEvent.class)))
                .thenReturn(resourceDetailsNotFound(RestDataFixture.NAME_1));

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/read/resources/getByName/{name}", RestDataFixture.NAME_1)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value()); //rest assured version
    }

    @Test
    public void thatViewResourceByIdRendersCorrectly() throws Exception {
        when(service.requestResourceDetails(any(RequestResourceDetailsEvent.class)))
                .thenReturn(resourceDetailsEvent(RestDataFixture.ID_1));

        MockMvcResponse res= given()
                .header("Accept", ContentType.JSON)
                .when()
                .get("/read/resources/{id}", RestDataFixture.ID_1)
                .then()
                .assertThat() //rest-assured version
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("resourceId", equalTo(RestDataFixture.ID_1.toString()))
                .body("name", equalTo(RestDataFixture.NAME_1))
                .body("description", equalTo(RestDataFixture.DESCRIPTION_1))
                .extract().response();


        //validating that long is acually parsable as a long
        Long id = JsonPath.with(res.body().print()).getLong("resourceId");
        assertNotNull(id);
        assertEquals(id, RestDataFixture.ID_1);
    }

    @Test
    public void thatViewResourceByNameRendersCorrectly() throws Exception {
        when(service.findByName(any(FindResourceByNameEvent.class)))
                .thenReturn(resourceDetailsEvent(RestDataFixture.NAME_1));

        MockMvcResponse res= given()
                .header("Accept", ContentType.JSON)
                .when()
                .get("/read/resources/getByName/{name}", RestDataFixture.NAME_1)
                .then()
                .assertThat() //rest-assured version
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON)
                .body("resourceId", equalTo(RestDataFixture.ID_1.toString()))
                .body("name", equalTo(RestDataFixture.NAME_1))
                .body("description", equalTo(RestDataFixture.DESCRIPTION_1))
                .extract().response();


        //validating that long is acually parsable as a long
        Long id = JsonPath.with(res.body().print()).getLong("resourceId");
        assertNotNull(id);
        assertEquals(id, RestDataFixture.ID_1);
    }

    @Test
    public void thatViewAllResourcesRendersCorrectly() throws Exception {
        final int nofResults = 5;

        when(service.findAll(any(RequestAllResourcesEvent.class)))
                .thenReturn(allResourcesEvent(nofResults));

        RestResourceList result = given()
                .header("Accept", ContentType.JSON)
                .when()
                .get("/read/resources")
                .as(RestResourceList.class);


        assertNotNull(result);
        assertEquals(result.getData().size(), nofResults);
        for (int i = 0; i < result.getData().size(); i++){
            assertEquals(result.getData().get(i).getResourceId(), String.valueOf(RestDataFixture.ID_1 + i));
            assertEquals(result.getData().get(i).getName(), RestDataFixture.NAME_1);
            assertEquals(result.getData().get(i).getDescription(), RestDataFixture.DESCRIPTION_1);
        }
    }

    @Test
    public void thatViewAllResourcesRendersCorrectlyAsJsonWithCorrectHeaders() throws Exception {
        final int nofResults = 5;

        when(service.findAll(any(RequestAllResourcesEvent.class)))
                .thenReturn(allResourcesEvent(nofResults));

        given()
                .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/read/resources")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .contentType(ContentType.JSON);

    }
}
