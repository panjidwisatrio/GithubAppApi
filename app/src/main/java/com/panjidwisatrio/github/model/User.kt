package com.panjidwisatrio.github.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

// merepresentasikan tabel "user" dalam database dengan nama yang sama.
@Entity(tableName = "user")
data class User(
    // annotasi moshi merepresentasikan nama json yang akan diambil.
    @field:Json(name = "id")
    // annotasi room merepresentasikan kolom "id" dalam tabel "user" dengan nama yang sama.
    @ColumnInfo(name = "id")
    // annotasi room merepresentasikan primary key dari tabel "user".
    @PrimaryKey
    val id: Int? = 0,

    @field:Json(name = "login")
    @ColumnInfo(name = "username")
    val username: String? = "",

    @field:Json(name = "name")
    @ColumnInfo(name = "name")
    val name: String? = "",

    @field:Json(name = "location")
    @ColumnInfo(name = "location")
    val location: String? = "",

    @field:Json(name = "company")
    @ColumnInfo(name = "company")
    val company: String? = "",

    @field:Json(name = "public_repos")
    @ColumnInfo(name = "repository")
    val repository: Int? = 0,

    @field:Json(name = "followers")
    @ColumnInfo(name = "follower")
    val follower: Int? = 0,

    @field:Json(name = "following")
    @ColumnInfo(name = "following")
    val following: Int? = 0,

    @field:Json(name = "avatar_url")
    @ColumnInfo(name = "avatar")
    val avatar: String? = "",

    @ColumnInfo(name = "isFavorite")
    var isFavorite: Boolean = false
)
