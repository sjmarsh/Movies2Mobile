package com.sjmarsh.movies2mobile.data

import com.sjmarsh.movies2mobile.models.ImportModel

interface IJsonToModel {
    fun convert(jsonString: String): ImportModel?
}