package session2;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;
public class APISimulation extends Simulation {
    HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://demostore.gatling.io")
            .contentTypeHeader("application/json")
            .acceptHeader("application/json");

    static ScenarioBuilder scenario =
            scenario("API Common Workflow")
                    .feed(csv("data/loginDetails.csv").circular()) // (1)
                    .exec(http("authentication")
                            .post("/api/authenticate")
                            .body(StringBody("{\"username\": \"#{username}\",\"password\": \"#{password}\"}")) // (2)
                            .check(jmesPath("token").saveAs("jwt")))
                    .pause(1,5)// (3)
                    .exec(http("List Categories")
                            .get("/api/category") // (4)
                            .header("Authorization", "Bearer #{jwt}") // (5)
                            .check(jmesPath("[0].id").saveAs("categoryId"))) // (6)
                    .exec(http("List Product by Category")
                            .get("/api/product") // (7)
                            .header("Authorization", "Bearer #{jwt}")
                            .formParam("category", "#{categoryId}"));

//    static ScenarioBuilder scenario =
//            scenario("random browsing")
//                    .feed(csv("data/resource.csv").random())
//                    .exec(http("access #{resource}")
//                            .get("#{resource}"));


    //scenario.injectOpen(atOnceUsers(1))     scenario.injectOpen(stressPeakUsers(2000).during(10))

    {
        setUp(
                scenario.injectOpen(stressPeakUsers(3000).during(10))
                        .protocols(httpProtocol));
    }
}
