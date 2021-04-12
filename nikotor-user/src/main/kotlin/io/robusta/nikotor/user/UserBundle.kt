package io.robusta.nikotor.user


interface EmailSender {

    fun sendEmail(target: String, sender: String, subject: String, content: String){}

}

private class FakeEmailSender:EmailSender

// Aggregate Roots:

// Hasher
// User
// UserCommands
// UserQueries

// Depends on:
// EmailSender
// CQRS Engine


class UserBundle(emailSender: EmailSender) {

    val hasher = Hasher()

    fun userFactory(email: String): User {
        return User(email)
    }

}

class UserCommandAndQueries{

}

class UserQueries{

}
