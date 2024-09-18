package com.abz.agency.testtask.presentation.ui.elements

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.abz.agency.testtask.R
import com.abz.agency.testtask.domain.entity.UserEntity
import com.abz.agency.testtask.presentation.ui.theme.Black48
import com.abz.agency.testtask.presentation.ui.theme.Black87

@Composable
fun UserCard(
    modifier: Modifier = Modifier,
    userEntity: UserEntity,
) {
    Row(modifier = modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp)) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(userEntity.photoPath)
                .crossfade(true)
                .build(),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            error = {
                Image(painterResource(id = R.drawable.image_incase), contentDescription = null)
            },
            loading = {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.secondary)
            },
            alignment = Alignment.TopCenter,
            modifier = Modifier.clip(CircleShape)
        )
        Column(Modifier.padding(start = 16.dp)) {
            Text(
                text = userEntity.name,
                style = MaterialTheme.typography.bodyMedium,
                color = Black87,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.padding(top = 8.dp))
            Text(
                text = userEntity.position,
                style = MaterialTheme.typography.bodySmall,
                color = Black48,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.padding(top = 4.dp))
            Text(
                text = userEntity.email,
                style = MaterialTheme.typography.bodySmall,
                color = Black87,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = userEntity.phone,
                style = MaterialTheme.typography.bodySmall,
                color = Black87,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.padding(top = 24.dp))
            HorizontalDivider()
        }
    }
}