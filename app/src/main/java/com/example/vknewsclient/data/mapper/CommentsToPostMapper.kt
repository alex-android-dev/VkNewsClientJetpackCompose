package com.example.vknewsclient.data.mapper

import com.example.vknewsclient.data.mapper.MapTimestampToDate.mapTimestampToDate
import com.example.vknewsclient.data.model.CommentsDto.CommentsResponseDto
import com.example.vknewsclient.domain.PostComment

class CommentsToPostMapper {

    fun mapResponseToPost(responseDto: CommentsResponseDto): List<PostComment> {
        val result = mutableListOf<PostComment>()

        val comments = responseDto.commentsContent.comments
        val profiles = responseDto.commentsContent.profiles

        comments.forEach { comment ->
            val profile = profiles.find { comment.fromProfileId == it.id } ?: return@forEach
            val authorName = profile.firstName + profile.lastName

            val postComment = PostComment(
                id = comment.id,
                authorName = authorName,
                avatarUrl = profile.photoUrl,
                commentText = comment.commentText,
                publicationDate = mapTimestampToDate(comment.publicationDate)
            )

            result.add(postComment)
        }


        return result
    }

}