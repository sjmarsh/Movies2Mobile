package com.sjmarsh.movies2mobile.data.mappers

import com.sjmarsh.movies2mobile.data.entities.ActorEntity
import com.sjmarsh.movies2mobile.models.ActorModel
import com.sjmarsh.movies2mobile.ui.extensions.toDate

fun ActorEntity.toModel() : ActorModel {
    return ActorModel(
        id = this.id,
        firstName = this.firstName,
        lastName = this.lastName,
        sex = this.sex,
        photo = this.photo,
        fullName = this.fullName,
        dateOfBirth = this.dateOfBirth.toDate()
    )
}
