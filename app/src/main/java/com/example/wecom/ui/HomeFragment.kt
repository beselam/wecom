package com.example.wecom.ui

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.wecom.R
import com.example.wecom.adapter.ExerciseRecycAdapter
import com.example.wecom.firestore.ExerciseFstore
import com.example.wecom.firestore.MyFirestore
import com.example.wecom.others.AppUtility
import com.example.wecom.others.Constants.PERMISSION_CODE_FOR_LOCATION
import com.example.wecom.viewmodel.MainViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_home.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class HomeFragment:Fragment(R.layout.fragment_home),EasyPermissions.PermissionCallbacks {
  //  val db = Firebase.firestore
    val myFirestore = MyFirestore()


    private val viewModel: MainViewModel by viewModels()
    private lateinit var mainadapter:ExerciseRecycAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        val exe = ExerciseFstore("ll","ss","ss",2
            ,3,5.5.toFloat())

        myFirestore.saveExerciseToFstore(exe)
        requestPermissions()
        setUpRecycler()
        fab.setOnClickListener {
         findNavController().navigate(R.id.action_homeFragment2_to_locationTrackerFragment)
     }
viewModel.getAllExersiseSortedByDate().observe(viewLifecycleOwner, Observer {
    mainadapter.submitList(it)
})
    }

      private fun requestPermissions() {
    if(AppUtility.locationPermissions(requireContext())) {
        return
    }
    if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
        EasyPermissions.requestPermissions(
            this,
            "Please accept location permissions continue.",
            PERMISSION_CODE_FOR_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    } else {
        EasyPermissions.requestPermissions(
            this,
            "Please accept location permissions continue.",
            PERMISSION_CODE_FOR_LOCATION ,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION
        )
    }
}



override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
}

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }
    }
fun setUpRecycler(){
    mainadapter = ExerciseRecycAdapter()
    home_rec.adapter = mainadapter

}


}