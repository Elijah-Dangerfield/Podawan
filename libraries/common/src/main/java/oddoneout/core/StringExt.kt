package podawan.core


fun String.removeHtmlTags(): String = this.replace(Regex("<[^>]*>"), "")
