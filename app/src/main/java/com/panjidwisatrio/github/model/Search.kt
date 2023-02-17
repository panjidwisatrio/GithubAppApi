package com.panjidwisatrio.github.model

import com.squareup.moshi.Json

data class Search(
    // annotasi moshi merepresentasikan nama json yang akan diambil.
    @field:Json(name = "items")
    val items: List<User>
)
