package com.example.chatappk.Models

class ModelMessage {
    var message: String? = null
    var senderId: String? = null
    var time: String? = null

    constructor(){}

    constructor(message: String?, senderId: String?, time: String?){
        this.message = message
        this.senderId = senderId
        this.time = time
    }
}