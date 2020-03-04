package io.robusta.nikotor.user

import io.robusta.nikotor.core.Event
import io.robusta.nikotor.core.ProjectionUpdater
import io.robusta.nikotor.data.DataSet
import java.util.concurrent.CompletableFuture

val usersDatabase = DataSet<User>()


class UsersProjectionUpdater(userBundle: UserBundle) : ProjectionUpdater {
    override val concernedEvents = listOf(
            UserRegisteredEvent::class.java,
            UserActivatedEvent::class.java,
            PasswordUpdatedEvent::class.java,
            AskPasswordResetEvent::class.java,
            UserUpdatedEvent::class.java,
            UserRemovedEvent::class.java)

    override suspend fun updateWithEvent(event: Event<*>) {


        when (event) {
            is UserRegisteredEvent -> {
                usersDatabase.add(event.payload)
                println(usersDatabase)
                // in subscriber: create AskActivationCommand
                //potAuFeuDatabase.add(payload)
            }
        }


    }

}
