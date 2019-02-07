package nevigation.example.com.fragmentdemo.camera

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_camera.*
import kotlinx.android.synthetic.main.fragment_camera.view.*
import nevigation.example.com.fragmentdemo.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class CameraFragment : Fragment(), View.OnClickListener {

    private lateinit var uri: Uri
    private var CAMERA_REQUEST: Int = 1
    private var REQUEST_CAMERA: Int = 2
    private var GALLERY_REQUEST: Int = 3

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btnSelectPhoto -> {
                selectPhoto()
            }
        }
    }

    private fun selectPhoto() {
        val items = arrayOf<CharSequence>("Take Photo", "Choose photo")
        val builder = AlertDialog.Builder(activity!!)
        builder.setTitle("Add Photo!")
        builder.setItems(items) { dialog, item ->
            if (items[item] == "Take Photo") {
                requestpermission()
            } else if (items[item] == "Choose photo") {
                displayImage()
            }
        }
        builder.show()
    }

    private fun displayImage() {
        if (ActivityCompat.checkSelfPermission(activity!!, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openImage()
        } else {
            val permossionRequested = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permossionRequested, GALLERY_REQUEST)
            }
        }
    }

    private fun openImage() {
        val intent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(intent, GALLERY_REQUEST)
    }

    private fun requestpermission() {
        if (ActivityCompat.checkSelfPermission(activity!!, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            intentCamera()
        } else {
            val permossionRequested = arrayOf(Manifest.permission.CAMERA)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permossionRequested, REQUEST_CAMERA)
            }
        }
    }

    private fun intentCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(activity!!.packageManager) != null) {
            val file = getOutputMediaFile()
            if (file != null) {
                uri = FileProvider.getUriForFile(activity!!, activity!!.packageName + ".provider", file as File)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                startActivityForResult(intent, CAMERA_REQUEST)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CAMERA_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    Glide.with(this).load(uri).into(ivPhoto)
                }
            }
            GALLERY_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    uri = data?.data!!
                    Glide.with(this).load(uri).into(ivPhoto)
                }
            }
        }
    }

    private fun getOutputMediaFile(): Any? {
        val mediaStorageDir = File(activity!!.externalCacheDir, "CameraDemo")

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null
            }
        }
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
        return File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CAMERA -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    intentCamera()
                } else {
                    Toast.makeText(activity!!, "Unable to invoke camera without permossion", Toast.LENGTH_SHORT).show()
                }
            }
            GALLERY_REQUEST -> {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openImage()
                } else {
                    Toast.makeText(activity!!, "cannot take photo", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_camera, container, false)
        view.btnSelectPhoto.setOnClickListener(this)

        return view;
    }
}
