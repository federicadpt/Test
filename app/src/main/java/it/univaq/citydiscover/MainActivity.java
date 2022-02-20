package it.univaq.citydiscover;

//import static androidx.core.content.ContentProviderCompat.requireContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContentProviderCompat;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.SearchView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import it.univaq.citydiscover.database.Database;
import it.univaq.citydiscover.fragments.Adapter;
import it.univaq.citydiscover.model.City;

public class MainActivity extends AppCompatActivity {
    private List<City> data = new ArrayList<>();
    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //sono i due elementi grafici definiti nell'activity main
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavHostFragment fragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentContainerView);

        //ora vanno uniti così
        NavigationUI.setupWithNavController(bottomNavigationView, fragment.getNavController());


    }

}
