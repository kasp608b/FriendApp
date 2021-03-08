package com.android.friendapp.Model

object Friends {
    public var nextId = 22

    public val mFriends = mutableListOf<BEFriend>(
        BEFriend(1,"Jonas", "123", true),
        BEFriend(2, "Anders", "1234", false),
        BEFriend(3,"Nikolaj", "12345", true),
        BEFriend(4,"Nadia", "12345678", true),
        BEFriend(5, "Michael", "23456789", true),
        BEFriend(6,"Kacper", "87654321", false),
        BEFriend(7, "Rocio", "12121212", true),
        BEFriend(8, "Jonas", "123", true),
        BEFriend(9,"Anders", "1234", false),
        BEFriend(10,"Nikolaj", "12345", true),
        BEFriend(11, "Nadia", "12345678", true),
        BEFriend(12, "Michael", "23456789", true),
        BEFriend(13, "Kacper", "87654321", false),
        BEFriend(14, "Rocio", "12121212", true),
        BEFriend(15, "Jonas", "123", true),
        BEFriend(16,"Anders", "1234", false),
        BEFriend(17, "Nikolaj", "12345", true),
        BEFriend(18, "Nadia", "12345678", true),
        BEFriend(19, "Michael", "23456789", true),
        BEFriend(20, "Kacper", "87654321", false),
        BEFriend(21, "Rocio", "12121212", true)
    )

    public fun getAll(): MutableList<BEFriend> = mFriends


    public fun getAllNames(): Array<String>  =  mFriends.map { p -> p.name }.toTypedArray()



}