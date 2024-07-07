package com.example.tipwise.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.view.CropImageView
import java.io.File

class CropImageContract : ActivityResultContract<CropImageContractOptions, CropImageResult>() {

    override fun createIntent(context: Context, input: CropImageContractOptions): Intent {
        return UCrop.of(input.uri, input.destinationUri)
            .withOptions(input.cropImageOptions.toUCropOptions())
            .getIntent(context)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): CropImageResult {
        return if (resultCode == Activity.RESULT_OK && intent != null) {
            val resultUri = UCrop.getOutput(intent)
            CropImageResult.Success(resultUri)
        } else {
            val error = if (intent != null) UCrop.getError(intent) else null
            CropImageResult.Failure(error)
        }
    }
}
data class CropImageContractOptions(
    val uri: Uri,
    val destinationUri: Uri = Uri.fromFile(File.createTempFile("cropped", ".jpg")),
    val cropImageOptions: CropImageOptions = CropImageOptions()
)

data class CropImageOptions(
    val guidelines: Int = 1, // ON
    val cropShape: Int = 0, // RECTANGLE
    val aspectRatioX: Float = 0f,
    val aspectRatioY: Float = 0f
) {
    fun toUCropOptions(): UCrop.Options {
        return UCrop.Options().apply {
            setCompressionQuality(100)
            setHideBottomControls(false)
            setFreeStyleCropEnabled(true)
            setShowCropGrid(true)
            setShowCropFrame(true)
            setCircleDimmedLayer(cropShape == 1) // 1 for OVAL
            if (aspectRatioX > 0 && aspectRatioY > 0) {
                withAspectRatio(aspectRatioX, aspectRatioY)
            }
        }
    }
}

sealed class CropImageResult {
    data class Success(override val uriContent: Uri?) : CropImageResult() {
        override val error: Throwable? = null
    }

    data class Failure(override val error: Throwable?) : CropImageResult() {
        override val uriContent: Uri? = null
    }

    val isSuccessful: Boolean
        get() = this is Success

    abstract val uriContent: Uri?
    abstract val error: Throwable?
}