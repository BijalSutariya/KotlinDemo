package test.example.com.demo

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_one.*
import kotlinx.android.synthetic.main.item_view.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import test.example.com.demo.model.MainModel
import test.example.com.demo.utils.ApiInterface
import test.example.com.demo.utils.OnMyClickListener
import kotlin.coroutines.experimental.coroutineContext

class OneActivity : AppCompatActivity(), (MainModel.AndroidBean) -> Unit {
    private var mainModelList: List<MainModel.AndroidBean>? = ArrayList()
    private val BASE_URL = "https://api.learn2crack.com/android/"
    override fun invoke(partItem: MainModel.AndroidBean) {
        val index = mainModelList?.indexOf(partItem)
        partItem.isSelected = !partItem.isSelected
        Toast.makeText(this, "Clicked: ${index}", Toast.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_one)
        fetchList()
    }

    private fun fetchList() {
        progressbar.setVisibility(View.VISIBLE)

        val apiInterface = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ApiInterface::class.java)

        val interface1: Call<MainModel> = apiInterface.getData()

        interface1.enqueue(object : Callback<MainModel> {
            override fun onResponse(call: Call<MainModel>, response: Response<MainModel>) {
                progressbar.setVisibility(View.GONE)
                val jsonResponse: MainModel = response.body()!!
                //jsonResponse.android?.let { mainModelList?.addAll(it) }
                mainModelList = jsonResponse.android

                if (mainModelList?.isNotEmpty()!!) {
                    recyclerView.layoutManager = LinearLayoutManager(this@OneActivity)
                    recyclerView.adapter = MainAdapter(mainModelList!!, this@OneActivity, this@OneActivity)
                }
            }

            override fun onFailure(call: Call<MainModel>, t: Throwable) {
                Log.e("TAG", "onFailure: " + t.message)
            }
        })
    }

    class MainAdapter(val item: List<MainModel.AndroidBean>, val context: Context, val clickListener: (MainModel.AndroidBean) -> Unit) : RecyclerView.Adapter<MainAdapter.MyViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
            return MyViewHolder(v)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.bindItems(item[position],clickListener)


        }

        override fun getItemCount(): Int {
            return item.size
        }

        class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            fun bindItems(data: MainModel.AndroidBean, clickListener: (MainModel.AndroidBean) -> Unit) {
                itemView.tv_name.text = data.name
                itemView.tv_version.text = data.ver
                itemView.tv_api_level.text = data.api
                itemView.setOnClickListener{ clickListener(data)
                }
            }
        }
    }
}