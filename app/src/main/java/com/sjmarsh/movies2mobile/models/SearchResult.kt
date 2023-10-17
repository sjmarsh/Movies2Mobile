package com.sjmarsh.movies2mobile.models

class SearchResult<T: ModelBase> (
    val result: List<T> = listOf(),
    val totalCount: Int = 0
)