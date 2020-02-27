package io.robusta.nikotor.user

import io.robusta.nikotor.core.Event
import io.robusta.nikotor.core.ProjectionUpdater
import io.robusta.nikotor.data.DataSet
import java.util.concurrent.CompletableFuture

val usersDatabase = DataSet<User>()


class UsersProjectionUpdater() : ProjectionUpdater {
    override val concernedEvents = listOf(
            UserRegisteredEvent::class.java,
            PasswordUpdatedPayload::class.java,
            UserUpdatedPayload::class.java,
            UserActivatedEvent::class.java,
            PasswordUpdatedEvent::class.java,
            AskPasswordResetEvent::class.java,
            UserUpdatedEvent::class.java,
            UserRemovedEvent::class.java)

    override fun  updateWithEvent(event: Event<*>): CompletableFuture<Void> {


        when(event){
            is UserRegisteredEvent-> {
                val user = event.payload
                //potAuFeuDatabase.add(payload)
            }
        }

        return CompletableFuture();

    }

}
