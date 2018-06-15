package io.github.yuokada.marathon.plugin.athenz;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AthenzConfiguration {

    @JsonProperty(value = "authenticationHeader")
    private String authenticationHeader;

    @JsonProperty(value = "athenzResource")
    private String resource;

    @JsonProperty(value = "athenzAction")
    private String action;

    @JsonProperty(required = false)
    private String athenzConfig = "/etc/athenz/athenz.conf";

    @JsonProperty(value = "athenzPolicyDir", required = false)
    private String policyDir = "/etc/athenz/pol";

    public String getAuthenticationHeader()
    {
        return authenticationHeader;
    }

    public void setAuthenticationHeader(String authenticationHeader)
    {
        this.authenticationHeader = authenticationHeader;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAthenzConfig() {
        return athenzConfig;
    }

    public void setAthenzConfig(String athenzConfig) {
        this.athenzConfig = athenzConfig;
    }

    public String getPolicyDir() {
        return policyDir;
    }

    public void setPolicyDir(String policyDir) {
        this.policyDir = policyDir;
    }

    @Override
    public String toString()
    {
        return "AthenzConfiguration{" +
                "authenticationHeader='" + authenticationHeader + '\'' +
                ", resource='" + resource + '\'' +
                ", action='" + action + '\'' +
                ", athenzConfig='" + athenzConfig + '\'' +
                ", policyDir='" + policyDir + '\'' +
                '}';
    }
}
