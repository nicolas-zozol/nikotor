package io.robusta.nikotor.user

import io.robusta.nikotor.PersistedEvent
import io.robusta.nikotor.ProjectionUpdater
import io.robusta.nikotor.user.User
import java.util.concurrent.CompletableFuture

public val usersDatabase = mutableMapOf<String, User>()

fun getDatabase(): MutableMap<String, User> {
    return usersDatabase
}

class UsersProjectionUpdater() : ProjectionUpdater {
    override val concernedEvents: List<String> = listOf(
        UserEvents.ASK_PASSWORD_RESET,
        UserEvents.USER_ACTIVATED,
        UserEvents.PASSWORD_UPDATED,
        UserEvents.ASK_PASSWORD_RESET,
        UserEvents.USER_UPDATED,
        UserEvents.USER_REMOVED
    )

    override fun <EventPayload> updateWithEvent(event: PersistedEvent<EventPayload>): CompletableFuture<Void> {


        val user = queryUserByEmail(event.payload.e)
        when(event.type){
            UserEvents.ASK_PASSWORD_RESET -> {}
            when (event.payload is EmailPayload){

            }

            UserEvents.USER_ACTIVATED ->
        }

    }

}
