package examples;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

public class CodeReuseExample {

    RequestSpecification requestSpec;
    ResponseSpecification responseSpec;
    int petId;

    @BeforeClass
    public void setUp(){
        requestSpec = new RequestSpecBuilder()
                .setBaseUri("https://petstore.swagger.io/v2/pet")
                .setContentType("application/json")
                .build();

        responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectBody("status", equalTo("alive"))
                .expectResponseTime(lessThan(2000L))
                .build();
    }

    @Test(priority = 1)
    public void postRequestTest(){

        //Request Body

        Map<String,Object>  reqBody = new HashMap<>();

        reqBody.put("id",87156);
        reqBody.put("name","Timmy");
        reqBody.put("status","alive");

        //Generate Response

        Response response = given().spec(requestSpec).body(reqBody)
                .when().post();

        System.out.println(response.getBody().asPrettyString());

        //Extract the petId
        petId = response.then().extract().path("id");

        //Assertions
        response.then().spec(responseSpec).body("name",equalTo("Timmy"));
    }

    @Test(priority = 2)
    public void getRequestTest(){
        //Generate Response
        given().spec(requestSpec).log().all().pathParam("petId",petId)
                .when().get("/{petId}")
                .then().spec(responseSpec).log().all();
    }

    @Test(priority = 3)
    public void deleteRequestTest(){
        //Generate Response
        given().spec(requestSpec).log().all().pathParam("petId",petId)
                .when().delete("/{petId}")
                .then().log().all().statusCode(200).body("message",equalTo("" + petId));
    }
}
