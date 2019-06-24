package com.hsd.avh.standstrong.fragments



import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.hsd.avh.standstrong.R
import com.hsd.avh.standstrong.databinding.*
import com.hsd.avh.standstrong.utilities.FirebaseTrackingUtil
import com.hsd.avh.standstrong.utilities.InjectorUtils
import com.hsd.avh.standstrong.viewmodels.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import kotlinx.android.synthetic.main.fragment_data_gps.*
import com.google.android.gms.maps.model.TileOverlay
import com.google.maps.android.heatmaps.HeatmapTileProvider
import com.google.maps.android.heatmaps.Gradient
import android.graphics.Color
import android.text.method.LinkMovementMethod
import com.google.android.gms.maps.model.TileOverlayOptions
import com.hsd.avh.standstrong.adapters.BindingAdapters
import com.hsd.avh.standstrong.data.posts.Gps
import com.hsd.avh.standstrong.fragments.baseFragments.BaseFragment
import com.hsd.avh.standstrong.utilities.Const
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*


class DataPostGpsFragment : BaseFragment(), OnMapReadyCallback {

    /**
     * Alternative radius for convolution
     */
    private val ALT_HEATMAP_RADIUS = 20

    /**
     * Alternative opacity of heatmap overlay
     */
    private val ALT_HEATMAP_OPACITY = 0.4

    /**
     * Alternative heatmap gradient (blue -> red)
     * Copied from Javascript version
     */
    private val ALT_HEATMAP_GRADIENT_COLORS = intArrayOf(Color.argb(0, 0, 255, 255), // transparent
            Color.argb(255 / 3 * 2, 0, 255, 255), Color.rgb(0, 191, 255), Color.rgb(0, 0, 127), Color.rgb(255, 0, 0))

    val ALT_HEATMAP_GRADIENT_START_POINTS = floatArrayOf(0.0f, 0.10f, 0.20f, 0.60f, 1.0f)

    val ALT_HEATMAP_GRADIENT = Gradient(ALT_HEATMAP_GRADIENT_COLORS,
            ALT_HEATMAP_GRADIENT_START_POINTS)

    private var mProvider: HeatmapTileProvider? = null
    private var mOverlay: TileOverlay? = null

    private val mDefaultGradient = true
    private val mDefaultRadius = true
    private val mDefaultOpacity = true


    private var mMap: GoogleMap? = null
    private var mMapView: MapView? = null
    override fun onMapReady(p0: GoogleMap?) {
        mMap = p0
        // Add a marker in Sydney, Australia, and move the camera.
        //val sydney = LatLng(-34.0, 151.0)
        //mMap!!.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        //mMap!!.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        vm.getGPSData().observe(viewLifecycleOwner, Observer { data->
            if (data != null && data.isNotEmpty()) {
                val point = LatLng(data[0].latitude!!, data[0].longitude!!)
                mMap!!.moveCamera( CameraUpdateFactory.newLatLngZoom(point , 14.0f) );

                if (mProvider == null)
                {
                    mProvider = HeatmapTileProvider.Builder().radius(ALT_HEATMAP_RADIUS)
                            .data(readItems(data))
                            .build()

                    mOverlay = mMap!!.addTileOverlay(TileOverlayOptions().tileProvider(mProvider))

                }
                else
                {
                    mProvider!!.setData(readItems(data))
                    if (mOverlay != null) {
                        mOverlay!!.clearTileCache()
                    }
                }
            }
        })

    }

    private lateinit var vm: DataPostViewModel


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        //Post Id is
        val motherId = DataPostActivityFragmentArgs.fromBundle(arguments).motherId
        val postDate = DataPostActivityFragmentArgs.fromBundle(arguments).postDate
        val factory = InjectorUtils.providePostDataViewModelFactory(requireActivity(), motherId, postDate)
        vm = ViewModelProviders.of(this, factory)
                .get(DataPostViewModel::class.java)

        val dataPostViewModel = ViewModelProviders.of(this, factory)
                .get(DataPostViewModel::class.java)


        val binding = DataBindingUtil.inflate<FragmentDataGpsBinding>(
                inflater, R.layout.fragment_data_gps, container, false).apply {
            vm = dataPostViewModel
            dateString = BindingAdapters.provideNepaliString(Date(postDate))
            setLifecycleOwner(this@DataPostGpsFragment)
        }


/*
        val mapFragment = getSupportFragmentManager()
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
*/

        MapsInitializer.initialize(this.activity!!)
        mMapView = binding.root.findViewById(R.id.map3)
        mMapView?.onCreate(savedInstanceState);
        mMapView?.getMapAsync(this)

        initPerson(motherId).observe(this, Observer {

            binding.person = it
        })

        return binding.root
    }

    private fun readItems(data: List<Gps>) :ArrayList<LatLng> {
         var list : ArrayList<LatLng> = ArrayList<LatLng>()
        for (item: Gps in data) {
            list.add(LatLng(item.latitude!!, item.longitude!!))
        }
        return list;
    }

    override fun onResume() {
        super.onResume()
        mMapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mMapView?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView?.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mMapView?.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView?.onLowMemory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}


