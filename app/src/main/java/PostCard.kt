import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.vknewsclient.R.drawable
import com.example.vknewsclient.ui.theme.Black500
import com.example.vknewsclient.ui.theme.VkNewsClientTheme

@Composable
fun PostCard() {
    PostHeader()
    PostBody()
}

@Composable
fun PostBody() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
    ) {
        
    }
}

@Composable
fun PostHeader() {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape),
            painter = painterResource(drawable.community_avatar),
            contentDescription = null,
        )

        Spacer(modifier = Modifier.width(10.dp))


        Column(
            modifier = Modifier
                .weight(1f),
        ) {
            Text(
                text = "уволено",
                color = MaterialTheme.colorScheme.onBackground,
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "14:00",
                color = Black500,
            )
        }

        Icon(
            imageVector = Icons.Rounded.MoreVert,
            contentDescription = null,
            tint = Black500,
        )
    }


}

@Preview
@Composable
fun PreviewPostCardLight() {
    VkNewsClientTheme(
        darkTheme = false
    ) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
        ) {
            PostCard()
        }
    }
}


@Preview
@Composable
fun PreviewPostCardDark() {
    VkNewsClientTheme(
        darkTheme = true
    ) {
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
        ) {
            PostCard()
        }
    }
}