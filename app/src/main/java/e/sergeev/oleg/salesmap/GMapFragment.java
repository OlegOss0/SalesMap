package e.sergeev.oleg.salesmap;

import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
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
import java.util.List;

import e.sergeev.oleg.salesmap.Loaders.FullDataLoader;
import e.sergeev.oleg.salesmap.Models.Buyer;
import e.sergeev.oleg.salesmap.Models.MyPoint;

public class GMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, MapControllerListener {

    private GoogleMap mGoogleMap;
    private MapView mMapView;
    private View mView;
    public Polygon border;
    private ArrayList<Marker> buyersMarkers;
    private Boolean buyersMarketsVisible;

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
        cameraPosition = CameraPosition.builder().target(new LatLng(55.755826, 37.617299)).zoom(10).bearing(0).tilt(45).build();
        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    //TODO унаследовано от MapControllerListener
    @Override
    public void createBorder(MyPoint[] borderCoordinates){
        PolygonOptions options = new PolygonOptions();
        for(MyPoint point : borderCoordinates){
            options.add(new LatLng(point.getLat(), point.getLongi()));
        }
        border = mGoogleMap.addPolygon(options);
        border.setStrokeWidth(5f);
        border.setFillColor(R.color.terrFillColor);
        border.setVisible(true);

        moveCamToObject(getCenterPoint((ArrayList<LatLng>)options.getPoints()));
    }

    @Override
    public boolean isBorderVisible(){
        return border.isVisible();
    }

    @Override
    public void setBorderVisible(boolean visible) {
        if(!border.isVisible()){
            moveCamToObject(getCenterPoint((ArrayList)border.getPoints()));
        }
        border.setVisible(visible);
    }

    public boolean isBorderCreated(){
        boolean created = false;
        if(border != null){
            created = true;
        }
        return created;
    }


    //TODO унаследовано от MapControllerListener
    public void createBuyersMarkets(@NotNull Buyer[] activeBuyers) {
        buyersMarkers = new ArrayList<>();
        for (int i = 0; i < activeBuyers.length; i++) {
            Buyer buyer = activeBuyers[i];
            double lat = buyer.getCoordinates().getLat();
            double longi = buyer.getCoordinates().getLongi();

            Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(lat, longi))
                    .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_work_point)));
            marker.setTag(Integer.toString(buyer.getId()));
            buyersMarkers.add(marker);
        }
        buyersMarketsVisible = true;
        mGoogleMap.setOnMarkerClickListener(this);
    }

    public boolean isBuyersMarketsCreated(){
        boolean created = false;
        if(buyersMarkers != null){
            created = true;
        }
        return created;
    }

    public void setBuyersMarketsVisible(boolean visible){
        ArrayList<LatLng> temp = new ArrayList<>();
        for(int i = 0; i < buyersMarkers.size(); i++){
            buyersMarkers.get(i).setVisible(visible);
            buyersMarketsVisible = visible;
            temp.add(i,buyersMarkers.get(i).getPosition());
        }
        if (visible == true){
            moveCamToObject(getCenterPoint(temp));
        }
    }

    @Override
    public boolean isBuyersMarketsVisible(){
        return buyersMarketsVisible;
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

    @Override
    public boolean onMarkerClick(final Marker marker) {
        String clickCount = (String) marker.getTag();
        MapController.Companion.getInstance().showBuyerInfo(clickCount);
        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
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
}
