package session3;

import io.gatling.javaapi.core.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class Authentication {

    public static final FeederBuilder<String> LOGIN_DETAILS = csv("data/loginDetails.csv").circular();

    public static ChainBuilder login =
            feed(LOGIN_DETAILS)
                    .exec(http("authentication")
                            .post("/api/authenticate")
                            .body(StringBody("{\"username\": \"#{username}\",\"password\": \"#{password}\"}"))
                            .check(status().is(200))
                            .check(jmesPath("token").saveAs("jwt")))
                    .exitHereIfFailed();

    public static ChainBuilder relogin =
            exec(http("re-authentication")
                    .post("/api/authenticate")
                    .body(StringBody("{\"username\": \"#{username}\",\"password\": \"#{password}\"}"))
                    .check(status().is(200))
                    .check(jmesPath("token").saveAs("jwt")));


}
