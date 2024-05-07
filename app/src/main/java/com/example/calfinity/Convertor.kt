package com.example.calfinity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.compose.ui.text.toUpperCase
import kotlin.math.pow

class Convertor : AppCompatActivity() {
    private var unit1In = 0
    private var unit2In = 0
    private var convMode = ""
    private lateinit var iptv : TextView
    private lateinit var optv : TextView

    // Conversion Factors
    private var area = arrayOf(
        listOf(1.0, 0.01, (10.0).pow(-6), (10.0).pow(-12), 0.00155, 0.000011, 386*(10.0).pow(-15), 247105*(10.0).pow(-15)),
        listOf(100.0, 1.0, (10.0).pow(-4), (10.0).pow(-10), 0.155, 0.001076, 3861*(10.0).pow(-14), 24710538*(10.0).pow(-15)),
        listOf((10.0).pow(6), (10.0).pow(4), 1.0, (10.0).pow(-6), 1550.003, 10.76391, 386102159*(10.0).pow(-15), 0.00024710538),
        listOf((10.0).pow(12), (10.0).pow(10), (10.0).pow(6), 1.0, 1550003100.0, 10763910.0, 0.386102, 247.10538),
        listOf(645.16, 6.4516, 6.4516*(10.0).pow(-4), 6.4516*(10.0).pow(-10), 1.0, 0.006944, 249098*(10.0).pow(-15), 159422508*(10.0).pow(-15)),
        listOf(92903.04, 929.0304, 0.09290304, 9.290304*(10.0).pow(-8), 144.0, 1.0, 3.5870064*(10.0).pow(-8), 0.000023),
        listOf(2589988110336.0, 25899881103.36, 2589988.110336, 2.589988110336, 4014489600.0, 27878400.0, 1.0, 640.0),
        listOf(4046856422.0, 40468564.22, 4046.856422, 0.004046856422, 6272640.0, 43560.0, 0.0015625, 1.0)
    )
    private var data = arrayOf(
        listOf(1.0, 0.125, 0.001, 0.000125, 0.000001, 0.000000125, 0.000000001, 0.000000000125),
        listOf(8.0, 1.0, 0.008, 0.001, 0.000008, 0.000001, 0.000000008, 0.000000001),
        listOf(1000.0, 125.0, 1.0, 0.125, 0.001, 0.000125, 0.000001, 0.000000125),
        listOf(8000.0, 1000.0, 8.0, 1.0, 0.008, 0.001, 0.000008, 0.000001),
        listOf(1000000.0, 125000.0, 1000.0, 125.0, 1.0, 0.125, 0.001, 0.000125),
        listOf(8000000.0, 1000000.0, 8000.0, 1000.0, 8.0, 1.0, 0.008, 0.001),
        listOf(1000000000.0, 125000000.0, 1000000.0, 125000.0, 1000.0, 125.0, 1.0, 0.125),
        listOf(8000000000.0, 1000000000.0, 8000000.0, 1000000.0, 8000.0, 1000.0, 8.0, 1.0)
    )
    private var length = arrayOf(
        listOf(1.0, 0.001, 0.000001, 0.0000001, 0.000000001, (10.0).pow(-12), 3.9370079*(10.0).pow(-8), 3.28084*(10.0).pow(-9), 6.21*(10.0).pow(-13)),
        listOf(1000.0, 1.0, 0.001, 0.0001, 0.000001, 0.000000001, 0.000039, 0.000003 ,6.21371*(10.0).pow(-10)),
        listOf(1000000.0, 1000.0, 1.0, 0.1, 0.001, 0.000001, 0.03937008, 0.00328084, 6.21371192*(10.0).pow(-7)),
        listOf(10000000.0, 10000.0, 10.0, 1.0, 0.01, 0.00001, 0.3937008, 0.0328084, 0.000006213),
        listOf((10.0).pow(9), 1000000.0, 1000.0, 100.0, 1.0, 0.001, 39.37008, 3.28084, 0.000621371),
        listOf((10.0).pow(12), (10.0).pow(9), 1000000.0, 100000.0, 1000.0, 1.0, 39370.08, 3280.84, 0.621371192),
        listOf(25400000.0, 25400.0, 25.4, 2.54, 0.0254, 0.000025, 1.0, 0.083333, 0.00001578),
        listOf(3.048*(10.0).pow(8), 304800.0, 304.8, 30.48, 0.3048, 0.0003048, 12.0, 1.0, 0.00018939),
        listOf(1.609344*(10.0).pow(12), 1.609344*(10.0).pow(9), 1609344.0, 160934.4, 1609.344, 1.609344, 63360.0, 5280.0, 1.0)
    )
    private var mass = arrayOf(
        listOf(1.0, 0.1, 0.001, 0.000001, 0.0000022046, 0.000000001),
        listOf(10.0, 1.0, 0.01, 0.00001, 0.0000220462, 0.00000001),
        listOf(1000.0, 100.0, 1.0, 0.001, 0.0022046226, 0.000001),
        listOf(1000000.0, 100000.0, 1000.0, 1.0, 2.2046226218, 0.001),
        listOf(453592.37, 45359.237, 453.592437, 0.453592437, 1.0, 0.000454),
        listOf((10.0).pow(9), (10.0).pow(8), 1000000.0, 1000.0, 2204.6226218, 1.0)
    )
    private var speed = arrayOf(
        listOf(1.0, 3.6, 2.237136, 1.94401244, 0.00293858),
        listOf(0.277777, 1.0, 0.6214268, 0.540003, 0.000816),
        listOf(0.447, 1.6092, 1.0, 0.8689736, 0.0013135),
        listOf(51.44, 185.184, 1.150783, 1.0, 0.0015116),
        listOf(340.3, 1225.08, 761.2975, 661.5474, 1.0)
    )

    private var time = arrayOf(
        listOf(1.0, 0.001, 0.000001, 0.000001*1.0/60, 0.000001*1.0/(60*60), 0.000001*1.0/(60*60*24), 0.000001*1.0/(60*60*24*7), 0.000001*1.0/(60*60*24*365)),
        listOf(1000.0, 1.0, 0.001, 0.001*1.0/60, 0.001*1.0/(60*60), 0.001*1.0/(60*60*24), 0.001*1.0/(60*60*24*7), 0.001*1.0/(60*60*24*365)),
        listOf(1000000.0, 1000.0, 1.0, 1.0/60, 1.0/(60*60), 1.0/(60*60*24), 1.0/(60*60*24*7), 1.0/(60*60*24*365)),
        listOf(1000000.0*60, 1000.0*60, 60.0, 1.0, 1.0/60, 1.0/(60*24), 1.0/(60*24*7), 1.0/(60*24*365)),
        listOf(1000000.0*60*60, 1000.0*60*60, 60.0*60, 60.0, 1.0, 1.0/24, 1.0/(24*7), 1.0/(24*365)),
        listOf(1000000.0*60*60*24, 1000.0*60*60*24, 60.0*60*24, 60.0*24, 24.0, 1.0, 1.0/7, 1.0/365),
        listOf(1000000.0*60*60*24*7, 1000.0*60*60*24*7, 60.0*60*24*7, 60.0*24*7, 24.0*7, 7.0, 1.0, 7.0/365),
        listOf(1000000.0*60*60*24*365, 1000.0*60*60*24*365, 60.0*60*24*365, 60.0*24*365, 24.0*365, 365.0, 365.0/7, 1.0)
    )
    private var volume = arrayOf(
        listOf(1.0, 0.001, 1.0, 0.000001, 0.0610237, 0.000035),
        listOf(1000.0, 1.0, 1000.0, 0.001, 61.023744, 0.035315),
        listOf(1.0, 0.001, 1.0, 0.000001, 0.0610237, 0.000035),
        listOf(1000000.0, 1000.0, 1000000.0, 1.0, 61023.7441, 35.31467),
        listOf(16.38706, 0.01638706, 16.38706, 0.000016, 1.0, 0.0005787),
        listOf(28316.8466, 28.3168466, 28316.8466, 0.02831685, 1728.0, 1.0)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Setting Main Convertor View
        setContentView(R.layout.convertor)

        // Declaring Objects
        iptv = findViewById(R.id.convVal1)
        optv = findViewById(R.id.convVal2)
        val modeChange = findViewById<ImageView>(R.id.mode)
        modeChange.setOnClickListener{
            val i = Intent(this, Calculator::class.java)
            startActivity(i)
        }

        // Converter Mode Spinner
        val convSpinner = findViewById<Spinner>(R.id.convModeSpinner)
        // Unit Spinners
        val unit1Spinner = findViewById<Spinner>(R.id.val1Unit)
        val unit2Spinner = findViewById<Spinner>(R.id.val2Unit)

        // Creating ArrayAdapters for unit spinners
        val areaUnits = ArrayAdapter.createFromResource(this, R.array.Area, android.R.layout.simple_spinner_item)
        areaUnits.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val dataUnits = ArrayAdapter.createFromResource(this, R.array.Data, android.R.layout.simple_spinner_item)
        dataUnits.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val lenUnits = ArrayAdapter.createFromResource(this, R.array.Length, android.R.layout.simple_spinner_item)
        lenUnits.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val massUnits = ArrayAdapter.createFromResource(this, R.array.Mass, android.R.layout.simple_spinner_item)
        massUnits.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val numSysUnits = ArrayAdapter.createFromResource(this, R.array.NumberSystem, android.R.layout.simple_spinner_item)
        numSysUnits.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val speedUnits = ArrayAdapter.createFromResource(this, R.array.Speed, android.R.layout.simple_spinner_item)
        speedUnits.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val tempUnits = ArrayAdapter.createFromResource(this, R.array.Temperature, android.R.layout.simple_spinner_item)
        tempUnits.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val timeUnits = ArrayAdapter.createFromResource(this, R.array.Time, android.R.layout.simple_spinner_item)
        timeUnits.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val volUnits = ArrayAdapter.createFromResource(this, R.array.Volume, android.R.layout.simple_spinner_item)
        volUnits.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Setting Elements for Spinner from R.values.strings.xml
        val convModeArray = ArrayAdapter.createFromResource(this, R.array.convType, android.R.layout.simple_spinner_item)
        convModeArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        convSpinner.adapter = convModeArray

        // Defining onItemSelectedListener extension function for Converter Mode Spinner
        convSpinner.setOnItemSelectedListener(object: Activity(), AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos:Int, id:Long) {
                // Creating and setting unit spinners
                reset()
                convMode = parent.getItemAtPosition(pos).toString()
                when (convMode) {
                    "Area" -> {
                        unit1Spinner.adapter = areaUnits
                        unit2Spinner.adapter = areaUnits
                    }
                    "Data" -> {
                        unit1Spinner.adapter = dataUnits
                        unit2Spinner.adapter = dataUnits
                    }
                    "Length" -> {
                        unit1Spinner.adapter = lenUnits
                        unit2Spinner.adapter = lenUnits
                    }
                    "Mass" -> {
                        unit1Spinner.adapter = massUnits
                        unit2Spinner.adapter = massUnits
                    }
                    "Number System" ->{
                        unit1Spinner.adapter = numSysUnits
                        unit2Spinner.adapter = numSysUnits
                    }
                    "Speed" -> {
                        unit1Spinner.adapter = speedUnits
                        unit2Spinner.adapter = speedUnits
                    }
                    "Temperature" -> {
                        unit1Spinner.adapter = tempUnits
                        unit2Spinner.adapter = tempUnits
                    }
                    "Time" -> {
                        unit1Spinner.adapter = timeUnits
                        unit2Spinner.adapter = timeUnits
                    }
                    "Volume" -> {
                        unit1Spinner.adapter = volUnits
                        unit2Spinner.adapter = volUnits
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        })
        // Defining onItemSelectedListener extension function for unit spinners
        unit1Spinner.setOnItemSelectedListener(object: Activity(), AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos:Int, id:Long) {
                unit1In = pos
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        })
        unit2Spinner.setOnItemSelectedListener(object: Activity(), AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos:Int, id:Long) {
                unit2In = pos
                valConvert(iptv.text.toString().toDouble())
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        })
    }

    // onClick Functions
    fun onNumberClick(view: View) {
        if (view is Button) {
            if (iptv.text.toString() == "0")
                iptv.text = view.text
            else
                iptv.append(view.text)
            var fromVal = iptv.text.toString()
            if (fromVal.last()== '.')
                fromVal += "0"
            valConvert(fromVal.toDouble())
        }
    }
    fun onDelClick(view: View) {
        val ipLen = iptv.text.length
        if (ipLen == 1)
            iptv.text = "0"
        else if (ipLen > 0)
            iptv.text = iptv.text.subSequence(0, ipLen-1)
        valConvert(iptv.text.toString().toDouble())
    }
    fun onAllClearClick(view: View) {
        reset()
    }

    // Function to convert value
    private fun valConvert(fromVal:Double){
        var convVal = ""
        when (convMode){
            "Area" -> { convVal = (fromVal * area[unit1In][unit2In]).toString() }
            "Data" -> { convVal = (fromVal * data[unit1In][unit2In]).toString() }
            "Length" -> { convVal = (fromVal * length[unit1In][unit2In]).toString() }
            "Mass" -> { convVal = (fromVal * mass[unit1In][unit2In]).toString() }
            "Number System" -> {
                try {
                    var value = convertType(fromVal.toString())
                    if (unit1In == unit2In) {
                        convVal = fromVal.toString()
                    } else if (unit1In == 0) {
                        if (unit2In == 1) {
                            val nDec = value.toInt(2).toString()
                            val nOct = Integer.toOctalString(nDec.toInt())
                            convVal = nOct
                        } else if (unit2In == 2) {
                            val nDec = value.toInt(2).toString()
                            convVal = nDec
                        } else if (unit2In == 3) {
                            val nDec = value.toInt(2).toString()
                            val nHex = Integer.toHexString(nDec.toInt()).toUpperCase()
                            convVal = nHex
                        }
                    } else if (unit1In == 1) {
                        if (unit2In == 0) {
                            val nDec = value.toInt(8).toString()
                            val nBin = Integer.toBinaryString(nDec.toInt())
                            convVal = nBin
                        } else if (unit2In == 2) {
                            val nDec = value.toInt(8).toString()
                            convVal = nDec
                        } else if (unit2In == 3) {
                            val nDec = value.toInt(8).toString()
                            val nHex = Integer.toHexString(nDec.toInt()).toUpperCase()
                            convVal = nHex
                        }
                    } else if (unit1In == 2) {
                        if (unit2In == 0) {
                            val nBin = Integer.toBinaryString(value.toInt())
                            convVal = nBin
                        } else if (unit2In == 1) {
                            val nOct = Integer.toOctalString(value.toInt())
                            convVal = nOct
                        } else if (unit2In == 3) {
                            val nHex = Integer.toHexString(value.toInt()).toUpperCase()
                            convVal = nHex
                        }
                    } else if (unit1In == 3) {
                        if (unit2In == 0) {
                            val nDec = value.toInt(16).toString()
                            val nBin = Integer.toBinaryString(nDec.toInt())
                            convVal = nBin
                        } else if (unit2In == 1) {
                            val nDec = value.toInt(16).toString()
                            val nOct = Integer.toOctalString(nDec.toInt())
                            convVal = nOct
                        } else if (unit2In == 2) {
                            val nDec = value.toInt(16).toString()
                            convVal = nDec
                        }
                    }
                } catch(e:Exception){
                    Toast.makeText(this,"Enter valid no.",Toast.LENGTH_SHORT).show()
                    //Log.d("Error", e.stackTraceToString())
                    reset()
                }
            }
            "Speed" -> { convVal = (fromVal * speed[unit1In][unit2In]).toString() }
            "Temperature" -> {
                if (unit1In == unit2In) {
                    convVal = fromVal.toString()
                } else if (unit1In == 0) {
                    if (unit2In == 1)
                        convVal = (fromVal * 9.0 / 5 + 32).toString()
                    else if (unit2In == 2)
                        convVal = (fromVal + 273.15).toString()
                } else if (unit1In == 1) {
                    if (unit2In == 0)
                        convVal = ((fromVal - 32)* 5.0 / 9).toString()
                    else if (unit2In == 2)
                        convVal = ((fromVal - 32)* 5.0 / 9 + 273.15).toString()
                } else if (unit1In == 2) {
                    if (unit2In == 0)
                        convVal = (fromVal - 273.15).toString()
                    else if (unit2In == 1)
                        convVal = ((fromVal - 273.15) * 9.0 / 5 + 32).toString()
                }
            }
            "Time" -> { convVal = (fromVal * time[unit1In][unit2In]).toString() }
            "Volume" -> { convVal = (fromVal * volume[unit1In][unit2In]).toString() }
        }
        optv.text = convertType(convVal)
    }

    private fun convertType(n: String):String{
        return if (n.contains('.')) {
                    if (n.substring(n.indexOf('.') + 1, n.length) != "0")
                        n
                    else
                        n.substring(0, n.length - 2).toInt().toString()
                } else {
                    n
                }
    }
    private fun reset(){
        iptv.text = "0"
        optv.text = "0"
    }
}