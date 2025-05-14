package mega.privacy.android.app.presentation.screen.chat.message

import mega.privacy.android.app.presentation.screen.chat.model.ChatInfoUiModel
import mega.privacy.android.app.presentation.screen.chat.model.ChatScreenCallerData

data class MessageListUiState(
    val chatInfoUiModel: ChatInfoUiModel? = null,
    val chatScreenCallerData: ChatScreenCallerData? = null,
    val scrollToMessageId: String? = null,
    val isScrollToMessageWithFocus: Boolean = false,
    val isScrollToBottom: Boolean = false,
    val isOnline: Boolean = true,
) 