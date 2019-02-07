package test.example.com.demo.model

class MainModel {

    lateinit var android: List<AndroidBean>

    class AndroidBean {
        lateinit var ver: String
        var name: String ?= null
        lateinit var api: String
        var isSelected: Boolean = false


        override fun toString(): String {
            return "AndroidBean(ver=$ver, name=$name, api=$api)"
        }
    }



}
