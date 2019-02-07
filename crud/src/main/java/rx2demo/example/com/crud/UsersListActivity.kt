package rx2demo.example.com.crud

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_users_list.*
import kotlinx.android.synthetic.main.users_item_view.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import rx2demo.example.com.crud.database.DatabaseHandler
import rx2demo.example.com.crud.model.Users
import rx2demo.example.com.crud.utils.MyClickListener

class UsersListActivity : AppCompatActivity(), MyClickListener {
    private var dbHelper: DatabaseHandler? = null


    private var userList: ArrayList<Users> = ArrayList()
    private var adapter: UsersAdapter? = null
    private var index: Int = 0

    override fun myClick(view: View, position: Int) {
        index = position
        when (view.id) {
            R.id.btnEdit -> {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("usersData", userList[position])
                intent.putExtra("position", position)
                startActivity(intent)
            }
            R.id.btnDelete -> {
                val users: Users = userList[position]
                dbHelper?.deleteUser(users.id)
                userList.removeAt(position)
                adapter?.notifyItemRemoved(position)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users_list)

        EventBus.getDefault().register(this)

        dbHelper = DatabaseHandler(this)

        fetchUsers()

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = UsersAdapter(userList, this)
        recyclerView.adapter = adapter


    }

    private fun fetchUsers() {
        userList = dbHelper!!.getAllUsers()

        adapter?.notifyDataSetChanged()

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(users: Users) {
        dbHelper!!.updateUserRecord(users)
        userList.removeAt(index)
        userList.add(index, users)
        adapter?.notifyItemChanged(index)
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    class UsersAdapter(val items: ArrayList<Users>, val listener: MyClickListener) : RecyclerView.Adapter<UsersAdapter.MyViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, position: Int): MyViewHolder {
            val v = LayoutInflater.from(parent.context).inflate(R.layout.users_item_view, parent, false)
            return MyViewHolder(v)
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.bindItems(items[position])
        }

        inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
            override fun onClick(view: View) {
                when (view.id) {
                    R.id.btnEdit, R.id.btnDelete -> listener.myClick(view, adapterPosition)
                }
            }

            fun bindItems(users: Users) {
                itemView.tvIndex.text = users.id.toString()
                itemView.tvFName.text = users.firstName
                itemView.tvLName.text = users.lastName
                itemView.btnEdit.setOnClickListener(this)
                itemView.btnDelete.setOnClickListener(this)
            }
        }
    }
}
