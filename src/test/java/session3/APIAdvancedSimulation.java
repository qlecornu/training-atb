package session3;

import io.gatling.javaapi.core.Assertion;
import io.gatling.javaapi.core.PopulationBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;


import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class APIAdvancedSimulation extends Simulation {
    HttpProtocolBuilder httpProtocol = http
            .baseUrl("https://demostore.gatling.io")
            .contentTypeHeader("application/json")
            .acceptHeader("application/json");
    static ScenarioBuilder scenario =
            scenario("API Common Workflow")
                    .exec(Authentication.login)
                    .pause(1, 2)
                    .group("Browse on the website")
                    .on(exec(BrowseWebsite.listCategories)
                            .pause(1, 3)
                            .exec(BrowseWebsite.listProductByCategory));

    static final String TYPE = System.getProperty("TYPE", "smoke");

    static PopulationBuilder getTypeOfLoadTest(String type){
        return switch (type) {
            case "stress" -> scenario.injectOpen(stressPeakUsers(2000).during(10));
            default -> scenario.injectOpen(atOnceUsers(1));
        };
    }

    static Assertion getAssertion(String type){
        return switch (type) {
            case "stress" -> global().responseTime().percentile(95.0).lt(300);
            default -> global().failedRequests().count().lt(1L);
        };
    }

    {
        setUp(
                getTypeOfLoadTest(TYPE))
                .protocols(httpProtocol)
                .assertions(getAssertion(TYPE));
    }
}
