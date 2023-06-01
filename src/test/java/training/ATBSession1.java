package training;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.core.CoreDsl.atOnceUsers;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.http;

public class ATBSession1 extends Simulation {

    /**
     * Protocol
     */
    HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://demostore.gatling.io")
            .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
            .acceptLanguageHeader("en-US,en;q=0.5")
            .acceptEncodingHeader("gzip, deflate")
            .userAgentHeader(
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0"
            );

    /**
     * VU behavior
     */
    static ScenarioBuilder scenario = scenario("Common user")
            .exec(http("Navigate to homepage")
                    .get("/"))
            ;

    /**
     * Injection Profile
     */
    {
        setUp(
                scenario.injectOpen(atOnceUsers(1)))
                .protocols(httpProtocol);
    }
}
