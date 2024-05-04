package podawan.core


fun String.removeHtmlTags(): String = this.replace(Regex("<[^>]*>"), "")

/**
 * @return true if the provided argument is filled in the route
 */
fun StringBuilder.replace(oldValue: String, newValue: String): StringBuilder {
    val index = indexOf(oldValue)
    return replace(index, index + oldValue.length, newValue)
}

fun String.ifNotEmpty(): String? = this.takeIf { it.isNotEmpty() }

fun String.ifLinkFormat(): String? = this.takeIf {
    it.matches(Regex("""\b(?:https?://|www\.)\S+\b"""))
}