package session3;

import io.gatling.javaapi.core.ChainBuilder;

import java.util.Map;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class BrowseWebsite {
    public static final Map<String, String> AUTHORIZATION_HEADERS = Map.ofEntries(
            Map.entry("authorization", "Bearer #{jwt}")
    );
    public static ChainBuilder listCategories =
            tryMax(1).on(
                    exec(
                            http("List Categories")
                                    .get("/api/category")
                                    .headers(AUTHORIZATION_HEADERS)
                                    .check(status().in(200, 401))
                                    .check(status().saveAs("status"))
                                    .check(jmesPath("[0].id").saveAs("categoryId")))
                            .doIfEquals("#{status}", 401).then(Authentication.relogin));

    public static ChainBuilder listProductByCategory =
            tryMax(1).on(
                    exec(
                            http("List Product by Category")
                                    .get("/api/product")
                                    .headers(AUTHORIZATION_HEADERS)
                                    .formParam("category", "#{categoryId}")
                                    .check(status().saveAs("status")))
                            .doIfEquals("#{status}", 401).then(Authentication.relogin));

}
