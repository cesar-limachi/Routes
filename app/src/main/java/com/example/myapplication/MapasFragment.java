package com.example.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapasFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapasFragment extends Fragment implements OnMapReadyCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MapasFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapasFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapasFragment newInstance(String param1, String param2) {
        MapasFragment fragment = new MapasFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_mapas, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return v;


    }

    GoogleMap map;
    Boolean actualPosition = true;
    JSONObject jso;
    Double longitudOrigen, latitudOrigen;


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        map = googleMap;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION)){

            }else {
               ActivityCompat.requestPermissions(getActivity(),
                       new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)){

            }else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }


            return;
        }
        map.setMyLocationEnabled(true);
        map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener(){
            @Override
            public void onMyLocationChange(Location location){

                if (actualPosition){
                    latitudOrigen = location.getLatitude();
                    longitudOrigen = location.getLongitude();
                    actualPosition = false;

                    LatLng miPosicion = new LatLng(latitudOrigen, longitudOrigen);

                    map.addMarker(new MarkerOptions().position(miPosicion).title("USTED ESTA AQUI"));
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(latitudOrigen, longitudOrigen))
                            .zoom(14)
                            .bearing(30)
                            .build();
                    map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

//                    map.addMarker(new MarkerOptions().
//                            position(new LatLng(
//                                    -16.40313202699187, -71.51634562166832
//                            )).
//                            title("X"));

                    String url = "https://maps.googleapis.com/maps/api/directions/json?" +
                            "origin=-16.405896760602953, -71.5316227056429"+
                            "&destination= -16.414297234976736, -71.49257428200421" +
                            "&waypoints=-16.406990105688475, -71.53064496625672"+
                            "|-16.404361088540195, -71.52744207034831"+
                            "|-16.406519900808323, -71.52522787489373"+
                            "|-16.407153754956408, -71.52643391999023"+
                            "|-16.40809801781829, -71.52615749618732"+
                            "|-16.403492599232138, -71.51758782762028"+
                            "|-16.403167583110175, -71.51647292013875"+
                            "|-16.405472995691223, -71.51550674667261"+
                            "|-16.404530065283538, -71.51390745022167"+
                            "|-16.416851344895317, -71.49663369865098"+
                            "|-16.416537118972787, -71.49410604273004"+
                            "|-16.41521198696383, -71.49428658576444"+
                            "|-16.414938934478066, -71.49247487241759"+
                            "&mode=driving"+
                            "&key=AIzaSyC7U2vYoCP2V2YDncF5PiQnjWaBE0iAi_c";

                    String urlVenida = "https://maps.googleapis.com/maps/api/directions/json?" +
                            "origin=-16.414966671636883, -71.49267138403225"+
                            "&destination=-16.405896760602953, -71.5316227056429"+
                            "&waypoints=-16.415212627763122, -71.49428453826229"+
                            "|-16.41650652563756, -71.49410820314374"+
                            "|-16.41686107208488, -71.49664717925393"+
                            "|-16.39944228853486, -71.52157491806665"+
                            "|-16.405078954594035, -71.5283924142038"+
                            "|-16.40846376852987, -71.53321717833282"+
                            "&key=AIzaSyC7U2vYoCP2V2YDncF5PiQnjWaBE0iAi_c";

                    RequestQueue queue = Volley.newRequestQueue(getActivity());
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                jso = new JSONObject(response);
                                trazarRuta(jso,"Vuelta");
                                Log.i("jsonRuta: ", ""+response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    });
                    queue.add(stringRequest);

                    RequestQueue queueVenida = Volley.newRequestQueue(getActivity());
                    StringRequest stringRequestVenida = new StringRequest(Request.Method.GET, urlVenida, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                jso = new JSONObject(response);
                                trazarRuta(jso,"Ida");
                                Log.i("jsonRuta: ", ""+response);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                        }
                    });
                    queueVenida.add(stringRequestVenida);
                }
            }
        });
    }

    private void trazarRuta(JSONObject jso,String est) {
        JSONArray jRoutes;
        JSONArray jLegs;
        JSONArray jSteps;

        try {
            jRoutes = jso.getJSONArray("routes");
            for (int i=0; i<jRoutes.length();i++){
                jLegs = ((JSONObject)(jRoutes.get(i))).getJSONArray("legs");
                for (int j=0; j<jLegs.length();j++){
                    jSteps = ((JSONObject)jLegs.get(j)).getJSONArray("steps");
                    for (int k = 0; k<jSteps.length();k++) {
                        String polyline = "" + ((JSONObject) ((JSONObject) jSteps.get(k)).get("polyline")).get("points");
                        Log.i("end", "" + polyline);
                        List<LatLng> list = PolyUtil.decode(polyline);

                        String InicioLat = ""+((JSONObject) ((JSONObject) jSteps.get(k)).get("start_location")).get("lat");
                        String InicioLng = ""+((JSONObject) ((JSONObject) jSteps.get(k)).get("start_location")).get("lng");

                        if (est == "Vuelta") {
                            map.addPolyline(new PolylineOptions().addAll(list).color(Color.RED).width(15));

//                            animateMarker(marcadorIda,new LatLng(Double.parseDouble(InicioLat),Double.parseDouble(InicioLng)),true);
                        }
                        else if (est == "Ida"){
                            map.addPolyline(new PolylineOptions().addAll(list).color(Color.BLUE).width(15));
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case 1:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                }  else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                }
                return;
        }
        // Other 'case' lines to check for other
        // permissions this app might request.
    }

    public void animateMarker(final Marker marker, final LatLng toPosition,
                              final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = map.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }

    public interface OnFragmentInteractionListener {
    }
}

