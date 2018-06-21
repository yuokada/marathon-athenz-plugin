package io.github.yuokada.marathon.plugin.athenz;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yahoo.athenz.zpe.AuthZpeClient;
import com.yahoo.athenz.zpe.AuthZpeClient.AccessCheckStatus;
import com.yahoo.athenz.zpe.ZpeConsts;
import java.io.IOException;
import mesosphere.marathon.plugin.PathId;
import mesosphere.marathon.plugin.auth.AuthorizedAction;
import mesosphere.marathon.plugin.auth.Authorizer;
import mesosphere.marathon.plugin.auth.Identity;
import mesosphere.marathon.plugin.http.HttpResponse;
import mesosphere.marathon.plugin.plugin.PluginConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.api.libs.json.JsObject;
import scala.collection.immutable.Map;

public class AthenzAuthorizer implements Authorizer, PluginConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(AthenzAuthorizer.class);

    private AthenzConfiguration config;

    @Override
    public <Resource> boolean isAuthorized(Identity principal,
        AuthorizedAction<Resource> action, Resource resource){
        LOGGER.info("isAuthorized: {}, action: {}, resource: {}", principal, action, resource);
        if (principal instanceof AthenzIdentity) {
            AthenzIdentity identity = (AthenzIdentity) principal;
            AthenzAction athenzAction = AthenzAction.byAction(action);
            LOGGER.info(resource.toString());

            // NOTE: {"message":"mesosphere.marathon.plugin.auth.AuthorizedResource$Plugins$ cannot be cast to mesosphere.marathon.plugin.PathId"}
            return isAuthorized(identity, athenzAction);
        }
        return false;
    }

    private boolean isAuthorized(AthenzIdentity principal, AthenzAction action){
        // NOTE: 認可の処理はこれから頑張る
        LOGGER.info("ACTION: " + action.toString());
        switch (action) {
            case CreateAppOrGroup:
            case UpdateAppOrGroup:
            case DeleteAppOrGroup:
                return !isGuestUser(principal);
            case ViewRunSpec:
            case ViewAppOrGroup:
            case ViewResource:
                return true;
            case KillTask:
                return !isGuestUser(principal);
            default:
                return true;
        }
    }

    private boolean isAuthorized(AthenzIdentity principal, AthenzAction action, PathId path){
        // NOTE: 認可の処理はこれから頑張る
        LOGGER.info("ACTION: " + action.toString());
        switch (action) {
            case CreateAppOrGroup:
                return true;
            case UpdateAppOrGroup:
                // return principal.getUser().contains("ernie");
                return true;
            case DeleteAppOrGroup:
                // return path.toString().startsWith("/test");
                return true;
            case ViewAppOrGroup:
            case ViewResource:
                return true;
            case KillTask:
                return false;
            default:
                return true;
        }
    }

    private AccessCheckStatus getAccessStatus(String token){
        return AuthZpeClient.allowAccess(token, config.getResource(), config.getAction());
    }

    private boolean isGuestUser(Identity principal){
        AthenzIdentity athenzIdentity = (AthenzIdentity) principal;
        return athenzIdentity.isGuestUser();
    }

    @Override
    public void handleNotAuthorized(Identity principal, HttpResponse response) {
        response.status(403);
        if (isGuestUser(principal)) {
            response.body("application/json",
                "{\"Error\": \"Not Authorized to perform this action for GuestUser!\"}".getBytes());
        } else {
            response.body("application/json",
                "{\"Error\": \"Not Authorized to perform this action!\"}".getBytes());
        }
    }

    @Override
    public void initialize(Map<String, Object> marathonInfo, JsObject jsObject){
        try {
            config = new ObjectMapper().readValue(jsObject.toString(), AthenzConfiguration.class);

            System.setProperty(ZpeConsts.ZPE_PROP_ATHENZ_CONF, config.getAthenzConfig());
            System.setProperty(ZpeConsts.ZPE_PROP_POLICY_DIR, config.getPolicyDir());
            AuthZpeClient.init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}