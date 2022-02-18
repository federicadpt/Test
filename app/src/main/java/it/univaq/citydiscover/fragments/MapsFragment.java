package it.univaq.citydiscover.fragments;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import it.univaq.citydiscover.R;
import it.univaq.citydiscover.database.Database;
import it.univaq.citydiscover.model.City;
import it.univaq.citydiscover.utility.LocationHelper;

public class MapsFragment extends Fragment implements OnMapReadyCallback, LocationListener {

    private GoogleMap map;  // variabile globale

    private Marker myMarker;
    private List<Marker> cityMarker = new ArrayList<>(); // non voglio che vengano replicati i miei marker -> pulizia

    private ActivityResultLauncher<String> launcher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {

                    if(result) {
                        locationHelper.start(MapsFragment.this);
                    } else {
                        Toast.makeText(requireContext(), "Enable location to find cities close to you", Toast.LENGTH_SHORT).show();
                    }

                }
            }
    );

    private LocationHelper locationHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        locationHelper = new LocationHelper(requireContext(), launcher);

        SupportMapFragment fragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragmentMap);
        if(fragment != null) fragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        map = googleMap;
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {

                City city = (City) marker.getTag(); // cast perché viene ritornato un Object
                if(city != null) {

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("city", city);
                    Navigation.findNavController(requireView()).navigate(R.id.action_navMap_to_detailActivity, bundle);
                    return true;
                }

                return false;
            }
        });

        locationHelper.start(this);  // essendo troppe le città da visualizzare nel file, vado a recuperare la posizione del mio devide
                                            // "mostrami solo le città distanti <tot> in linea d'aria dalla mia posizione
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

        load(location);
    }

    private void load(Location location) {

        addMyMarker(location); // la prima volta aggiunge un marker, dalla volta successiva deve aggiornare sempre lo stesso marker (non deve crearne uno nuovo)

        for(Marker m : cityMarker) m.remove(); // pulizia (avviene sul MAIN THREAD)

        // nuovo thread per andare a fare lettura di tutte le città sul database
        new Thread(() -> {

            LatLngBounds.Builder bounds = new LatLngBounds.Builder();

            List<City> cityList = Database.getInstance(requireContext()).cityDao().findAll();
            for(City city: cityList) {

                // istanzio una location per ogni città
                Location l = new Location("city");
                l.setLatitude(city.getLatitude());
                l.setLongitude(city.getLongitude());

                // valuto la distanza in METRI
                if(l.distanceTo(location) > 30000) continue;

                // aggiungo un marker per ogni città che si trova a meno di 30 km
                MarkerOptions options = new MarkerOptions();
                options.title(String.valueOf(city.getCode()));
                options.position(new LatLng(l.getLatitude(), l.getLongitude()));
                bounds.include(new LatLng(l.getLatitude(), l.getLongitude()));

                requireActivity().runOnUiThread(() ->  {
                    Marker marker = map.addMarker(options);
                    marker.setTag(city);
                    cityMarker.add(marker);
                }); // l'aggiunta della città avviene sul main thread
            }

            try {
                requireActivity().runOnUiThread(() -> map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 10)));
                // se non avessi alcuna città il build del bounds potrebbe andare in errore (non riesce a costruirlo senza neanche una farmacia)
                // gestisco con un'eccezione per sicurezza
            } catch (Exception e) {
                e.printStackTrace();
            }


        }).start();
    }

    private void addMyMarker(Location location) {

        if(myMarker == null) { // se ancora non esiste
            MarkerOptions options = new MarkerOptions();
            options.title("MyLocation");
            options.position(new LatLng(location.getLatitude(), location.getLongitude()));
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));  // icona diversa dalle altre di colore azzurro
            myMarker = map.addMarker(options);
        } else {
            myMarker.setPosition(new LatLng(location.getLatitude(), location.getLongitude())); // aggiorno il marker
        }

    }
}
