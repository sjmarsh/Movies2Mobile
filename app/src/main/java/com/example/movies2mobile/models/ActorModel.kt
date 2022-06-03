package com.example.movies2mobile.models

import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.*

class ActorModel (
    id: Int?,
    val firstName: String?,
    val lastName: String?,
    val sex: String?,
    val dateOfBirth: Date?,
    @JsonIgnore
    val photo: String?,
    @JsonIgnore
    val fullName: String?
) : ModelBase(id)