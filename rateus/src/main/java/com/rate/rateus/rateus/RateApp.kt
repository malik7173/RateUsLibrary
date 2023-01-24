package com.rate.rateus.rateus

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBar

class RateApp(var context: Context) {

    var editor: SharedPreferences.Editor
    private var pref: SharedPreferences = context.getSharedPreferences("RateAppPreference", 0)

    init {
        editor = pref.edit()
    }

    private var isAlreadyRated: Boolean
        get() = pref.getBoolean(IS_RATED, false)
        set(isAlreadyRated) {
            editor.putBoolean(IS_RATED, isAlreadyRated)
            editor.apply()
        }


    fun ratingDialog(
        unSelectedStar: Int,
        selectedStar: Int,
        email: String,
        packageName: String
    ) {

        var isRated = 0
        if (!isAlreadyRated) {
            try {
                val dialog = Dialog(context)
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setContentView(R.layout.rating_dialog) //get layout from ExitDialog folder
                dialog.window!!.setLayout(
                    ActionBar.LayoutParams.MATCH_PARENT,
                    ActionBar.LayoutParams.MATCH_PARENT
                )
                if (dialog.window != null) {
                    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                }

                val btnYes = dialog.findViewById<LinearLayout>(R.id.btnExit)
                val btnNo = dialog.findViewById<LinearLayout>(R.id.btnCancel)


                val rateTxt = dialog.findViewById<TextView>(R.id.rateTxt)
                val rateDesc = dialog.findViewById<TextView>(R.id.rateDescription)
                val llBars = dialog.findViewById<LinearLayout>(R.id.llBars)


                val ratingOne = dialog.findViewById<ImageView>(R.id.start1)
                val ratingTwo = dialog.findViewById<ImageView>(R.id.start2)
                val ratingThree = dialog.findViewById<ImageView>(R.id.start3)
                val ratingFour = dialog.findViewById<ImageView>(R.id.start4)
                val ratingFive = dialog.findViewById<ImageView>(R.id.start5)

                ratingOne.setOnClickListener {
                    ratingOne.setImageResource(selectedStar)
                    ratingTwo.setImageResource(unSelectedStar)
                    ratingThree.setImageResource(unSelectedStar)
                    ratingFour.setImageResource(unSelectedStar)
                    ratingFive.setImageResource(unSelectedStar)
                    isRated = 1
                }
                ratingTwo.setOnClickListener {
                    ratingOne.setImageResource(selectedStar)
                    ratingTwo.setImageResource(selectedStar)
                    ratingThree.setImageResource(unSelectedStar)
                    ratingFour.setImageResource(unSelectedStar)
                    ratingFive.setImageResource(unSelectedStar)
                    isRated = 2

                }
                ratingThree.setOnClickListener {
                    ratingOne.setImageResource(selectedStar)
                    ratingTwo.setImageResource(selectedStar)
                    ratingThree.setImageResource(selectedStar)
                    ratingFour.setImageResource(unSelectedStar)
                    ratingFive.setImageResource(unSelectedStar)

                    isRated = 3
                }
                ratingFour.setOnClickListener {
                    ratingOne.setImageResource(selectedStar)
                    ratingTwo.setImageResource(selectedStar)
                    ratingThree.setImageResource(selectedStar)
                    ratingFour.setImageResource(selectedStar)
                    ratingFive.setImageResource(unSelectedStar)
                    isRated = 4
                }
                ratingFive.setOnClickListener {
                    ratingOne.setImageResource(selectedStar)
                    ratingTwo.setImageResource(selectedStar)
                    ratingThree.setImageResource(selectedStar)
                    ratingFour.setImageResource(selectedStar)
                    ratingFive.setImageResource(selectedStar)
                    isRated = 5
                }

                btnNo.setOnClickListener { dialog.dismiss() }
                btnYes.setOnClickListener {

                    when (isRated) {
                        1, 2, 3 -> {
                            Handler(Looper.getMainLooper()).postDelayed({
                                sendMail(email)
                                dialog.dismiss()
                            }, 500)
                        }
                        4, 5 -> {
                            Handler(Looper.getMainLooper()).postDelayed({
                                context.startActivity(
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("market://details?id=$packageName")
                                    )
                                )
                                dialog.dismiss()
                            }, 500)
                        }
                        else -> {
                            Toast.makeText(
                                context,
                                "Please Select Rating First!",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            dialog.dismiss()
                        }
                    }
                }
                dialog.show()
            } catch (e: RuntimeException) {
                e.printStackTrace()
            }
        } else {
            Toast.makeText(
                context,
                "You already rated this app! Thanks",
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }


    private fun sendMail(email: String) {

        var deviceInfo = "Device Info:\n"
        deviceInfo += "\nBRAND: ${Build.BRAND} "
        deviceInfo += "\nOS Version: ${System.getProperty("os.version")}(${Build.VERSION.INCREMENTAL})"
        deviceInfo += "\nOS API Level: ${Build.VERSION.SDK_INT}"
        deviceInfo += "\nDevice: ${Build.DEVICE}"
        deviceInfo += "\nModel: ${Build.MODEL} "


        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:")
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        intent.putExtra(Intent.EXTRA_SUBJECT, "Review From : [Your Country]")
        intent.putExtra(Intent.EXTRA_TEXT, deviceInfo)
        context.startActivity(Intent.createChooser(intent, "Email via..."))
    }

    companion object {
        const val IS_RATED = "isAlreadyRated"
    }

}