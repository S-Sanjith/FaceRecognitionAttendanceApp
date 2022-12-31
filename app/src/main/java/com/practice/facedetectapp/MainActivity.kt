package com.practice.facedetectapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.*
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.practice.facedetectapp.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

// typealias LumaListener = (luma: Double) -> Unit
// typealias QrListener = (qrValue: String) -> Unit
// typealias FaceListener = (faceValue: String) -> Unit

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding

    private var imageCapture: ImageCapture? = null

    // private var videoCapture: VideoCapture<Recorder>? = null
    // private var recording: Recording? = null

    private lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
        /*
        fixedRateTimer("timer", false, 0L, 2000) {
            this@MainActivity.runOnUiThread {
                if(QrAnalyzer.S != "") {
                    Toast.makeText(baseContext, QrAnalyzer.S, Toast.LENGTH_SHORT).show()
                    QrAnalyzer.S = ""
                }
            }
        }
        */

        /*
        fixedRateTimer("timer", false, 0L, 2000) {
            this@MainActivity.runOnUiThread {
                if(FaceAnalyzer.S != "") {
                    Toast.makeText(baseContext, FaceAnalyzer.S, Toast.LENGTH_SHORT).show()
                    FaceAnalyzer.S = ""
                }
            }
        }
        */

        // Set up the listeners for take photo and video capture buttons
        viewBinding.imageCaptureButton.setOnClickListener { takePhoto() }
        viewBinding.videoCaptureButton.setOnClickListener { captureVideo() }

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create time stamped name and MediaStore entry.
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues)
            .build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun
                        onImageSaved(output: ImageCapture.OutputFileResults){
                    val msg = "Photo capture succeeded: ${output.savedUri}"
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    Log.d(TAG, msg)
                }
            }
        )
    }

    private fun captureVideo() {}

    @SuppressLint("UnsafeOptInUsageError")
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(viewBinding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            /*
            val imageAnalyzer = ImageAnalysis.Builder()
                .build()
                .also {
                    //it.setAnalyzer(cameraExecutor, QrAnalyzer { luma ->
                    it.setAnalyzer(cameraExecutor, FaceAnalyzer { luma ->
                        Log.d(TAG, "Average luminosity: $luma")
                    })
                }

             */

            // Select front camera as a default
            //val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            val point = Point()
            //val size = display?.getRealSize(point)
            val imageAnalysis = ImageAnalysis.Builder()
                .setTargetResolution(Size(point.x, point.y))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            val highAccuracyOpts = FaceDetectorOptions.Builder()
                //.setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                //.setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                //.setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                //.enableTracking()
                .build()

            val objectDetector = FaceDetection.getClient(highAccuracyOpts)

            imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this)) { imageProxy ->
                //val im = getCorrectionMatrix(imageProxy, viewBinding.viewFinder)

                val scaleX = viewBinding.viewFinder.width.toFloat() / imageProxy.height.toFloat()
                val scaleY = viewBinding.viewFinder.height.toFloat() / imageProxy.width.toFloat()

//                val scaleX = viewBinding.viewFinder.width.toFloat() / imageProxy.height.toFloat()
//                val scaleY = viewBinding.viewFinder.height.toFloat() / imageProxy.width.toFloat()
//                val r: Rect = Rect()
//                val width = viewBinding.viewFinder.width
//                r.left = width + 20
//                r.right = width - 20
//                r.top
//
                val element =
                    Draw(this, Rect(), "", (1).toFloat(), (1).toFloat())
                viewBinding.layout.addView(element, 1)

                val rotationDegrees = imageProxy.imageInfo.rotationDegrees
                val image = imageProxy.image
                if (image != null) {
                    val inputImage = InputImage.fromMediaImage(image, rotationDegrees)
                    objectDetector
                        .process(inputImage)
                        .addOnFailureListener {
                            imageProxy.close()
                        }.addOnSuccessListener { objects ->
                            // Here, we get a list of objects which are detected.
                            if(objects.isEmpty())
                                viewBinding.faceDetectStatus.text = "No face detected"
                            else
                                viewBinding.faceDetectStatus.text = "Face detected"

//                            for (it in objects) {
                                // val detectedFace = it.boundingBox
                                // Bitmap faceBmp = Bitmap.createBitmap()
//                                if (viewBinding.layout.childCount > 1) viewBinding.layout.removeViewAt(
//                                    1
//                                )
//                                val r = it.boundingBox
//                                if(cameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA) {
//                                    val rc = viewBinding.viewFinder.width - r.left
//                                    val lc = viewBinding.viewFinder.width - r.right
//                                    r.left = lc
//                                    r.right = rc
//                                }

//                                val element =
//                                    Draw(this, r, "", scaleX, scaleY)
//                                viewBinding.layout.addView(element, 1)

//                            }
                            //imageProxy.close()
                        }
                        .addOnCompleteListener {
                            imageProxy.close()
                        }
                }
            }

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture, imageAnalysis)

            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    /*
    private fun setFaceDetector(imageAnalysis: ImageAnalysis, lensFacing: Int) {
        viewBinding.viewFinder.previewStreamState.observe(this, object: Observer<PreviewView.StreamState> {
            override fun onChanged(streamState: PreviewView.StreamState?) {
                if (streamState != PreviewView.StreamState.STREAMING) {
                    return
                }

                val preview = viewBinding.viewFinder.getChildAt(0)
                var width = preview.width * preview.scaleX
                var height = preview.height * preview.scaleY
                val rotation = preview.display.rotation
                if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270) {
                    val temp = width
                    width = height
                    height = temp
                }

                imageAnalysis.setAnalyzer(
                    executor,
                    createFaceDetector(width.toInt(), height.toInt(), lensFacing)
                )
                viewBinding.viewFinder.previewStreamState.removeObserver(this)
            }
        })
    }

    private fun createFaceDetector(
        viewfinderWidth: Int,
        viewfinderHeight: Int,
        lensFacing: Int
    ): ImageAnalysis.Analyzer {
        val isFrontLens = lensFacing == CameraSelector.LENS_FACING_FRONT
        val faceDetector = FaceAnalyzer(viewfinderWidth, viewfinderHeight, isFrontLens)
        faceDetector.listener = object : FaceAnalyzer.Listener {
            override fun onFacesDetected(faceBounds: List<RectF>) {
                faceBoundsOverlay.post { faceBoundsOverlay.drawFaceBounds(faceBounds) }
            }

            override fun onError(exception: Exception) {
                Log.d(TAG, "Face detection error", exception)
            }
        }
        return faceDetector
    }


     */
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val TAG = "CameraXApp"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }
}

class Draw(context: Context?, private var rect: Rect, private var text: String, private var _scaleX: Float, private var _scaleY: Float) : View(context) {

    private lateinit var paint: Paint
    private lateinit var textPaint: Paint

    init {
        init()
    }

    private fun init() {
        paint = Paint()
        paint.color = Color.RED
        paint.strokeWidth = 20f
        paint.style = Paint.Style.STROKE

        textPaint = Paint()
        textPaint.color = Color.RED
        textPaint.style = Paint.Style.FILL
        textPaint.textSize = 80f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
//        canvas.drawText(text, rect.centerX().toFloat(), rect.centerY().toFloat(), textPaint)
//        canvas.drawRect(( rect.left.toFloat()*_scaleX)-50, rect.top.toFloat()*_scaleY - 50, (rect.right.toFloat()*_scaleX)+50, rect.bottom.toFloat()*_scaleY + 50, paint)
        canvas.drawRect( (50).toFloat(), (150).toFloat(), (width-50).toFloat(), (150+(width-100)).toFloat(), paint)
    }
}

/*
private class FaceAnalyzer(private val listener: FaceListener) : ImageAnalysis.Analyzer {

    companion object {
        var S: String = ""
    }

    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            // Pass image to an ML Kit Vision API
            // ...
            /*
            val highAccuracyOpts = FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                .build()
            */

            val rotation = imageProxy.imageInfo.rotationDegrees
            val inputImage = InputImage.fromMediaImage(mediaImage, rotation)

            val detector = FaceDetection.getClient();
            val result = detector.process(image)
                .addOnSuccessListener { faces: List<Face> ->
                    //val listener = listener ?: return@addOnSuccessListener

                    // In order to correctly display the face bounds, the orientation of the analyzed
                    // image and that of the viewfinder have to match. Which is why the dimensions of
                    // the analyzed image are reversed if its rotation information is 90 or 270.

                    //val reverseDimens = rotation == 90 || rotation == 270
                    //val width = if (reverseDimens) imageProxy.height else imageProxy.width
                    //val height = if (reverseDimens) imageProxy.width else imageProxy.height
                    //val faceBounds = faces.map { it.boundingBox.transform(width, height) }
                    //listener.onFacesDetected(faceBounds)

                    for(face in faces) {
                        val bounds = face.boundingBox
                        val rotY = face.headEulerAngleY // Head is rotated to the right rotY degrees
                        val rotZ = face.headEulerAngleZ // Head is tilted sideways rotZ degrees

                        S = "Face Detected"
                        Log.v("TESTING", "Face at ${bounds.top} ${bounds.left} ${bounds.bottom} ${bounds.right}")
                    }
                }
                .addOnFailureListener { }
                .addOnCompleteListener { imageProxy.close() }

        }
    }

    /*
    internal interface Listener {
        /** Callback that receives face bounds that can be drawn on top of the viewfinder.  */
        fun onFacesDetected(faceBounds: List<RectF>)

        /** Invoked when an error is encounter during face detection.  */
        fun onError(exception: Exception)
    }

     */
}
*/


/*
class QrAnalyzer(private val listener: QrListener) : ImageAnalysis.Analyzer {

    companion object {
        var S: String = ""
    }

    private fun ByteBuffer.toByteArray(): ByteArray {
        rewind()    // Rewind the buffer to zero
        val data = ByteArray(remaining())
        get(data)   // Copy the buffer into a byte array
        return data // Return the byte array
    }

    @SuppressLint("UnsafeExperimentalUsageError")
    override fun analyze(image: ImageProxy) {
        val mediaImage = image.image
        if (mediaImage != null) {
            val img = InputImage.fromMediaImage(mediaImage, image.imageInfo.rotationDegrees)
            // Pass image to an ML Kit Vision API
            // ...
            val options = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(
                    Barcode.FORMAT_ALL_FORMATS
                )
                .build()

            val scanner = BarcodeScanning.getClient(options)
            val result = scanner.process(img)
                .addOnSuccessListener { barcodes ->
                    // Task completed successfully
                    // [START_EXCLUDE]
                    // [START get_barcodes]
                    for (barcode in barcodes) {
                        val bounds = barcode.boundingBox
                        val corners = barcode.cornerPoints

                        val rawValue = barcode.rawValue

                        val valueType = barcode.valueType
                        // See API reference for complete list of supported types
                        Log.v("TESTING: ", "Barcode detected: $rawValue")
                        S = rawValue.toString()
                    }
                }
                .addOnFailureListener { }
                .addOnCompleteListener { image.close() }
        }
    }
}

 */