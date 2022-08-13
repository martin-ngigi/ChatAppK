package com.example.chatappk

class ModelUser {
    var name: String? = null
    var email: String? = null
    var uid: String? = null

    constructor(){}

    constructor(name: String?, email: String?, uid: String?){ // ? means nullable
        this.name = name
        this.email = email
        this.uid = uid
    }

}