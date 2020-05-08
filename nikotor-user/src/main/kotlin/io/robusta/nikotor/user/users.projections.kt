package io.robusta.nikotor.user

import io.robusta.nikotor.core.Event
import io.robusta.nikotor.core.ProjectionUpdater
import io.robusta.nikotor.data.DataSet
import java.lang.IllegalStateException
import java.util.concurrent.CompletableFuture

val usersDatabase = DataSet<User>()
val accountSet = DataSet<Account>()
val visitSet = DataSet<Visit>()
val loginAttempts = DataSet<LoginAttempt>()

val activationTokenDatabase = DataSet<ActivationTokenRecord>()


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
                val user = event.payload.user
                /* Should be transactional */
                usersDatabase.add(user)
                activationTokenDatabase.add(ActivationTokenRecord(user))
                accountSet.add(Account(user, event.payload.hashedPassword))
                /* End of transaction */
            }
            is UserActivatedEvent -> {
                val activatedUser = queryUserByEmail(event.payload.email)
                        ?: throw IllegalStateException("User ${event.payload.email} disappeared")
                activatedUser.activated = true
            }

            is PasswordUpdatedEvent ->{
                accountSet.update(Account(User(event.payload.email), event.payload.hashedPassword))
            }

            is LoginEvent ->{
                loginAttempts.add(LoginAttempt(event.payload.email, event.payload.token))
            }
        }
    }
}
