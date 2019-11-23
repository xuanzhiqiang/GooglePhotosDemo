package com.walnut.googleapi.bean


data class SharedAlbumOptions(
        val isCollaborative: Boolean? = false,
        val isCommentable: Boolean? = false
)


data class ShareInfo(
        val sharedAlbumOptions: SharedAlbumOptions? = null,
        val shareableUrl: String? = null,
        val shareToken: String? = null,
        val isJoined: Boolean? = false,
        val isOwned: Boolean? = false
)

data class Album(
        val id: String? = null,
        val title: String? = null,
        val productUrl: String? = null,
        val isWriteable: Boolean? = false,
        val shareInfo: ShareInfo? = null,
        val mediaItemsCount: String? = null,
        val coverPhotoBaseUrl: String? = null,
        val coverPhotoMediaItemId: String? = null,
        // 请求错误时
        val error: Error? = null
) : BaseResponse

data class CreateAlbumRequestBody(val album: Album)

fun createAlbumRequestBody(title: String): CreateAlbumRequestBody = CreateAlbumRequestBody(Album(title = title))


data class ListAlbums(
        val albums: Array<Album>? = null,
        val nextPageToken: String? = null,
        // 请求错误时
        val error: Error? = null
) : BaseResponse {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ListAlbums

        if (albums != null) {
            if (other.albums == null) return false
            if (!albums.contentEquals(other.albums)) return false
        } else if (other.albums != null) return false
        if (nextPageToken != other.nextPageToken) return false
        if (error != other.error) return false

        return true
    }

    override fun hashCode(): Int {
        var result = albums?.contentHashCode() ?: 0
        result = 31 * result + (nextPageToken?.hashCode() ?: 0)
        result = 31 * result + (error?.hashCode() ?: 0)
        return result
    }
}