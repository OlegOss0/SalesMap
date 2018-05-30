package e.sergeev.oleg.salesmap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import e.sergeev.oleg.salesmap.Models.Buyer;
import e.sergeev.oleg.salesmap.Models.MyPoint;

public class GMapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mGoogleMap;
    private MapView mMapView;
    private View mView;
    private LatLng myLocations;
    private FusedLocationProviderClient mFusedLocationClient;
    private Polygon border;
    private ArrayList<Marker> activeBuyersMarkers;
    private Boolean activeBuyersVisible;
    private ArrayList<Marker> sleepBuyersMarkers;
    private Boolean sleepBuyersVisible;

    public GMapFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_google_map, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = (MapView) mView.findViewById(R.id.google_map);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mGoogleMap = googleMap;

        CameraPosition cameraPosition;
        if(myLocations != null){
            cameraPosition = CameraPosition.builder().target(myLocations).zoom(10).bearing(0).tilt(45).build();
        }else{
            cameraPosition = CameraPosition.builder().target(new LatLng(55.755826, 37.617299)).zoom(10).bearing(0).tilt(45).build();
        }
        //googleMap.addMarker(new MarkerOptions().position(new LatLng(55.755826, 37.617299)));/*.title("Жопа мира").snippet("Вот это да..."));*/

        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    public void createBorderPolygon(MyPoint[] borderCoordinates){
        PolygonOptions options = new PolygonOptions();
        for(MyPoint point : borderCoordinates){
            options.add(new LatLng(point.getLat(), point.getLongi()));
        }
            /*for (int i = 0; i < borderCoordinates.size(); i++) {
                options.add(new LatLng(borderCoordinates[i].getLat(), borderCoordinates[i].getLongi()));
            }*/
        border = mGoogleMap.addPolygon(options);
        border.setStrokeWidth(5f);
        border.setFillColor(R.color.terrFillColor);
        border.setVisible(true);

        moveCamToObject(getCenterPoint((ArrayList<LatLng>) options.getPoints()));
    }

    public Polygon getBorder(){
        return border;
    }

    public void setBorderVisible(Boolean is){
        border.setVisible(is);
    }

    public void createActiveBuyersMarkets(@NotNull Buyer[] activeBuyers) {
        activeBuyersMarkers = new ArrayList<>();

        for (int i = 0; i < activeBuyers.length; i++) {
            Buyer buyer = activeBuyers[i];
            double lat = buyer.getCoordinates().getLat();
            double longi = buyer.getCoordinates().getLongi();

            Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lat, longi))
                    .title(Integer.toString(buyer.getId()))
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_work_point)));
            activeBuyersMarkers.add(marker);
        }

        activeBuyersVisible = true;
    }

    public void setActiveBuyersVisible(Boolean is){
        for(int i = 0; i < activeBuyersMarkers.size(); i++){
            activeBuyersMarkers.get(i).setVisible(is);
            activeBuyersVisible = is;
        }
    }
    public boolean isActiveBuyersVisible(){
        return activeBuyersVisible;
    }


    public void createSleepBuyersMarkets(@NotNull Buyer[] activeBuyers) {
        sleepBuyersMarkers= new ArrayList<>();
        for (int i = 0; i < activeBuyers.length; i++) {
            Buyer buyer = activeBuyers[i];
            Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(buyer.getCoordinates().getLat(), buyer.getCoordinates().getLongi()))
                    .title(Integer.toString(buyer.getId()))
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_sleep_point)));
            sleepBuyersMarkers.add(marker);
        }
        sleepBuyersVisible = true;
    }
    public ArrayList getSleepBuyersMarkers(){
        return sleepBuyersMarkers;
    }
    public void setSleepBuyersVisible(Boolean is){
        for(int i = 0; i < sleepBuyersMarkers.size(); i++){
            sleepBuyersMarkers.get(i).setVisible(is);
            sleepBuyersVisible = is;
        }
    }
    public boolean isSleepBuyersVisible(){
        return sleepBuyersVisible;
    }

    public ArrayList getActiveBuyersMarkers(){
        return activeBuyersMarkers;
    }

    private LatLng getCenterPoint(ArrayList<LatLng> pointsList){
        LatLng centerLatLng = null;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for(int i = 0 ; i < pointsList.size() ; i++)
        {
            builder.include(pointsList.get(i));
        }
        LatLngBounds bounds = builder.build();
        centerLatLng =  bounds.getCenter();

        return centerLatLng;
    }

    private void moveCamToObject(LatLng centerPoint){
        CameraPosition cameraPosition;
        cameraPosition = CameraPosition.builder().target(centerPoint).zoom(12).bearing(0).tilt(45).build();
        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }


}
