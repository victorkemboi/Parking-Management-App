package com.park254.app.park254.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.ConnectivityManager
import android.os.Environment
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import com.google.android.gms.maps.model.LatLng
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.park254.app.park254.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.DateFormatSymbols
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


object UtilityClass {
    const val MAP_BUTTON_REQUEST_CODE = 301
    const val OPEN_DOCUMENT_FIRST_PHOTO_CODE = 302
    const val OPEN_DOCUMENT_SECOND_PHOTO_CODE = 303
    const val OPEN_DOCUMENT_THIRD_PHOTO_CODE = 304
    const val MAP_VIEW_BUNDLE_KEY = "305"
    const val REQUEST_LOCATION_PERMISSION_FOR_ENABLE_MY_LOCATION = 306
    const val REQUEST_LOCATION_PERMISSION_FOR_GET_DEVICE_LOCATION = 307
    const val REQUEST_CHECK_SETTINGS = 308
    const val HASH_MAP_UNIQUE_KEY = "wgwrvwrgerge43647423rgfrg34t35635"
    const val DEFAULT_ZOOM = (12).toFloat()
    const val BASE_URL: String = "https://park254.azurewebsites.net/v1/"
    const val DATABASE_NAME = "park254DB"
    const val BARCODE_READER_REQUEST_CODE = 309
    fun requestFocus(view: View, window: Window) {
        if (view.requestFocus()) {
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }

    fun hideKeyboard(activity: Activity) {
        val view = activity.currentFocus
        if (view != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun getStringTimeStampWithDate(date: Date): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("GMT")
        return dateFormat.format(date)
    }

    fun getDateWithServerTimeStamp(string: String): Calendar? {

        val cal: Calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSZ", Locale.US)
        sdf.timeZone = TimeZone.getDefault()

        cal.time = sdf.parse(string)

        return try {
            cal
        } catch (e: ParseException) {
            null
        }
    }

    fun getMonthForInt(num: Int): String {
        var month = "Jan"
        val dfs = DateFormatSymbols()
        val months = dfs.months
        if (num in 0..11) {
            month = months[num]
        }
        return month
    }

    fun getTimeDifference(string: String): String {

        val timeFromServer = getDateWithServerTimeStamp(string)!!.time!!.time

        val timeNow = Calendar.getInstance(TimeZone.getDefault()).time!!.time

        val timeDifference = timeNow - timeFromServer


        // Calculate difference in weeks
        val diffWeeks = timeDifference / (7 * 24 * 60 * 60 * 1000)
        if (diffWeeks > 0) {
            if (diffWeeks == (1).toLong()) {
                return "$diffWeeks week ago"
            }
            return "$diffWeeks weeks ago"
        }

        // Calculate difference in days
        val diffDays = timeDifference / (24 * 60 * 60 * 1000)
        if (diffDays > 0) {
            if (diffDays == (1).toLong()) {
                return "$diffDays day ago"
            }
            return "$diffDays days ago"
        }

        // Calculate difference in hours
        val diffHours = timeDifference / (60 * 60 * 1000)
        if (diffHours > 0) {
            if (diffHours == (1).toLong()) {
                return "$diffHours hour ago"
            }
            return "$diffHours hours ago"
        }


        // Calculate difference in minutes
        val diffMinutes = timeDifference / (60 * 1000)
        if (diffMinutes > 0) {
            if (diffMinutes == (1).toLong()) {
                return "$diffMinutes minute ago"
            }
            return "$diffMinutes minutes ago"
        }

        // Calculate difference in seconds
        val diffSeconds = timeDifference / 1000
        if (diffSeconds > 0) {


            if (diffSeconds == (1).toLong()) {
                return "$diffSeconds second ago"
            }
            return "$diffSeconds seconds ago"
        }








        return ""
    }

    fun timeAMorPM(calendar: Calendar): String {
        if (calendar.get(Calendar.AM_PM) == Calendar.AM) {
            return "AM"
        }
        return "PM"
    }

    fun addZeroForOneToNine(int: Int): String {
        if (int in 0 until 9) {
            return "0$int"
        }
        return int.toString()
    }


    @Throws(WriterException::class)
    fun encodeDataToQR(Value: String, context: Context): Bitmap? {
        val QRcodeWidth = 500
        val bitMatrix: BitMatrix
        try {
            bitMatrix = MultiFormatWriter().encode(
                    Value,
                    BarcodeFormat.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            )

        } catch (e: IllegalArgumentException) {

            return null
        }

        val bitMatrixWidth = bitMatrix.width

        val bitMatrixHeight = bitMatrix.height

        val pixels = IntArray(bitMatrixWidth * bitMatrixHeight)

        for (y in 0 until bitMatrixHeight) {
            val offset = y * bitMatrixWidth

            for (x in 0 until bitMatrixWidth) {

                pixels[offset + x] = if (bitMatrix.get(x, y))
                    ContextCompat.getColor(context,R.color.black)
                else
                    ContextCompat.getColor(context,R.color.white)
            }
        }
        val bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444)

        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight)
        return bitmap
    }


    fun saveImage(myBitmap: Bitmap, context: Context, activity: Activity): String {
        val bytes = ByteArrayOutputStream()
        val IMAGE_DIRECTORY = "/Park254/Media/Payments"
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
        val photoDirectory = File(
                Environment.getExternalStorageDirectory().path + IMAGE_DIRECTORY)
        // have the object build the directory structure, if needed.

        if (!photoDirectory.exists()) {
            //Log.d("dirrrrrr", "" + photoDirectory.mkdirs())
            photoDirectory.mkdirs()
        }

        try {
            val f = File(photoDirectory, "IMG-" + Calendar.getInstance().get(Calendar.YEAR) +
                    addZeroForOneToNine(Calendar.getInstance().get(Calendar.MONTH)) +
                    addZeroForOneToNine(Calendar.getInstance().get(Calendar.DATE)) +
                    "-" +
                    (1 until 100).random() + ".jpg")
            f.createNewFile()   //give read write permission
            val fo = FileOutputStream(f)
            fo.write(bytes.toByteArray())
            MediaScannerConnection.scanFile(context,
                    arrayOf(f.path),
                    arrayOf("image/jpeg"), null)
            fo.close()
            //Log.d("TAG", "File Saved::--->" + f.absolutePath)

            Snackbar.make(activity.window.decorView.rootView, "QR Code Saved Successfully.", Snackbar.LENGTH_LONG).show()

            return f.absolutePath
        } catch (e1: IOException) {
            e1.printStackTrace()
        }

        return ""

    }

    fun isOnline(context: Context): Boolean{
        val cm  =context.getSystemService(Context.CONNECTIVITY_SERVICE)  as ConnectivityManager
        val netInfo = cm.activeNetworkInfo

        return netInfo!=null && netInfo.isConnected
    }


    private fun IntRange.random() =
            Random().nextInt((endInclusive + 1) - start) + start

    fun  computeCentroid( points:List<LatLng>): LatLng {
    var latitude = 0.0
    var longitude = 0.0
    val n = points.size

    for ( point in points) {
        latitude += point.latitude
        longitude += point.longitude
    }

    return  LatLng(latitude/n, longitude/n)
}

}