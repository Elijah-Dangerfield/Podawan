package podawan.core

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.receiveAsFlow
import podawan.core.common.BuildConfig

object SnackBarPresenter {

    val messages: Channel<Message> = Channel(Channel.UNLIMITED)
    val messagesFlow = messages.receiveAsFlow().distinctUntilChanged()

    fun showDebugMessage(message: Message) {
        if (BuildConfig.DEBUG) {
            messages.trySend(message.copy(isDebug = true))
        }
    }

    fun showMessage(message: Message) {
        messages.trySend(message)
    }

    fun showMessage(
        message: String,
        title: String? = null,
        autoDismiss: Boolean = true,
        action: (() -> Unit)? = null,
        actionLabel: String? = null
    ) {
        messages.trySend(
            Message(
                title = title,
                message = message,
                autoDismiss = autoDismiss,
                action = action,
                actionLabel = actionLabel
            )
        )
    }
}

data class Message(
    val message: String,
    val autoDismiss: Boolean,
    val isDebug : Boolean = false,
    val title: String? = null,
    val action: (() -> Unit)? = null,
    val actionLabel: String? = null
) {
    override fun equals(other: Any?): Boolean {
        return this === other
    }
}
