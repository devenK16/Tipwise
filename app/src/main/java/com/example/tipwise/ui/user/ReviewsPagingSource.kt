import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.tipwise.api.ReviewAPI
import com.example.tipwise.models.Review
import com.example.tipwise.repository.ReviewRepository
import java.io.IOException

class ReviewPagingSource(
    private val reviewAPI: ReviewAPI,
    private val userId: String
) : PagingSource<Int, Review>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Review> {
        return try {
            val page = params.key ?: 1
            val limit = params.loadSize
            Log.d("ReviewPagingSource", "Loading page $page with limit $limit for user $userId")
            val response = reviewAPI.getReviews(userId, page, limit)

            if (response.isSuccessful) {
                val body = response.body()!!
                Log.d("ReviewPagingSource", "Loaded ${body.reviews.size} reviews. Current page: ${body.currentPage}, Total pages: ${body.totalPages}")
                LoadResult.Page(
                    data = body.reviews,
                    prevKey = if (page > 1) page - 1 else null,
                    nextKey = if (body.reviews.size == limit) page + 1 else null
                )
            } else {
                Log.e("ReviewPagingSource", "API call failed: ${response.code()} ${response.message()}")
                LoadResult.Error(Exception("Failed to load"))
            }
        } catch (e: Exception) {
            Log.e("ReviewPagingSource", "Error loading reviews", e)
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Review>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}