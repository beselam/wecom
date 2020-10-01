package com.example.wecom.ui

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.wecom.R
import com.example.wecom.others.AppUtility
import com.example.wecom.others.Constants.PERMISSION_CODE_FOR_LOCATION
import kotlinx.android.synthetic.main.fragment_home.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

class HomeFragment:Fragment(R.layout.fragment_home),EasyPermissions.PermissionCallbacks {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermissions()
     fab.setOnClickListener {
         findNavController().navigate(R.id.action_homeFragment2_to_locationTrackerFragment)
     }

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


}