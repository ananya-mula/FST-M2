package liveproject;

import au.com.dius.pact.consumer.dsl.DslPart;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

@ExtendWith(PactConsumerTestExt.class)
public class ConsumerTest {
    //Headers
    Map<String, String> header = new HashMap<>();
    //Resource Path
    String resourcePath = "/api/users";

    //Create Pact
    @Pact(consumer = "UserConsumer", provider = "UserProvider")
    public RequestResponsePact createPact(PactDslWithProvider builder){
        // add the headers
        header.put("Content-Type", "application/json");

        //Create the body
        DslPart requestResponseBody = new PactDslJsonBody()
                .numberType("id",123)
                .stringType("firstName","Ananya")
                .stringType("lastName","Mula")
                .stringType("email","abc@example.com");

        //create the contract
        return builder.given("A request to create user")
                .uponReceiving("A request to create a user")
                    .method("POST")
                    .path(resourcePath)
                    .headers(header)
                    .body(requestResponseBody)
                .willRespondWith()
                    .status(201)
                    .body(requestResponseBody)
                .toPact();
    }

    @Test
    @PactTestFor(providerName = "UserProvider", port = "8282")
    public void consumerTest(){
        String baseURI = "http://localhost:8282";
        //create request body
        Map<String,Object> reqBody = new HashMap<>();
        reqBody.put("id",123);
        reqBody.put("firstName","Ana");
        reqBody.put("lastName","Mul");
        reqBody.put("email","xyz@example.com");

        //generate response
        given().headers(header).body(reqBody).log().all()
                .when().post(baseURI+resourcePath)
                .then().statusCode(201).log().all();


    }
}
