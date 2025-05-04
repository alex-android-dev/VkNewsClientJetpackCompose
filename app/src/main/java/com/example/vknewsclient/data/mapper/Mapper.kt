package com.example.vknewsclient.data.mapper


import com.example.vknewsclient.data.model.CommentsDto.CommentsResponseDto
import com.example.vknewsclient.data.model.NewsFeedModelDto.NewsFeedResponseDto
import com.example.vknewsclient.domain.entity.FeedPost
import com.example.vknewsclient.domain.entity.PostComment
import com.example.vknewsclient.domain.entity.StatisticItem
import com.example.vknewsclient.domain.entity.StatisticType
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject
import kotlin.math.absoluteValue

class Mapper @Inject constructor() {

    fun mapNewsFeedResponseToPosts(responseDto: NewsFeedResponseDto): List<FeedPost> {
        val result = mutableListOf<FeedPost>()

        val posts = responseDto.newsFeedContent.posts
        val groups = responseDto.newsFeedContent.groups

        posts.forEach { post ->

            val group =
                groups.find { it.id == post.communityId.absoluteValue } ?: return@forEach
            // Находим группу с таким Айди. Удаляем знак минус

            val feedPost = FeedPost(
                id = post.id,
                communityId = group.id,
                communityName = group.name,
                publicationDate = mapTimestampToDate(post.date * 1000),
                communityImageUrl = group.imageUrl,
                contentText = post.text,
                contentImageUrl = post.posts?.firstOrNull()?.photo?.photoUrls?.lastOrNull()?.url,
                statistics = listOf(
                    StatisticItem(type = StatisticType.LIKES, count = post.likes.count),
                    StatisticItem(type = StatisticType.COMMENTS, count = post.comments.count),
                    StatisticItem(type = StatisticType.VIEWS, count = post.views.count),
                    StatisticItem(type = StatisticType.SHARES, count = post.reposts.count)
                ),
                isLiked = post.likes.userLikes > 0
            )

            result.add(feedPost)

        }

        return result
    }

    fun mapCommentsResponseDtoToComments(responseDto: CommentsResponseDto): List<PostComment> {
        val result = mutableListOf<PostComment>()

        val comments = responseDto.commentsContent.comments
        val profiles = responseDto.commentsContent.profiles

        comments.forEach { comment ->
            if (comment.commentText.isBlank()) return@forEach

            val profile = profiles.find { comment.fromProfileId == it.id } ?: return@forEach
            val authorName = "${profile.firstName} ${profile.lastName}"

            val postComment = PostComment(
                id = comment.id,
                authorName = authorName,
                avatarUrl = profile.photoUrl,
                commentText = comment.commentText,
                publicationDate = mapTimestampToDate(comment.publicationDate * 1_000)
                // TODO проверить что будет если так. По хорошему нужно умножить на 1_000
            )

            result.add(postComment)
        }


        return result
    }

    private fun mapTimestampToDate(timestamp: Long): String {
        val date = Date(timestamp)
        return SimpleDateFormat("d MMMM yyyy, hh:mm", Locale.getDefault()).format(date)
    }


}