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
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import kotlin.math.ln
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.sqrt

class Calculator : AppCompatActivity() {
    private lateinit var inexp: TextView
    private lateinit var outans: TextView
    private var canInsertOperator = false
    private var canInsertPt = true
    private var digit = ""
    private var exp = mutableListOf<String>()
    private var allowUnary = true
    private var spFunOn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Setting default Main Calculator View
        setContentView(R.layout.calculator)

        // Declaring objects
        inexp = findViewById(R.id.exp)
        outans = findViewById(R.id.ans)
        val modeChange = findViewById<ImageView>(R.id.mode)
        modeChange.setOnClickListener{
            val i = Intent(this, Convertor::class.java)
            startActivity(i)
        }
        // Calculator Mode Spinner
        val calcSpinner = findViewById<Spinner>(R.id.calcModeSpinner)
        // Setting Elements for Spinner from R.values.strings.xml
        val calcModeArray = ArrayAdapter.createFromResource(this, R.array.calcType, android.R.layout.simple_spinner_item)
        calcModeArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        calcSpinner.adapter = calcModeArray

        // Defining onItemSelectedListener extension function
        calcSpinner.setOnItemSelectedListener(object : Activity(),
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
                reset("all")
                // Setting selected Calculator Mode View
                when (parent.getItemAtPosition(pos).toString()) {
                    "Standard" -> { rFrag(Standard()) }
                    "Scientific" -> { rFrag(Scientific()) }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        })
    }

    // Function to replace 
    private fun rFrag(f: Fragment){
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(R.id.modeFrag,f)
        ft.commit()
    }
    //----------------------------------------------------------------------------------------------
    // Standard Calculator Functions
    // onClick Functions
    fun stOnNumberClick(view: View) {
        if (view is Button) {
            if (view.text == "."){
                if (canInsertPt) {
                    inexp.append(view.text)
                    digit += view.text
                    canInsertPt = false
                }
            } else {
                inexp.append(view.text)
                digit += view.text
            }
            canInsertOperator = true
        }
    }
    fun stOnOperatorClick(view: View) {
        if (view is Button && canInsertOperator) {
            expEval()
            inexp.append(view.text)
            exp.add(view.text.toString())
            canInsertOperator = false
            canInsertPt = true
        }
    }
    fun stOnDelClick(view: View) {
        val inexpLen = inexp.text.length
        var dLen = digit.length
        if (inexpLen > 0) {
            val c = inexp.text.toString().last()
            inexp.text = inexp.text.subSequence(0, inexpLen - 1)
            if (c !in "/*+-"){
                if (dLen > 0)
                    digit = digit.substring(0, dLen - 1)
                else {
                    digit = exp[0]
                    dLen = digit.length
                    if (dLen > 0) {
                        digit = digit.substring(0, dLen - 1)
                        exp.clear()
                    }
                }
            }
        }
    }
    fun stOnAllClearClick(view: View) {
        reset("st")
    }
    fun stOnEqualsClick(view: View) {
        if (inexp.text.toString().isNotEmpty())
            expEval()
    }
    // Expression Evaluator
    private fun expEval(){
        if (digit != "") {
            exp.add(digit)
            digit = ""
        }
        if (exp.size==3){
            val newval = when(exp[1]){
                            "+"-> {(exp[0].toDouble()) + (exp[2].toDouble())}
                            "-"-> {(exp[0].toDouble()) - (exp[2].toDouble())}
                            "*"-> {(exp[0].toDouble()) * (exp[2].toDouble())}
                            "/"-> {(exp[0].toDouble()) / (exp[2].toDouble())}
                            else -> {0}
                        }
            exp.clear()
            exp.add(convertType(newval.toString()))
            inexp.text = exp[0]
        }
        outans.text = exp[0]
    }
    //----------------------------------------------------------------------------------------------
    // Scientific Calculator Functions
    // onClick Function
    fun sciOnNumOpClick(view: View){
        if (view is Button) {
            if (view.text.toString() == ")" && spFunOn){
                inexp.append(view.text.toString())
                spFunOn = false
            }
            else if (view.text.toString() in "()^/*+-mod")
                inexp.append(" "+view.text.toString()+" ")
            else
                inexp.append(view.text.toString())
        }
    }
    fun sciOnFuncClick(view: View) {
        if (view is Button){
            when (view.text) {
                "x²" -> inexp.append(" ^ 2")
                "1/x" -> inexp.append("1 / ")
                "√x" -> {inexp.append("√("); spFunOn = true}
                "10ˣ" -> inexp.append("10 ^ ")
                "log" -> {inexp.append("log("); spFunOn = true}
                "ln" -> {inexp.append("ln("); spFunOn = true}
            }
        }
    }
    fun sciOnUnaryClick(view: View){
        val len = inexp.text.length
        if (allowUnary && inexp.text.subSequence(0, len-1) != "-"){
            inexp.append("-")
            allowUnary = false
        } else if (inexp.text.subSequence(0, len-1) == "-"){
            inexp.text = inexp.text.subSequence(0, len-1)
            allowUnary = true
        }
    }
    fun sciOnDelClick(view: View){
        val len = inexp.text.length
        if (len > 0) inexp.text = inexp.text.subSequence(0, len-1)
    }
    fun sciOnAllClearClick(view: View){
        reset("sci")
    }
    fun sciOnEqualsClick(view: View){
        if (inexp.text.toString().isNotEmpty()) {
            val expr = inexp.text.split(" ").toMutableList()
            try {
                outans.text = returnAns(expr)
            }catch(e:Exception) {
                outans.text = "Syntax Error"
                //Log.d("Error",e.stackTraceToString())
            }
        }
    }
    // Expression Evaluator (uses Infix to Postfix and Postfix Evaluation)
    private fun returnAns(infixExp:MutableList<String>):String {
        // Infix to Postfix
        val postfixExp = mutableListOf<String>()
        val stack = mutableListOf<String>()
        val priority = mapOf("^" to 3, "/" to 2, "*" to 2, "mod" to 2, "+" to 1, "-" to 1, "(" to 0)
        for (i in infixExp) {
            if (i == "(") {
                stack.add(i)
            } else if (i in "^/*+-mod") {
                if (stack.size == 0) {
                    stack.add(i)
                } else {
                    if (priority.getValue(i) > priority.getValue(stack.last()))
                        stack.add(i)
                    else {
                        while ((stack.size > 0) && (priority.getValue(i) <= priority.getValue(stack.last())))
                            postfixExp.add(stack.removeLast())
                        stack.add(i)
                    }
                }
            } else if (i == ")") {
                while (stack.last() != "(")
                    postfixExp.add(stack.removeLast())
                stack.removeLast()
            } else if (i.isDigitsOnly()){
                postfixExp.add(i)
            } else {
                val ans:String
                if (i.startsWith("√(")) {
                    ans = returnAns(i.substring(2, i.length - 1).split(" ").toMutableList())
                    postfixExp.add(String.format("%.4f", sqrt(ans.toDouble())))
                } else if (i.startsWith("log(")) {
                    ans = returnAns(i.substring(4, i.length - 1).split(" ").toMutableList())
                    postfixExp.add(String.format("%.4f", sqrt(ans.toDouble())))
                } else if (i.startsWith("ln(")) {
                    ans = returnAns(i.substring(3, i.length - 1).split(" ").toMutableList())
                    postfixExp.add(String.format("%.4f", sqrt(ans.toDouble())))
                } else if (i=="e"){
                    postfixExp.add("2.71828")
                } else if (i=="π"){
                    postfixExp.add("3.14159")
                } else if ("e" in i){
                    postfixExp.add(String.format("%.4f",(i.substring(0,i.length-1).toDouble()*2.71828)))
                } else if ("π" in i){
                    postfixExp.add(String.format("%.4f",(i.substring(0,i.length-1).toDouble()*3.14159)))
                }
            }
        }
        infixExp.clear()
        if (stack.size != 0) {
            for (j in 0 until stack.size)
                postfixExp.add(stack.removeLast())
        }
        // Postfix Evaluation
        stack.clear()
        for (i in postfixExp) {
            if ("^/*+-mod".contains(i)) {
                val val2 = (stack.removeLast()).toDouble()
                val val1 = (stack.removeLast()).toDouble()
                when (i) {
                    "+" -> stack.add((val1 + val2).toString())
                    "-" -> stack.add((val1 - val2).toString())
                    "*" -> stack.add((val1 * val2).toString())
                    "/" -> stack.add((val1 / val2).toString())
                    "^" -> stack.add((val1.pow(val2)).toString())
                    "mod" -> stack.add((val1 % val2).toString())
                }
            } else {
                stack.add(i)
            }
        }
        return convertType(stack[0])
    }
    //----------------------------------------------------------------------------------------------
    // Common Functions
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
    private fun reset(mode:String){
        inexp.text = ""
        outans.text = ""
        when (mode) {
            "st" -> {
                canInsertOperator = false
                canInsertPt = true
                digit = ""
                exp.clear()
            }
            "sci" -> {
                allowUnary = true
                spFunOn = false
            }
            else -> {
                canInsertOperator = false
                canInsertPt = true
                digit = ""
                exp.clear()
                allowUnary = true
                spFunOn = false
            }
        }
    }
}

// Fragments
class Standard : Fragment(R.layout.standard) {}
class Scientific : Fragment(R.layout.scientific) {}
