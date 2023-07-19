package com.test.mini01_lbs01

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.test.mini01_lbs01.PermissionUtils.PermissionDeniedDialog.Companion.newInstance
import com.test.mini01_lbs01.PermissionUtils.isPermissionGranted
import com.test.mini01_lbs01.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnRequestPermissionsResultCallback,
    GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {
    private lateinit var activityMainBinding: ActivityMainBinding

    private var permissionDenied = false
    private var map: GoogleMap? = null
    private var cameraPosition: CameraPosition? = null

    // The entry point to the Places API.
    private lateinit var placesClient: PlacesClient

    // The entry point to the Fused Location Provider.
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private var lastKnownLocation: Location? = null

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private val defaultLocation = LatLng(-33.8523341, 151.2106085)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION)
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION)
        }
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        setSupportActionBar(activityMainBinding.toolbar)

        // Construct a PlacesClient
        Places.initialize(applicationContext, BuildConfig.MAPS_API_KEY)
        placesClient = Places.createClient(this)

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        //구글 지도를 보여주는 MapFragment 객체를 추출한다.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        //구글 지도 사용 준비가 완료되면 반응하는 리스너를 등록한다.
        mapFragment.getMapAsync { googleMap ->
            map = googleMap
//        googleMap.addMarker(
//            MarkerOptions()
//                .position(LatLng(0.0, 0.0))
//                .title("Marker")
//        )
            googleMap.setOnMyLocationButtonClickListener(this)
            googleMap.setOnMyLocationClickListener(this)
//        googleMap.mapType = GoogleMap.MAP_TYPE_TERRAIN
            enableMyLocation()
            getDeviceLocation()
        }

//        MapsInitializer.initialize(this, MapsInitializer.Renderer.LATEST, null)

    }

    override fun onSaveInstanceState(outState: Bundle) {
        map?.let { map ->
            outState.putParcelable(KEY_CAMERA_POSITION, map.cameraPosition)
            outState.putParcelable(KEY_LOCATION, lastKnownLocation)
        }
        super.onSaveInstanceState(outState)
    }


    private fun getDeviceLocation() {
        try {
            if (!permissionDenied) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {

                            val latLng = LatLng(
                                lastKnownLocation!!.latitude, lastKnownLocation!!.longitude
                            )
//                            //마커
//                            map?.addMarker(MarkerOptions()
//                                .title("Marker")
//                                .position(latLng)
//                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder)))
                            //카메라 이동
                            map?.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    latLng, DEFAULT_ZOOM.toFloat()
                                )
                            )
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", task.exception)
                        map?.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                    defaultLocation,
                                    DEFAULT_ZOOM.toFloat()
                                )
                        )
                        map?.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }
            }

        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    //권한 허용 메서드
    //하나라도 거부 돼있다면 isMyLocationEnabled을 false로 설정한다.
    private fun enableMyLocation() {
        // 1. 둘 다 허용 된다면 my location layer를 보여주게 한다.
        if (ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map?.isMyLocationEnabled = true
            return
        }

        // 2. If if a permission rationale dialog should be shown
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this, android.Manifest.permission.ACCESS_FINE_LOCATION
            ) || ActivityCompat.shouldShowRequestPermissionRationale(
                this, android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            PermissionUtils.RationaleDialog.newInstance(
                LOCATION_PERMISSION_REQUEST_CODE, true
            ).show(supportFragmentManager, "dialog")
            return
        }

        // 3. Otherwise, request permission
        ActivityCompat.requestPermissions(
            this, arrayOf(
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ), LOCATION_PERMISSION_REQUEST_CODE
        )
        // [END maps_check_location_permission]
    }


    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show()
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false
    }

    override fun onMyLocationClick(location: Location) {
        Toast.makeText(this, "Current location:\n$location", Toast.LENGTH_LONG).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            return
        }

        if (isPermissionGranted(
                permissions, grantResults, android.Manifest.permission.ACCESS_FINE_LOCATION
            ) || isPermissionGranted(
                permissions, grantResults, android.Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) {
            enableMyLocation()
        } else {
            permissionDenied = true
        }
    }

    // [END maps_check_location_permission_result]
    override fun onResumeFragments() {
        super.onResumeFragments()
        if (permissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError()
            permissionDenied = false
        }
    }

    private fun showMissingPermissionError() {
        newInstance(true).show(supportFragmentManager, "dialog")
    }

    companion object {
        /**
         * Request code for location permission request.
         *
         * @see .onRequestPermissionsResult
         */
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1

        private val TAG = MainActivity::class.java.simpleName
        private const val DEFAULT_ZOOM = 15
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1

        // Keys for storing activity state.
        private const val KEY_CAMERA_POSITION = "camera_position"
        private const val KEY_LOCATION = "location"
    }

}