package com.messy.util

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore

fun getFilePathByUri(context: Context, uri: Uri): String {
    var path = ""
    when {
        ContentResolver.SCHEME_FILE == uri.scheme -> {
            path = uri.path!!
        }
        ContentResolver.SCHEME_CONTENT == uri.scheme -> {
            @SuppressLint("ObsoleteSdkInt")
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                context.contentResolver.query(uri, arrayOf(MediaStore.Images.Media.DATA), null, null, null)
                    ?.use {
                        if (it.moveToFirst()) {
                            val columnIndex = it.getColumnIndex(MediaStore.Images.Media.DATA)
                            if (columnIndex > -1)
                                path = it.getString(columnIndex)
                        }
                    }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if ("content".equals(uri.scheme, true)) {
                    context.contentResolver.query(uri, null, null, null, null)
                        ?.use {
                            if (it.moveToFirst()) {
                                val columnIndex = it.getColumnIndex(MediaStore.Images.Media.DATA)
                                if (columnIndex > -1)
                                    path = it.getString(columnIndex)
                            }
                        }
                } else if (DocumentsContract.isDocumentUri(context, uri)) {
                    when {
                        isExternalStorageDocument(uri) -> {
                            val docId = DocumentsContract.getDocumentId(uri)
                            val split = docId.split(":")
                            val type = split[0]
                            if ("primary".equals(type, true)) {
                                path = "${Environment.getExternalStorageDirectory()}/${split[1]}"
                            }
                        }
                        isDownloadsDocument(uri) -> {
                            val docId = DocumentsContract.getDocumentId(uri)
                            val contentUri = ContentUris.withAppendedId(
                                Uri.parse("content://downloads/public_downloads"),
                                docId.toLong()
                            )
                            val column = "_data"
                            val projection = arrayOf(column)
                            path = getDataColumn(context, contentUri, projection, null, null) ?: ""
                        }
                        isMediaDocument(uri) -> {
                            val docId = DocumentsContract.getDocumentId(uri)
                            val split = docId.split(":")
                            val type = split[0]
                            val contentUri: Uri = when (type) {
                                "image" -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                "video" -> MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                                "audio" -> MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                                else -> MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            }
                            val column = "_data"
                            val projection = arrayOf(column)
                            val selection = "_id=?"
                            val selectionArgs = arrayOf(split[1])
                            path = getDataColumn(context, contentUri, projection, selection, selectionArgs) ?: ""
                        }
                    }
                }
            }
        }
    }
    return path
}

private fun getDataColumn(
    context: Context,
    uri: Uri,
    projection: Array<String>?,
    selection: String?,
    selectionArgs: Array<String>?
): String? {
    context.contentResolver.query(uri, projection, selection, selectionArgs, null)?.use {
        if (it.moveToFirst()) {
            val columnIndex = it.getColumnIndex("_data")
            if (columnIndex > -1)
                return it.getString(columnIndex)
        }
    }
    return null
}

private fun isExternalStorageDocument(uri: Uri): Boolean {
    return "com.android.externalstorage.documents" == uri.authority
}

private fun isDownloadsDocument(uri: Uri): Boolean {
    return "com.android.providers.downloads.documents" == uri.authority
}

private fun isMediaDocument(uri: Uri): Boolean {
    return "com.android.providers.media.documents" == uri.authority
}