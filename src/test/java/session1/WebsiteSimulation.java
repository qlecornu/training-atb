package session1;

import io.gatling.javaapi.core.*; // (1)
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;
public class WebsiteSimulation extends Simulation { // (2)
    HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://demostore.gatling.io")
            .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
            .acceptLanguageHeader("en-US,en;q=0.5")
            .acceptEncodingHeader("gzip, deflate")
            .userAgentHeader(
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0"
            ); // (3)

    static ScenarioBuilder scenario = scenario("Common user") // (4)
            .exec(http("Navigate to homepage")
                    .get("/"));

    {
        setUp(
                scenario.injectOpen(atOnceUsers(1000))) // (5)
                .protocols(httpProtocol); // (6)
    }
}
