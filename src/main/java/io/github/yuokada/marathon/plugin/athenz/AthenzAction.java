package io.github.yuokada.marathon.plugin.athenz;

import mesosphere.marathon.plugin.auth.AuthorizedAction;
import mesosphere.marathon.plugin.auth.CreateGroup$;
import mesosphere.marathon.plugin.auth.CreateResource$;
import mesosphere.marathon.plugin.auth.CreateRunSpec$;
import mesosphere.marathon.plugin.auth.DeleteGroup$;
import mesosphere.marathon.plugin.auth.DeleteResource$;
import mesosphere.marathon.plugin.auth.DeleteRunSpec$;
import mesosphere.marathon.plugin.auth.UpdateGroup$;
import mesosphere.marathon.plugin.auth.UpdateResource$;
import mesosphere.marathon.plugin.auth.UpdateRunSpec$;
import mesosphere.marathon.plugin.auth.ViewGroup$;
import mesosphere.marathon.plugin.auth.ViewResource$;
import mesosphere.marathon.plugin.auth.ViewRunSpec$;

public enum AthenzAction {
    CreateAppOrGroup(CreateGroup$.MODULE$),
    UpdateAppOrGroup(UpdateGroup$.MODULE$),
    DeleteAppOrGroup(DeleteGroup$.MODULE$),
    ViewAppOrGroup(ViewGroup$.MODULE$),

    CreateResource(CreateResource$.MODULE$),
    UpdateResource(UpdateResource$.MODULE$),
    DeleteResource(DeleteResource$.MODULE$),
    ViewResource(ViewResource$.MODULE$),

    CreateRunSpec(CreateRunSpec$.MODULE$),
    UpdateRunSpec(UpdateRunSpec$.MODULE$),
    DeleteRunSpec(DeleteRunSpec$.MODULE$),
    ViewRunSpec(ViewRunSpec$.MODULE$),

    KillTask(DeleteGroup$.MODULE$);

    private final AuthorizedAction<?> action;

    AthenzAction(AuthorizedAction<?> action){
        this.action = action;
    }

    public static AthenzAction byAction(AuthorizedAction<?> action){
        for (AthenzAction a : values()) {
            if (a.action.equals(action)) {
                return a;
            }
        }
        throw new IllegalArgumentException("Unknown AthenzAction: " + action);
    }
}
