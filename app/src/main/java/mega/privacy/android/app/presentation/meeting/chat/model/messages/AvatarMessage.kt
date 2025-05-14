package mega.privacy.android.app.presentation.meeting.chat.model.messages

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import mega.privacy.android.app.presentation.meeting.chat.view.ChatAvatar
import mega.privacy.android.app.presentation.meeting.chat.view.LastItemAvatarPosition
import mega.privacy.android.shared.original.core.ui.controls.chat.ChatMessageContainer
import mega.privacy.android.shared.original.core.ui.controls.chat.messages.reaction.model.UIReaction
import mega.privacy.android.shared.original.core.ui.theme.extensions.conditional
import mega.privacy.android.domain.entity.chat.messages.TypedMessage
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import mega.privacy.android.icon.pack.R as IconPackR
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import mega.privacy.android.domain.entity.chat.ChatMessageStatus

/**
 * Avatar message
 */
@OptIn(ExperimentalFoundationApi::class)
abstract class AvatarMessage : UiChatMessage {

    /**
     * Content composable
     *
     * @param onLongClick Already established in the Modifier, but required by some type of messages
     * with text in bubbles and others which has interactions with their content.
     */
    @Composable
    abstract fun ContentComposable(
        interactionEnabled: Boolean,
        onLongClick: () -> Unit,
        initialiseModifier: (onClick: () -> Unit) -> Modifier,
        navHostController: NavHostController,
    )

    abstract override val message: TypedMessage

    /**
     * Avatar composable
     */
    @Composable
    open fun MessageAvatar(
        lastUpdatedCache: Long,
        modifier: Modifier,
        lastItemAvatarPosition: LastItemAvatarPosition?,
    ) {
        if (showAvatar(lastItemAvatarPosition)) {
            ChatAvatar(
                modifier = modifier,
                handle = userHandle,
                lastUpdatedCache = lastUpdatedCache
            )
        } else {
            Spacer(modifier = modifier)
        }
    }

    /**
     * Show avatar
     */
    private fun showAvatar(lastItemAvatarPosition: LastItemAvatarPosition?): Boolean =
        lastItemAvatarPosition?.shouldBeDrawnByMessage() == true


    @Composable
    override fun MessageListItem(
        state: UIMessageState,
        onLongClick: (TypedMessage) -> Unit,
        onMoreReactionsClicked: (Long) -> Unit,
        onReactionClicked: (Long, String, List<UIReaction>) -> Unit,
        onReactionLongClick: (String, List<UIReaction>) -> Unit,
        onForwardClicked: (TypedMessage) -> Unit,
        onSelectedChanged: (Boolean) -> Unit,
        onNotSentClick: (TypedMessage) -> Unit,
        navHostController: NavHostController,
    ) {
        val isError = message.isSendError()

        ChatMessageContainer(
            isMine = displayAsMine,
            showForwardIcon = shouldDisplayForwardIcon,
            reactions = reactions,
            onMoreReactionsClick = { onMoreReactionsClicked(id) },
            onReactionClick = { onReactionClicked(id, it, reactions) },
            onReactionLongClick = { onReactionLongClick(it, reactions) },
            onForwardClicked = { onForwardClicked(message) },
            modifier = Modifier.fillMaxWidth(),
            isSelectMode = state.isInSelectMode,
            isSelected = state.isChecked,
            onSelectionChanged = onSelectedChanged,
            avatarOrIcon = { avatarModifier ->
                MessageAvatar(
                    lastUpdatedCache = state.lastUpdatedCache,
                    avatarModifier,
                    state.lastItemAvatarPosition,
                )
            },
            avatarAlignment = if (state.lastItemAvatarPosition == LastItemAvatarPosition.Top) {
                Alignment.Top
            } else {
                Alignment.Bottom
            },
            isSendError = isError
        ) { interactionEnabled ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
            ) {
                val isPurelySending = message.isMine &&
                        message.status == ChatMessageStatus.SENDING &&
                        !isError

                if (isPurelySending) {
                    if (state.isOnline) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .size(16.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = IconPackR.drawable.ic_clock_rotate_medium_regular_outline),
                            contentDescription = "Pending offline",
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .size(16.dp)
                        )
                    }
                }

                ContentComposable(
                    interactionEnabled = interactionEnabled,
                    onLongClick = {
                        if (interactionEnabled) {
                            onLongClick(message)
                        }
                    },
                    initialiseModifier = { onClickPassedFromContent ->
                        Modifier.contentInteraction(
                            onNotSentClick = onNotSentClick,
                            onClick = onClickPassedFromContent,
                            onLongClick = { onLongClick(message) },
                            interactionEnabled = interactionEnabled
                        )
                    },
                    navHostController = navHostController,
                )
            }
        }
    }

    private fun Modifier.contentInteraction(
        onNotSentClick: (TypedMessage) -> Unit,
        onClick: () -> Unit,
        onLongClick: (TypedMessage) -> Unit,
        interactionEnabled: Boolean,
    ) = if (message.isNotSent() && interactionEnabled) {
        forNotSent(
            onNotSentClick = { onNotSentClick(message) }
        )
    } else {
        setClickHandlers(
            onClick = onClick,
            onLongClick = { onLongClick(message) },
            interactionEnabled = interactionEnabled,
        )
    }

    private fun Modifier.forNotSent(onNotSentClick: () -> Unit) = this.combinedClickable(
        onClick = onNotSentClick,
        onLongClick = onNotSentClick,
    )

    private fun Modifier.setClickHandlers(
        onClick: () -> Unit,
        onLongClick: () -> Unit,
        interactionEnabled: Boolean,
    ) = this.conditional(interactionEnabled) {
        combinedClickable(
            onClick = onClick,
            onLongClick = onLongClick
        )
    }

    override val isSelectable = true
}