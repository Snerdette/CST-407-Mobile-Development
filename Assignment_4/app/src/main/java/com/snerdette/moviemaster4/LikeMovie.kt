package com.snerdette.moviemaster4


class LikeMovie {
    var key : String?
    val userId : String?
    val movieID : String?

    constructor() {
        key = null
        userId = null
        movieID = null
    }

    constructor(key: String?, userId: String?, movieID: String?) {
        this.key = key
        this.userId = userId
        this.movieID = movieID
    }
}