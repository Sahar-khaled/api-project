import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.path.json.JsonPath;
import org.json.simple.JSONObject;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class api_automation {
    @Test()
    public void getUserId() {
        Response response = given().when().get("https://jsonplaceholder.typicode.com/users?id=2")
                .then().assertThat().statusCode(200).extract().response();
        String responseInString = response.asString();
        System.out.println(responseInString);

        // get the user email address from the response
        JsonPath jsonPath = new JsonPath(responseInString);
        String emailAddress = jsonPath.getString("email");
        System.out.println(emailAddress);
    }

    @Test
    public void userPost() {
        Response response = given().contentType(ContentType.JSON).when().get("https://jsonplaceholder.typicode.com/posts?userId=2")
                .then().assertThat().statusCode(200).extract().response();
        String responseInString = response.asString();
        System.out.println(responseInString);

        // Using the userID, get the user’s associated posts and
        JsonPath jsonPath = new JsonPath(responseInString);
        String userPosts = jsonPath.getString("title");
        System.out.println(userPosts);

        // verify the Posts contain valid Post IDs (an Integer between 1 and 100).
        String postId = response.asString();
        System.out.println(postId);
//        response.then().assertThat().body("id", allOf(greaterThanOrEqualTo(1), lessThanOrEqualTo(100)));
        response.then().assertThat().body("id",
                everyItem(greaterThanOrEqualTo(1)));
        response.then().assertThat().body("id", everyItem(lessThanOrEqualTo(100)));
    }


    @Test
    public void addPost() {

        JSONObject request = new JSONObject();

        RestAssured.baseURI = "https://jsonplaceholder.typicode.com/posts?userId=2";
        request.put("id", "");
        request.put("title", "foo");
        request.put("body", "bar");
        request.put("userId", "1");

        Response res = given()
                .contentType("application/json")
                .body(request.toJSONString())
                .when()
                .post("");

        String body = res.getBody().asString();
        System.out.println(body);
    }
}