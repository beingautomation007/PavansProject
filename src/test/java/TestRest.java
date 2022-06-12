import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;

public class TestRest {

    @Test(description = "Verify that getting a random valid user id returns the user’s details", enabled = true)
    public void getRandomValidUser() throws IOException {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com/users";
        RequestSpecification httpsRequest = RestAssured.given();
        String getUserID = Utils.readPropertyFiles(System.getProperty("user.dir")+"/src/main/resources/TestData.properties", "userID");
        Response response = httpsRequest.request(Method.GET, getUserID);
        String responseBody = response.getBody().asString();
        System.out.println("Response Body is : " + responseBody);
        Assert.assertEquals(responseBody.contains(getUserID), true);
    }

    @Test(description = "Verify that all posts have valid post ids (integer between 1-100) when getting posts for the same user", enabled = true)
    public void verifyAllPostHaveValidIDs() {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com/posts";
        RequestSpecification httpsRequest = RestAssured.given();
        Response response = httpsRequest.request(Method.GET);
        JsonPath jsonResult = response.jsonPath();
        jsonResult.get();
       // System.out.println(jsonPath.get());
        ArrayList<Integer> arrayList = jsonResult.get("id");
        System.out.println("********************************");
        System.out.println(arrayList);
        for(int idValueFromPost : arrayList){
            if(idValueFromPost > 0 && idValueFromPost <=100){ //verifying User Id value lies in b/w 1 -100
                assert true;
            }else{
                assert false;
            }
        }
    }

    @Test(description = "Verify that doing a post using the same user with a non-empty title and body will return a correct response (refer to the mock API documentation on what response will be returned")
    public void verifyPostWithNonEmptyTitle() throws IOException {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com/posts";
        RequestSpecification httpsRequest = RestAssured.given();
        JSONObject requestParams = new JSONObject();
        String titleValue = Utils.readPropertyFiles(System.getProperty("user.dir")+"/src/main/resources/TestData.properties", "title");
        Assert.assertEquals(titleValue.isEmpty(), false); //Verifying title should not be empty
        String bodyValue = Utils.readPropertyFiles(System.getProperty("user.dir")+"/src/main/resources/TestData.properties", "body");
        String userIDValue = Utils.readPropertyFiles(System.getProperty("user.dir")+"/src/main/resources/TestData.properties", "userID");
        requestParams.put("title", titleValue);
        requestParams.put("body", bodyValue);
        requestParams.put("userId", userIDValue);
        httpsRequest.header("Content-Type", "application/json"); // Add the Json to the body of the request
        httpsRequest.body(requestParams.toJSONString());
        Response response = httpsRequest.request(Method.POST);
        JsonPath jsonRes = response.jsonPath();
        jsonRes.get();
        ResponseBody responseBody = response.getBody();
        System.out.println(response.getStatusLine());
        System.out.println(responseBody.asString());

        JsonPath jsonPathEvaluator = response.jsonPath();

        String title = jsonPathEvaluator.get("title");
        String body = jsonPathEvaluator.get("body");
        String userId = jsonPathEvaluator.get("userId");
        System.out.println("Title received from Response " + title);

        // Validate the response
        Assert.assertEquals(title, titleValue);
        Assert.assertEquals(body, bodyValue);
        Assert.assertEquals(userId, userIDValue);
    }
}