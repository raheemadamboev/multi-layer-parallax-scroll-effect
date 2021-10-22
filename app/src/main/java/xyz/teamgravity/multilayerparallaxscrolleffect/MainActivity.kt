package xyz.teamgravity.multilayerparallaxscrolleffect

import android.content.res.Resources
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val moonScrollSpeed = 0.08f
        val midScrollSpeed = 0.03f

        setContent {

            var moonOffset by remember { mutableStateOf(0f) }
            var midOffset by remember { mutableStateOf(0f) }

            val imageHeight = (LocalConfiguration.current.screenWidthDp * (2f / 3f)).dp
            val lazyListState = rememberLazyListState()

            val nestedScrollConnection = remember {
                object : NestedScrollConnection {
                    override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                        val delta = available.y

                        // check if first item or last
                        if (lazyListState.firstVisibleItemIndex == 0) {
                            return Offset.Zero
                        }

                        val layoutInfo = lazyListState.layoutInfo
                        if (layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1) {
                            return Offset.Zero
                        }

                        moonOffset += delta * moonScrollSpeed
                        midOffset += delta * midScrollSpeed
                        return Offset.Zero
                    }
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(nestedScrollConnection),
                state = lazyListState
            ) {
                items(10) {
                    Text(
                        text = "Sample item",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }

                item {
                    Box(
                        modifier = Modifier
                            .clipToBounds()
                            .fillMaxWidth()
                            .height(imageHeight + midOffset.toDp())
                            .background(Brush.verticalGradient(listOf(Color(0xFFf36b21), Color(0xFFf9a521))))
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_moon),
                            contentDescription = "moon",
                            contentScale = ContentScale.FillWidth,
                            alignment = Alignment.BottomCenter,
                            modifier = Modifier
                                .matchParentSize()
                                .graphicsLayer {
                                    translationY = moonOffset
                                }
                        )

                        Image(
                            painter = painterResource(id = R.drawable.ic_mid),
                            contentDescription = "mid",
                            contentScale = ContentScale.FillWidth,
                            alignment = Alignment.BottomCenter,
                            modifier = Modifier
                                .matchParentSize()
                                .graphicsLayer {
                                    translationY = midOffset
                                }
                        )

                        Image(
                            painter = painterResource(id = R.drawable.ic_outer),
                            contentDescription = "outer",
                            contentScale = ContentScale.FillWidth,
                            alignment = Alignment.BottomCenter,
                            modifier = Modifier.matchParentSize()
                        )
                    }
                }

                items(20) {
                    Text(
                        text = "Sample item",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }
            }
        }
    }

    private fun Float.toDp() = (this / Resources.getSystem().displayMetrics.density).dp
}