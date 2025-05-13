package mega.privacy.android.app.presentation.imagepreview.fetcher

import android.os.Bundle
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import mega.privacy.android.app.presentation.imagepreview.ImagePreviewViewModel
import mega.privacy.android.app.presentation.photos.model.Sort
import mega.privacy.android.domain.entity.node.ImageNode
import mega.privacy.android.domain.entity.node.NodeId
import mega.privacy.android.domain.qualifier.DefaultDispatcher
import mega.privacy.android.domain.usecase.photos.MonitorMediaDiscoveryNodesUseCase
import timber.log.Timber
import javax.inject.Inject

class MediaDiscoveryImageNodeFetcher @Inject constructor(
    private val monitorMediaDiscoveryNodesUseCase: MonitorMediaDiscoveryNodesUseCase,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
) : ImageNodeFetcher {
    override fun monitorImageNodes(bundle: Bundle): Flow<List<ImageNode>> {
        val parentId = NodeId(bundle.getLong(PARENT_ID))
        val recursive = bundle.getBoolean(IS_RECURSIVE)

        val sortModeName = bundle.getString(ImagePreviewViewModel.IMAGE_PREVIEW_CURRENT_SORT_FETCHER_PARAM)
        val currentSort = try {
            sortModeName?.let { Sort.valueOf(it) }
        } catch (e: IllegalArgumentException) {
            Timber.w(e, "Invalid sort mode name received: $sortModeName")
            null
        }

        Timber.d("MediaDiscoveryImageNodeFetcher: parentId=$parentId, recursive=$recursive, sortMode=$currentSort")

        return monitorMediaDiscoveryNodesUseCase(
            parentId = parentId,
            recursive = recursive,
        ).mapLatest { imageNodes ->
            when (currentSort) {
                Sort.NEWEST -> imageNodes.sortedWith(compareByDescending<ImageNode> { it.modificationTime }.thenByDescending { it.id.longValue })
                Sort.OLDEST -> imageNodes.sortedWith(compareBy<ImageNode> { it.modificationTime }.thenBy { it.id.longValue })
                // Add other sort cases if they exist in the Sort enum and are relevant
                // For example, by name:
                // Sort.NAME_ASC -> imageNodes.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.name }.thenBy { it.id.longValue })
                // Sort.NAME_DESC -> imageNodes.sortedWith(compareByDescending(String.CASE_INSENSITIVE_ORDER) { it.name }.thenByDescending { it.id.longValue })
                null -> {
                    Timber.d("MediaDiscoveryImageNodeFetcher: No valid sort mode from bundle, using default (newest first).")
                    imageNodes.sortedWith(compareByDescending<ImageNode> { it.modificationTime }.thenByDescending { it.id.longValue })
                }
                else -> {
                    // Fallback for any other Sort enum values not explicitly handled, though ideally all are.
                    Timber.w("MediaDiscoveryImageNodeFetcher: Unhandled sort mode '$currentSort', using default.")
                    imageNodes.sortedWith(compareByDescending<ImageNode> { it.modificationTime }.thenByDescending { it.id.longValue })
                }
            }
        }.flowOn(defaultDispatcher)
    }

    internal companion object {
        const val PARENT_ID = "parentId"
        const val IS_RECURSIVE = "recursive"
    }
}