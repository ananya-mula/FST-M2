package examples;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.hamcrest.Matchers.equalTo;

public class FirstTest {

    String baseURI = "https://petstore.swagger.io/v2/pet";

    @Test
    public void getRequestTest() throws MalformedURLException {
        Response response = given().header("Content-Type","application/json").queryParam("status", "sold")
                .when().get(baseURI+"/findByStatus");

        System.out.println(response.getBody().asString());
        System.out.println(response.getBody().asPrettyString());


        String petStatus = response.then().extract().path("[0].status");
        System.out.println("Status of the first Pet: " +petStatus);

        System.out.println(response.getHeaders().asList());

        response.then().statusCode(200);
        response.then().body("[0].status",equalTo("sold"));

        URL schemaurl = new URL("https://petstore.swagger.io/v2/swagger.json");
        response.then().body(matchesJsonSchema(schemaurl));
    }

    @Test
    public void getRequestPathParam(){

        given().header("Content-Type","application/json").pathParam("petId", "122")
                .when().get(baseURI +"/{petId}")
                .then().statusCode(200).body("status",equalTo("sold"));

    }

}
