package com.mib.feature_home.utils

import android.content.*
import android.content.res.Resources
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.and


class AppUtils {

    companion object {
        fun isNetworkAvailable(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val activeNetwork = cm.activeNetwork ?: return false
                val networkCapabilities = cm.getNetworkCapabilities(activeNetwork) ?: return false
                return when {
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } else {
                val activeNetwork = cm.activeNetworkInfo
                return activeNetwork != null && activeNetwork.isConnectedOrConnecting
            }
        }

        fun hideKeyboard(view: View, context: Context) {
            try {
                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            } catch (ignored: Exception) {
            }
        }

        fun showKeyboard(view: EditText, context: Context) {
            try {
                view.requestFocus()
                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
            } catch (ignored: Exception) {
            }
        }

        fun copyToClipboard(context: Context, text: CharSequence){
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("label", text)
            clipboard.setPrimaryClip(clip)
        }

        fun isLastItemDisplaying(recyclerView: RecyclerView): Boolean {
            if (recyclerView.adapter!!.itemCount != 0) {
                val lastVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.adapter!!.itemCount - 1) return true
            }
            return false
        }

        fun firstSetRecyclerView(context: Context, orientation: Int, rv: RecyclerView) {
            rv.setHasFixedSize(true)
            rv.setItemViewCacheSize(20)
            rv.layoutManager = LinearLayoutManager(context, orientation, false)
            rv.itemAnimator = DefaultItemAnimator()
        }

        fun firstSetRecyclerViewGrid(context: Context, rv: RecyclerView) {
            val gridLayoutManager = GridLayoutManager(context, 3)

            rv.setHasFixedSize(true)
            rv.setItemViewCacheSize(20)
            rv.layoutManager = gridLayoutManager
            rv.itemAnimator = DefaultItemAnimator()
        }

        fun setIdr(amount: Double): String? {
            val localeID = Locale("in", "ID")
            val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
            return formatRupiah.format(amount)
        }

        fun orderDatePicker(datePicker: DatePicker, ymdOrder: CharArray) {
            val idYear: Int = Resources.getSystem().getIdentifier("year", "id", "android")
            val idMonth: Int = Resources.getSystem().getIdentifier("month", "id", "android")
            val idDay: Int = Resources.getSystem().getIdentifier("day", "id", "android")
            val idLayout: Int = Resources.getSystem().getIdentifier("pickers", "id", "android")

            val spinnerYear: NumberPicker = datePicker.findViewById(idYear) as NumberPicker
            val spinnerMonth: NumberPicker = datePicker.findViewById(idMonth) as NumberPicker
            val spinnerDay: NumberPicker = datePicker.findViewById(idDay) as NumberPicker
            val layout: LinearLayout = datePicker.findViewById(idLayout) as LinearLayout
            layout.removeAllViews()
            for (i in 0..2) {
                when (ymdOrder[i]) {
                    'y' -> {
                        layout.addView(spinnerYear)
                        setImeOptionsDatePicker(spinnerYear, i)
                    }
                    'm' -> {
                        layout.addView(spinnerMonth)
                        setImeOptionsDatePicker(spinnerMonth, i)
                    }
                    'd' -> {
                        layout.addView(spinnerDay)
                        setImeOptionsDatePicker(spinnerDay, i)
                    }
                    else -> throw java.lang.IllegalArgumentException("Invalid char[] ymdOrder")
                }
            }
        }

        fun orderMonthYearPicker(datePicker: DatePicker, ymdOrder: CharArray) {
            val idYear: Int = Resources.getSystem().getIdentifier("year", "id", "android")
            val idMonth: Int = Resources.getSystem().getIdentifier("month", "id", "android")
//            val idDay: Int = Resources.getSystem().getIdentifier("day", "id", "android")
            val idLayout: Int = Resources.getSystem().getIdentifier("pickers", "id", "android")

            val spinnerYear: NumberPicker = datePicker.findViewById(idYear) as NumberPicker
            val spinnerMonth: NumberPicker = datePicker.findViewById(idMonth) as NumberPicker
//            val spinnerDay: NumberPicker = datePicker.findViewById(idDay) as NumberPicker
            val layout: LinearLayout = datePicker.findViewById(idLayout) as LinearLayout
            layout.removeAllViews()
            for (i in 0..1) {
                when (ymdOrder[i]) {
                    'y' -> {
                        layout.addView(spinnerYear)
                        setImeOptionsDatePicker(spinnerYear, i)
                    }
                    'm' -> {
                        layout.addView(spinnerMonth)
                        setImeOptionsDatePicker(spinnerMonth, i)
                    }
//                    'd' -> {
//                        layout.addView(spinnerDay)
//                        setImeOptionsDatePicker(spinnerDay, i)
//                    }
                    else -> throw java.lang.IllegalArgumentException("Invalid char[] ymdOrder")
                }
            }
        }

        private fun setImeOptionsDatePicker(spinner: NumberPicker, spinnerIndex: Int) {
            val imeOptions: Int
            imeOptions = if (spinnerIndex < 2) {
                EditorInfo.IME_ACTION_NEXT
            } else {
                EditorInfo.IME_ACTION_DONE
            }
            val idPickerInput = Resources.getSystem().getIdentifier("numberpicker_input", "id", "android")
            val input = spinner.findViewById(idPickerInput) as TextView
            input.imeOptions = imeOptions
        }

        fun updatePlaystore(context: Context) {
            val appPackageName = context.packageName // getPackageName() from Context or Activity object
            try {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
            } catch (anfe: ActivityNotFoundException) {
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
            }
        }

        fun checkAndPhoneNumberCorrection(phoneNumber: String): String {
            var result: String = ""

            if(phoneNumber.substring(0, 1) == "0") {
                result =  "62" + phoneNumber.substring(1)
            } else if(phoneNumber.substring(0, 2) == "62") {
                result =  "62" + phoneNumber.substring(2)
            } else if(phoneNumber.substring(0, 3) == "+62") {
                result =  "62" + phoneNumber.substring(3)
            } else {
                result = "62$phoneNumber"
            }

            return result
        }

        fun directionsGoogleMap(mContext: Context, latitude: String, longitude: String) {
            val uri = Uri.parse("http://maps.google.com/maps?daddr=$latitude,$longitude")
//            val uri = Uri.parse("http://maps.google.com/maps?saddr=-6.163446446875413, 106.72119750605988&daddr=$latitude,$longitude")
            val mapIntent = Intent(Intent.ACTION_VIEW, uri)
            mapIntent.setPackage("com.google.android.apps.maps")
            mapIntent.resolveActivity(mContext.packageManager)?.let {
                mContext.startActivity(mapIntent)
            }
        }

        fun formatDatetimeToDate(oriDate: String): String {
            return if(oriDate != "") {
                val input = SimpleDateFormat("dd MMM yyyy hh:mm:ss", Locale.getDefault())
                val output = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

                output.format(input.parse(oriDate)!!)
            } else {
                "-"
            }
        }

        fun getTodaysDate(): String {
            val c = Calendar.getInstance().time

            val df = SimpleDateFormat("dd MMM", Locale.getDefault())
            return df.format(c)
        }

        fun createRequestBody(value: String?): RequestBody? {
            return value?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        }

//        var mBottomSheetDialog: BottomSheetDialog? = null
//        fun unAuthorized(context: Context, errorMessage: String?) {
//            val mDialogUtils = DialogUtils(context)
//            mBottomSheetDialog = BottomSheetDialog(context)
//            mBottomSheetDialog!!.setOnCancelListener {
//                SharedPreferenceHelper.logoutApps(context)
//            }
//
//            mDialogUtils.showDialogError(context, mBottomSheetDialog!!, context.getString(R.string.problem), context.getString(R.string.invalid_credential), context.getString(R.string.logout),
//                ContextCompat.getDrawable(context, R.drawable.ic_access_denied), object : DialogListener {
//                    override fun onSubmitClicked(message: String) {
//                        SharedPreferenceHelper.logoutApps(context)
//                    }
//                })
//        }

        fun convertMillisToDate(dateInMillis: Long?): String {
            if(dateInMillis == null) return "-"
            val dfDay = SimpleDateFormat("dd", Locale.getDefault())
            val dfMonth = SimpleDateFormat("MM", Locale.getDefault())
            val dfYear = SimpleDateFormat("yyyy", Locale.getDefault())

            var date = dateInMillis*1000
            val day = convertDate(dfDay.format(Date(date)).toInt())
            val month = convertDate(dfMonth.format(Date(date)).toInt())
            val year = convertDate(dfYear.format(Date(date)).toInt())
            return "$day-$month-$year"
        }

        fun convertDate(input: Int): String {
            return if (input >= 10) {
                input.toString()
            } else {
                "0$input"
            }
        }

        fun isPricePromo(promoPrice: String) : Boolean {
            var isPromo = false

            isPromo = promoPrice != "-1"

            return isPromo
        }

        fun removePointString(ori: String): String? {
            return ori.replace(".", "")
        }

        fun formatDateWithDayName(ori: String): String? {
            if(ori != "") {
                val input = SimpleDateFormat("yyyy-MM-dd", Locale.US)

                val output = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                val sdf = SimpleDateFormat("EEEE", Locale.getDefault())

                return "${sdf.format(input.parse(ori)!!)}, ${output.format(input.parse(ori)!!)}"
            }

            return "-"
        }

        fun goToWhatsApp(context: Context, number: String) {
            val url = "https://api.whatsapp.com/send?phone=$number"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            context.startActivity(i)
        }

        fun encode(message: String, key: String): String {
            val hashingAlgorithm = "HmacSHA256" //or "HmacSHA1", "HmacSHA512"
            val bytes = hmac(hashingAlgorithm, key.toByteArray(), message.toByteArray())
            return bytesToHex(bytes)
        }

        @Throws(NoSuchAlgorithmException::class, InvalidKeyException::class)
        private fun hmac(algorithm: String?, key: ByteArray?, message: ByteArray?): ByteArray {
            val mac = Mac.getInstance(algorithm)
            mac.init(SecretKeySpec(key, algorithm))
            return mac.doFinal(message)
        }

        private fun bytesToHex(bytes: ByteArray): String {
            val hexArray = "0123456789abcdef".toCharArray()
            val hexChars = CharArray(bytes.size * 2)
            var j = 0
            var v: Int
            while (j < bytes.size) {
                v = bytes[j] and 0xFF
                hexChars[j * 2] = hexArray[v ushr 4]
                hexChars[j * 2 + 1] = hexArray[v and 0x0F]
                j++
            }
            return String(hexChars)
        }
//        @Throws(java.lang.Exception::class)
//        fun encode(key: String, data: String): String? {
//            val sha256_HMAC = Mac.getInstance("HmacSHA256")
//            val secret_key = SecretKeySpec(key.toByteArray(charset("UTF-8")), "HmacSHA256")
//            sha256_HMAC.init(secret_key)
//            val string = Hex.encodeHexString(sha256_HMAC.doFinal(data.toByteArray(charset("UTF-8"))))
//            return string
//        }
        const val KEY_HASH_PASSWORD = "hash_password"

    }

}