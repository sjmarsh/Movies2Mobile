package com.sjmarsh.movies2mobile.data

import com.sjmarsh.movies2mobile.data.fileStorage.entities.ImportEntity

interface IJsonToModel {
    fun convert(jsonString: String): ImportEntity?
}