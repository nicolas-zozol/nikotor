package io.robusta.nikotor.user

import io.robusta.nikotor.PersistedEvent
import io.robusta.nikotor.ProjectionUpdater
import java.util.concurrent.CompletableFuture

public val usersDatabase = mutableMapOf<String, User>()

fun getDatabase(): MutableMap<String, User> {
    return usersDatabase
}

class UsersProjectionUpdater() : ProjectionUpdater {
    override val concernedEvents: List<String> = listOf(
        UserEvents.USER_REGISTERED,
        UserEvents.ASK_PASSWORD_RESET,
        UserEvents.USER_ACTIVATED,
        UserEvents.PASSWORD_UPDATED,
        UserEvents.ASK_PASSWORD_RESET,
        UserEvents.USER_UPDATED,
        UserEvents.USER_REMOVED
    )

    override fun <EventPayload> updateWithEvent(event: PersistedEvent<EventPayload>): CompletableFuture<Void> {


        val nextIndex = usersDatabase.size +1
        return CompletableFuture();

    }

}
