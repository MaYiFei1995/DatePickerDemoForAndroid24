package com.mai.datepickerdemo

import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.DatePicker
import java.lang.Exception
import java.lang.reflect.Field

/*
 * Force spinners on Android 7.0 only (SDK 24).
 */
class DatePickerDialogForAndroid24(
    context: Context,
    listener: OnDateSetListener?,
    year: Int,
    monthOfYear: Int,
    dayOfMonth: Int
) : DatePickerDialog(context, listener, year, monthOfYear, dayOfMonth) {

    init {
        if (Build.VERSION.SDK_INT == 24) {
            try {
                val field = findField(
                    DatePickerDialog::class.java,
                    DatePicker::class.java,
                    "mDatePicker"
                )
                val datePicker = field.get(this) as DatePicker
                val delegateClass =
                    Class.forName("android.widget.DatePicker\$DatePickerDelegate")
                val delegateField = findField(DatePicker::class.java, delegateClass, "mDelegate")

                val delegate = delegateField.get(datePicker)
                val spinnerDelegateClass = Class.forName("android.widget.DatePickerSpinnerDelegate")

                if (delegate.javaClass != spinnerDelegateClass) {
                    delegateField.set(datePicker, null)
                    datePicker.removeAllViews()

                    val spinnerDelegateConstructor = spinnerDelegateClass.getDeclaredConstructor(
                        DatePicker::class.java,
                        Context::class.java,
                        AttributeSet::class.java,
                        Int::class.java,
                        Int::class.java
                    )
                    spinnerDelegateConstructor.isAccessible = true
                    val spinnerDelegate = spinnerDelegateConstructor.newInstance(
                        datePicker,
                        context,
                        null,
                        android.R.attr.datePickerStyle,
                        0
                    )
                    delegateField.set(datePicker, spinnerDelegate)

                    datePicker.init(year, monthOfYear, dayOfMonth, this)
                    datePicker.calendarViewShown = false
                    datePicker.spinnersShown = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        else{
            throw Exception("Not Android 7.0 Device")
        }
    }

    /**
     * Find Field with expectedName in objectClass. If not found, find first occurrence of
     * target fieldClass in objectClass.
     */
    private fun findField(
        objectClass: Class<*>,
        fieldClass: Class<*>,
        expectedName: String
    ): Field {
        try {
            val field = objectClass.getDeclaredField(expectedName)
            field.isAccessible = true
            return field
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        }

        // Search for it if it wasn't found under the expectedName.
        for (field in objectClass.declaredFields) {
            if (field.type === fieldClass) {
                field.isAccessible = true
                return field
            }
        }
        throw Exception("On find field expectedName $expectedName error.. return null")
    }
}