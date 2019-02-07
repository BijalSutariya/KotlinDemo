package test.example.com.demo.utils

import android.view.View
import test.example.com.demo.model.MainModel

interface OnMyClickListener{
    fun onMyClick(view: View, version: MainModel.AndroidBean, position: Int)
}
