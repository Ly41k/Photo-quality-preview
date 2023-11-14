package com.example.photoqualitypreview.domain

data class PhotoItem(val photoBytes: ByteArray?) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PhotoItem

        if (photoBytes != null) {
            if (other.photoBytes == null) return false
            if (!photoBytes.contentEquals(other.photoBytes)) return false
        } else if (other.photoBytes != null) return false

        return true
    }

    override fun hashCode(): Int {
        return photoBytes?.contentHashCode() ?: 0
    }
}