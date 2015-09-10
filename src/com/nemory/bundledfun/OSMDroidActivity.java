package com.nemory.bundledfun;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;

public class OSMDroidActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_osmdroid);
        
        MapView mvMap = (MapView) findViewById(R.id.mvMap1);
		mvMap.setTileSource(TileSourceFactory.MAPNIK);
		mvMap.setMultiTouchControls(true);
		mvMap.setBuiltInZoomControls(true);
		
		MapController mapController = mvMap.getController();
		mapController.setZoom(8);
		mapController.animateTo(new GeoPoint(10.574222 , 123.310547));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_osmdroid, menu);
        return true;
    }
}
