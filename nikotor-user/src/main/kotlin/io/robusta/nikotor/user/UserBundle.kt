package io.robusta.nikotor.user


interface EmailSender{

    fun sendEmail(target:String, sender:String, subject:String, content:String)

}

class UserBundle(emailSender: EmailSender) {

}