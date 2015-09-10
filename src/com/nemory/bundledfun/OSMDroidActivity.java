package com.nemory.bundledfun;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;

import com.google.android.maps.MapActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;

public class OSMDroidActivity extends MapActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_osmdroid);
        
        final MapView mvMap = (MapView) findViewById(R.id.mvMap1);
		mvMap.setTileSource(TileSourceFactory.MAPNIK);
		mvMap.setMultiTouchControls(true);
		mvMap.setBuiltInZoomControls(true);
		
		MapController mapController = mvMap.getController();
		mapController.setZoom(8);
		double lat = Double.parseDouble("10.574222");
		double longi = Double.parseDouble("123.310547");
		GeoPoint g = new GeoPoint((int)(lat * 1e6) , (int)(longi * 1e6));
		
		mapController.animateTo(g);
		
		mvMap.setOnTouchListener(new View.OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				int X = (int)event.getX();
		        int Y = (int)event.getY();

		        GeoPoint geoPoint = (GeoPoint) mvMap.getProjection().fromPixels(X, Y);
		        
		        Log.d("LOCATION", geoPoint.getLatitudeE6() + ", " + geoPoint.getLongitudeE6());
				return true;
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_osmdroid, menu);
        return true;
    }

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
}
