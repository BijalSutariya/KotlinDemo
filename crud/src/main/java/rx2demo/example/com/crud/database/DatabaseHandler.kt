package rx2demo.example.com.crud.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import rx2demo.example.com.crud.model.Users

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSIOM) {
    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME " +
                "($ID Integer PRIMARY KEY, $FIRST_NAME TEXT, $LAST_NAME TEXT)"
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addUser(users: Users): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(FIRST_NAME, users.firstName)
        values.put(LAST_NAME, users.lastName)

        val _success = db.insert(TABLE_NAME, null, values)
        db.close()

        Log.v("InsertedID", "$_success")
        return (Integer.parseInt("$_success") != -1)
    }

    fun getAllUsers(): ArrayList<Users> {
        var usersList: ArrayList<Users> = ArrayList()
        val db = readableDatabase
        val selectALLQuery = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectALLQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    var id = cursor.getString(cursor.getColumnIndex(ID))
                    var firstName = cursor.getString(cursor.getColumnIndex(FIRST_NAME))
                    var lastName = cursor.getString(cursor.getColumnIndex(LAST_NAME))
                    var users: Users = Users()
                    users.id = id.toInt()
                    users.firstName = firstName
                    users.lastName = lastName
                    usersList.add(users)
                    //  allUser = "$allUser\n$id $firstName $lastName"
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()
        return usersList
    }

    fun updateUserRecord(users: Users) {

        val db = this.writableDatabase
        //you can use the constants above instead of typing the column names
        db.execSQL("UPDATE  " + TABLE_NAME + " SET $FIRST_NAME ='" + users.firstName + "', $LAST_NAME ='"
                + users.lastName + "' WHERE $ID ='"
                + users.id + "'")
        db.close()
    }

    fun deleteUser(userId: Int) {
        val db = this.writableDatabase

        db.delete(TABLE_NAME, ID + "=?", arrayOf(userId.toString()))
        db.close()

    }

    companion object {
        private val DB_NAME = "UsersDB"
        private val DB_VERSIOM = 1;
        private val TABLE_NAME = "users"
        private val ID = "id"
        private val FIRST_NAME = "FirstName"
        private val LAST_NAME = "LastName"
    }
}