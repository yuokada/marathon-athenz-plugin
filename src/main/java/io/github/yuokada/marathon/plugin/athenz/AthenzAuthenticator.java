package io.github.yuokada.marathon.plugin.athenz;

import akka.dispatch.ExecutionContexts;
import akka.dispatch.Futures;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yahoo.athenz.auth.token.RoleToken;
import com.yahoo.athenz.zpe.AuthZpeClient;
import com.yahoo.athenz.zpe.AuthZpeClient.AccessCheckStatus;
import com.yahoo.athenz.zpe.ZpeConsts;
import java.io.IOException;
import java.util.concurrent.Executors;
import mesosphere.marathon.plugin.auth.Authenticator;
import mesosphere.marathon.plugin.auth.Identity;
import mesosphere.marathon.plugin.http.HttpRequest;
import mesosphere.marathon.plugin.http.HttpResponse;
import mesosphere.marathon.plugin.plugin.PluginConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.api.libs.json.JsObject;
import scala.Option;
import scala.collection.immutable.Map;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;

public class AthenzAuthenticator implements Authenticator, PluginConfiguration {

    private final ExecutionContext EC = ExecutionContexts
        .fromExecutorService(Executors.newSingleThreadExecutor());
    private AthenzConfiguration config;
    private static final Logger LOGGER = LoggerFactory.getLogger(AthenzAuthenticator.class);

    public AthenzAuthenticator() {
    }

    @Override
    public Future<Option<Identity>> authenticate(HttpRequest request) {
        return Futures.future(() -> Option.apply(doAuth(request)), EC);
    }

    private Identity doAuth(HttpRequest request) {
        try {
            Option<String> header = request.header(config.getAuthenticationHeader()).headOption();
            if (header.isDefined()) {
                String tokenString = header.get();
                AuthZpeClient.AccessCheckStatus status = getAccessCheckStatus(tokenString);
                if (status == AccessCheckStatus.ALLOW) {
                    RoleToken roleToken = AuthZpeClient.validateRoleToken(tokenString);
                    return new AthenzIdentity(roleToken);
                } else {
                    return null;
                }
            }
        } catch (Exception ex) { /* do not authenticate in case of exception */ }
        if (config.getEnableGuest()) {
            // Return GuestUser Identity
            return new AthenzIdentity();
        } else {
            return null;
        }
    }

    private AccessCheckStatus getAccessCheckStatus(String tokenString){
        return AuthZpeClient.allowAccess(tokenString, config.getResource(), config.getAction());
    }

    @Override
    public void handleNotAuthenticated(HttpRequest request, HttpResponse response) {
        Option<String> headerValue = request.header(config.getAuthenticationHeader()).headOption();
        LOGGER.info(headerValue.get());

        AuthZpeClient.AccessCheckStatus status = getAccessCheckStatus(headerValue.get());
        response.status(401);
        response.header("AthenzResource", config.getResource() + ":" + config.getAction());
        response.body("application/json",
            String.format("{\"Error\": \"%s\"}", status.toString()).getBytes());
    }
    @Override
    public void initialize(Map<String, Object> marathonInfo, JsObject jsObject) {
        try {
            LOGGER.info(jsObject.toString());
            config = new ObjectMapper().readValue(jsObject.toString(), AthenzConfiguration.class);

            System.setProperty(ZpeConsts.ZPE_PROP_ATHENZ_CONF, config.getAthenzConfig());
            System.setProperty(ZpeConsts.ZPE_PROP_POLICY_DIR,  config.getPolicyDir());
            AuthZpeClient.init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

