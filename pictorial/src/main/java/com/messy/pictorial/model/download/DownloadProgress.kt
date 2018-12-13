package com.messy.pictorial.model.download

data class DownloadProgress(
    val progress: Int = 0,
    val fileSize: Long = 0,
    val status: Boolean = false,
    val fileName: String = ""
)