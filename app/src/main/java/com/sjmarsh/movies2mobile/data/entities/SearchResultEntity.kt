package com.sjmarsh.movies2mobile.data.entities

class SearchResultEntity<T> (
    val results: List<T> = listOf(),
    val totalCount: Int = 0
)