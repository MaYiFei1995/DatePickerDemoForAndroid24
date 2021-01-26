package com.mai.datepickerdemo

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.Toast
import java.lang.Exception
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun onclick(v: View) {
        if (Build.VERSION.SDK_INT == 24) {
            val theme = when (v.id) {
                R.id.api24Traditional -> android.R.style.Theme_Dialog
                R.id.api24HoloDark -> android.R.style.Theme_Holo
                R.id.api24HoloLight -> android.R.style.Theme_Holo_Light
                else -> 0
            }
            if (theme != 0) {
                showDatePickerDialogApi24(theme)
                return
            }
        }
        val theme =
            when (v.id) {
                R.id.THEME_TRADITIONAL -> AlertDialog.THEME_TRADITIONAL
                R.id.THEME_HOLO_DARK -> AlertDialog.THEME_HOLO_DARK
                R.id.THEME_HOLO_LIGHT -> AlertDialog.THEME_HOLO_LIGHT
                R.id.THEME_DEVICE_DEFAULT_DARK -> AlertDialog.THEME_DEVICE_DEFAULT_DARK
                R.id.THEME_DEVICE_DEFAULT_LIGHT -> AlertDialog.THEME_DEVICE_DEFAULT_LIGHT
                R.id.Theme_Material_Dialog_Alert -> android.R.style.Theme_Material_Dialog_Alert
                R.id.Theme_Material_Light_Dialog_Alert -> android.R.style.Theme_Material_Light_Dialog_Alert
                R.id.Theme_DeviceDefault_Dialog_Alert -> android.R.style.Theme_DeviceDefault_Dialog_Alert
                R.id.Theme_DeviceDefault_Light_Dialog_Alert -> android.R.style.Theme_DeviceDefault_Light_Dialog_Alert
                else -> 0
            }
        showDatePickerDialog(theme)
    }

    /**
     * 不同Theme的效果预览
     */
    private fun showDatePickerDialog(theme: Int) {
        if (theme == 0) {
            Toast.makeText(this@MainActivity, "Unknown theme!", Toast.LENGTH_SHORT).show()
            return
        }
        try {
            val dialog = DatePickerDialog(
                this@MainActivity, theme, null,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            )
            dialog.create()
            dialog.getButton(DialogInterface.BUTTON_NEGATIVE)
                .visibility = View.GONE
            dialog.show()
        } catch (e: Exception) {
            showAlertDialog(e.toString())
            e.printStackTrace()
        }
    }

    /**
     * Api24下显示Spinner样式的Dialog
     * @param theme ContextThemeWrapper的theme与DialogTheme的对应关系如下
     *              android.R.style.Theme_Dialog -> AlertDialog.THEME_TRADITIONAL
     *              android.R.style.Theme_Holo -> AlertDialog.THEME_HOLO_DARK
     *              android.R.style.Theme_Holo_Light -> AlertDialog.THEME_HOLO_LIGHT
     */
    private fun showDatePickerDialogApi24(theme: Int) {
        val themeContext =
            ContextThemeWrapper(this@MainActivity, theme)
        try {
            val dialog = DatePickerDialogForAndroid24(
                themeContext,
                null,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            )
            dialog.create()
            dialog.getButton(DialogInterface.BUTTON_NEGATIVE).visibility = View.GONE
            dialog.show()
        } catch (e: Exception) {
            showAlertDialog(e.toString())
            e.printStackTrace()
        }
    }

    private fun showAlertDialog(e: String) {
        android.support.v7.app.AlertDialog.Builder(this@MainActivity)
            .setTitle("Err")
            .setMessage(e)
            .create()
            .show()
    }
}
