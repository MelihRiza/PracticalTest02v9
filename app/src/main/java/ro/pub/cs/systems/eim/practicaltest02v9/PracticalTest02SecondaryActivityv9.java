package ro.pub.cs.systems.eim.practicaltest02v9;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class PracticalTest02SecondaryActivityv9 extends AppCompatActivity implements OnMapReadyCallback {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Initialize map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng ghelmegioaia = new LatLng(44.615, 22.832);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ghelmegioaia, 15));

        LatLng bucharest = new LatLng(44.4268, 26.1025);
        googleMap.addMarker(new MarkerOptions().position(bucharest).title("Marker in Bucharest"));
    }
}
