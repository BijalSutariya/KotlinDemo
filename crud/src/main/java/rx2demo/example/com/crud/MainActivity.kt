package rx2demo.example.com.crud

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import rx2demo.example.com.crud.database.DatabaseHandler
import rx2demo.example.com.crud.model.Users
import android.app.Activity
import android.view.inputmethod.InputMethodManager


class MainActivity : AppCompatActivity() {

    private var dbHelper: DatabaseHandler? = null
    private var usersModel: Users? = null
    private var success: Boolean = false
    private var position : Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        dbHelper = DatabaseHandler(this)

        usersModel = intent.getParcelableExtra("usersData")
        position= intent.getIntExtra("position",0)

        if (usersModel != null) {
            etFirstName.setText(usersModel!!.firstName)
            etSecondName.setText(usersModel!!.lastName)
            btnSave.setText(R.string.edit)
        }

        tvNext.setOnClickListener {
            val intent = Intent(this, UsersListActivity::class.java)
            startActivity(intent)

        }
        btnSave.setOnClickListener {
            if (validation()) {
                val users = Users()
                users.firstName = etFirstName.text.toString()
                users.lastName = etSecondName.text.toString()

                if (usersModel != null) {
                    users.id = usersModel!!.id
                    EventBus.getDefault().post(users)
                } else {
                    success = dbHelper!!.addUser(users)
                }
                etFirstName.setText("")
                etSecondName.setText("")

                val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                var view = currentFocus
                if (view == null) {
                    view = View(this)
                }
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }



    }

    private fun validation(): Boolean {
        val validate: Boolean

        if (etFirstName.text.toString() != "" &&
                etSecondName.text.toString() != "") {
            validate = true
        } else {
            validate = false
            Toast.makeText(this, "Fill all details", Toast.LENGTH_LONG).show()
        }
        return validate
    }
}
