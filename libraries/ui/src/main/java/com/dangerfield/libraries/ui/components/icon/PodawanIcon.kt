package com.dangerfield.libraries.ui.components.icon

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.IosShare
import androidx.compose.material.icons.filled.LibraryMusic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LibraryMusic
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Upcoming
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material.icons.rounded.Bookmarks
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Grid3x3
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.ShortText
import androidx.compose.material.icons.rounded.Upcoming
import androidx.compose.material.icons.rounded.ViewDay
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dangerfield.libraries.ui.Preview
import com.dangerfield.libraries.ui.components.text.Text


sealed class PodawanIcon(
    val imageVector: ImageVector,
    val contentDescription: String?
) {

    class HomeOutline(contentDescription: String?) : PodawanIcon(
        imageVector = Icons.Outlined.Home,
        contentDescription = contentDescription
    )

    class HomeFilled(contentDescription: String?) : PodawanIcon(
        imageVector = Icons.Filled.Home,
        contentDescription = contentDescription
    )

    class SearchOutline(contentDescription: String?) : PodawanIcon(
        imageVector = Icons.Outlined.Search,
        contentDescription = contentDescription
    )

    class SearchFilled(contentDescription: String?) : PodawanIcon(
        imageVector = Icons.Filled.Search,
        contentDescription = contentDescription
    )

    class LibraryOutline(contentDescription: String?) : PodawanIcon(
        imageVector = Icons.Outlined.LibraryMusic,
        contentDescription = contentDescription
    )

    class LibraryFilled(contentDescription: String?) : PodawanIcon(
        imageVector = Icons.Filled.LibraryMusic,
        contentDescription = contentDescription
    )

    class Add(contentDescription: String?) : PodawanIcon(
        imageVector = Icons.Rounded.Add,
        contentDescription = contentDescription
    )

    class ArrowBack(contentDescription: String?) : PodawanIcon(
        imageVector = Icons.Rounded.ArrowBack,
        contentDescription = contentDescription
    )

    class Bookmark(contentDescription: String?) : PodawanIcon(
        imageVector = Icons.Rounded.Bookmark,
        contentDescription = contentDescription
    )

    class BookmarkBorder(contentDescription: String?) : PodawanIcon(
        contentDescription = contentDescription,
        imageVector = Icons.Rounded.BookmarkBorder,
    )

    class Bookmarks(contentDescription: String?) :
        PodawanIcon(imageVector = Icons.Rounded.Bookmarks, contentDescription = contentDescription)

    class BookmarksBorder(contentDescription: String?) :
        PodawanIcon(imageVector = Icons.Outlined.Bookmarks, contentDescription = contentDescription)

    class Info(contentDescription: String?) :
        PodawanIcon(imageVector = Icons.Default.Info, contentDescription = contentDescription)

    class Check(contentDescription: String?) :
        PodawanIcon(imageVector = Icons.Rounded.Check, contentDescription = contentDescription)

    class Close(contentDescription: String?) :
        PodawanIcon(imageVector = Icons.Rounded.Close, contentDescription = contentDescription)

    class Grid3x3(contentDescription: String?) :
        PodawanIcon(imageVector = Icons.Rounded.Grid3x3, contentDescription = contentDescription)

    class MoreVert(contentDescription: String?) :
        PodawanIcon(imageVector = Icons.Default.MoreVert, contentDescription = contentDescription)

    class Person(contentDescription: String?) :
        PodawanIcon(imageVector = Icons.Rounded.Person, contentDescription = contentDescription)

    class Search(contentDescription: String?) :
        PodawanIcon(imageVector = Icons.Rounded.Search, contentDescription = contentDescription)

    class Settings(contentDescription: String?) :
        PodawanIcon(imageVector = Icons.Rounded.Settings, contentDescription = contentDescription)

    class ShortText(contentDescription: String?) :
        PodawanIcon(imageVector = Icons.Rounded.ShortText, contentDescription = contentDescription)

    class Upcoming(contentDescription: String?) :
        PodawanIcon(imageVector = Icons.Rounded.Upcoming, contentDescription = contentDescription)

    class UpcomingBorder(contentDescription: String?) :
        PodawanIcon(imageVector = Icons.Outlined.Upcoming, contentDescription = contentDescription)

    class ViewDay(contentDescription: String?) :
        PodawanIcon(imageVector = Icons.Rounded.ViewDay, contentDescription = contentDescription)

    class ChevronLeft(contentDescription: String?) : PodawanIcon(
        imageVector = Icons.Default.ChevronLeft,
        contentDescription = contentDescription
    )

    class ChevronRight(contentDescription: String?) : PodawanIcon(
        imageVector = Icons.Default.ChevronRight,
        contentDescription = contentDescription
    )

    class Theme(contentDescription: String?) : PodawanIcon(
        imageVector = Icons.Default.Palette,
        contentDescription = contentDescription
    )

    class Chat(contentDescription: String?) : PodawanIcon(
        imageVector = Icons.Default.ChatBubble,
        contentDescription = contentDescription
    )

    class Android(contentDescription: String?) : PodawanIcon(
        imageVector = Icons.Default.Android,
        contentDescription = contentDescription
    )

    class DropDown(contentDescription: String?) : PodawanIcon(
        imageVector = Icons.Default.ArrowDropDown,
        contentDescription = contentDescription
    )

    class Bug(contentDescription: String?) : PodawanIcon(
        imageVector = Icons.Default.BugReport,
        contentDescription = contentDescription
    )

    class VideoCall(contentDescription: String?) : PodawanIcon(
        imageVector = Icons.Default.Videocam,
        contentDescription = contentDescription
    )

    class Pencil(contentDescription: String?) : PodawanIcon(
        imageVector = Icons.Default.Edit,
        contentDescription = contentDescription
    )

    class Alarm(contentDescription: String?) : PodawanIcon(
        imageVector = Icons.Default.Alarm,
        contentDescription = contentDescription
    )

    class Question(contentDescription: String?) : PodawanIcon(
        imageVector = Icons.Default.QuestionMark,
        contentDescription = contentDescription
    )

    class Share(contentDescription: String?) : PodawanIcon(
        imageVector = Icons.Default.IosShare,
        contentDescription = contentDescription
    )

    class Copy(contentDescription: String?) : PodawanIcon(
        imageVector = Icons.Default.ContentCopy,
        contentDescription = contentDescription
    )

    class Graph(contentDescription: String?) : PodawanIcon(
        imageVector = Icons.Default.AutoGraph,
        contentDescription = contentDescription
    )

    class Play(contentDescription: String?) : PodawanIcon(
        imageVector = Icons.Default.PlayArrow,
        contentDescription = contentDescription
    )

    class Pause(contentDescription: String?) : PodawanIcon(
        imageVector = Icons.Default.Pause,
        contentDescription = contentDescription
    )

    class ArrowCircleDown(contentDescription: String?) : PodawanIcon(
        imageVector = Icons.Default.ArrowCircleDown,
        contentDescription = contentDescription
    )
}

private val allIcons = listOf(
    PodawanIcon.Add(""),
    PodawanIcon.ArrowBack(""),
    PodawanIcon.Bookmark(""),
    PodawanIcon.BookmarkBorder(""),
    PodawanIcon.Bookmarks(""),
    PodawanIcon.BookmarkBorder(""),
    PodawanIcon.BookmarksBorder(""),
    PodawanIcon.Info(""),
    PodawanIcon.Check(""),
    PodawanIcon.Chat(""),
    PodawanIcon.Close(""),
    PodawanIcon.Grid3x3(""),
    PodawanIcon.MoreVert(""),
    PodawanIcon.Person(""),
    PodawanIcon.Search(""),
    PodawanIcon.Settings(""),
    PodawanIcon.ShortText(""),
    PodawanIcon.Upcoming(""),
    PodawanIcon.UpcomingBorder(""),
    PodawanIcon.ViewDay(""),
    PodawanIcon.ChevronLeft(""),
    PodawanIcon.ChevronRight(""),
    PodawanIcon.Theme(""),
    PodawanIcon.Android(""),
    PodawanIcon.DropDown(""),
    PodawanIcon.Bug(""),
    PodawanIcon.VideoCall(""),
    PodawanIcon.Pencil(""),
    PodawanIcon.Alarm(""),
    PodawanIcon.Question(""),
    PodawanIcon.Share(""),
    PodawanIcon.Copy(""),
    PodawanIcon.Graph(""),
    PodawanIcon.HomeOutline(""),
    PodawanIcon.HomeFilled(""),
    PodawanIcon.SearchOutline(""),
    PodawanIcon.SearchFilled(""),
    PodawanIcon.LibraryOutline(""),
    PodawanIcon.LibraryFilled(""),
    PodawanIcon.Play(""),
    PodawanIcon.Pause(""),
    PodawanIcon.ArrowCircleDown(""),
)

@Preview(device = "spec:id=reference_phone,shape=Normal,width=1000,height=1800,unit=dp,dpi=200")
@Composable
private fun IconPreview() {
    Preview(showBackground = true) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(allIcons) { icon ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Icon(
                        imageVector = icon.imageVector,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = icon::class.java.simpleName)
                }
            }
        }
    }
}
