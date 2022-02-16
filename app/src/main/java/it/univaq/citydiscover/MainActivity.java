package it.univaq.citydiscover;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //sono i due elementi grafici definiti nell'activity main
        BottomNavigationView bottomNavigationView= findViewById(R.id.bottomNavigationView);
        NavHostFragment fragment= (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentContainerView);

        //ora vanno uniti così
        NavigationUI.setupWithNavController(bottomNavigationView,fragment.getNavController());

    }
}