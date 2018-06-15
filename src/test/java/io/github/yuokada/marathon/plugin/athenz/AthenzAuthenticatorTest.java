package io.github.yuokada.marathon.plugin.athenz;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.util.Optional;
import java.util.Set;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;


public class AthenzAuthenticatorTest
{
    private static final String CONFIG_FILE = "/config.json";

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    public void testThatLDAPIsCorrect() throws Exception {
        AthenzConfiguration config = getConfig();
        assertEquals(config.getAuthenticationHeader(), "Athenz-Role-Auth");
        assertEquals(config.getResource(), "queryengine.privates.muon:devcluster");
        assertEquals(config.getAction(), "*");
        assertEquals(config.getAthenzConfig(), "/etc/athenz/athenz.conf");
        assertEquals(config.getPolicyDir(), "/etc/athenz/zpl");
    }

    private AthenzConfiguration getConfig() throws Exception {
        return MAPPER.readValue(getClass().getResourceAsStream(CONFIG_FILE), AthenzConfiguration.class);
    }

}
