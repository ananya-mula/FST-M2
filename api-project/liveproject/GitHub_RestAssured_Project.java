package liveproject;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.IOException;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.oauth2;

public class GitHub_RestAssured_Project {

    RequestSpecification requestSpec;
    ResponseSpecification responseSpec;
    String sshKey;
    int id;
    final static String ROOT_URI = "https://api.github.com/user/keys";


    @BeforeClass
    public void setup(){
        requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAuth(oauth2("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"))
                .setBaseUri("https://api.github.com/user/keys")
                .build();

        responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectContentType("application/json")
                .build();
    }

    @Test(priority = 1)
    public void addSSHKey() throws IOException {
        FileInputStream inputJSON = new FileInputStream("src/test/java/liveproject/keyInfo.json");
        String reqBody = new String(inputJSON.readAllBytes());

        Response response = given().spec(requestSpec)
                .body(reqBody)
                .when().post();

        System.out.println(response.getBody().asPrettyString());
        inputJSON.close();

        id = response.then().extract().path("id");
        System.out.println("The id is : " +id);
        // Assertion
        response.then().statusCode(201);
    }

    @Test(priority=2)
    public void getSSHKey() {
        Response response = given().spec(requestSpec)
                .pathParam("id", id) // Add path parameter
                .when().get("/{id}"); // Send GET request

        // Print response
        System.out.println(response.asPrettyString());
        Reporter.log(response.asPrettyString());

        // Assertion
        response.then().statusCode(200);
    }

    @Test(priority=3)
    public void deleteSSHKey() {
        Response response = given().spec(requestSpec) // Use requestSpec
                .pathParam("id", id) // Add path parameter
                .when().delete("/{id}"); // Send GET request

        // Assertions
        response.then().statusCode(204);
    }


}
