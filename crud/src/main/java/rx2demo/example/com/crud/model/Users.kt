package rx2demo.example.com.crud.model

import android.os.Parcel
import android.os.Parcelable

class Users() : Parcelable{
    var id: Int = 0;
    var firstName: String = "";
    var lastName: String = "";

    private fun readIn(parcel: Parcel) {
        id = parcel.readInt()
        firstName = parcel.readString()
        lastName = parcel.readString()
    }


     override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Users> {
        override fun createFromParcel(parcel: Parcel): Users {
            val model = Users()
            model.readIn(parcel)
            return model
        }

        override fun newArray(size: Int): Array<Users?> {
            return arrayOfNulls(size)
        }
    }


}