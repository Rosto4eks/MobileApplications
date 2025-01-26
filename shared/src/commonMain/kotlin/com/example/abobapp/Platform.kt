package com.example.abobapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform