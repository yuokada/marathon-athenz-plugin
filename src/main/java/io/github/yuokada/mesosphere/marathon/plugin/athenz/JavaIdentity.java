package io.github.yuokada.mesosphere.marathon.plugin.athenz;

import mesosphere.marathon.plugin.auth.Identity;

public class JavaIdentity implements Identity {

    private final String name;

    public JavaIdentity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
