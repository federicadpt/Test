package it.univaq.citydiscover.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import it.univaq.citydiscover.R;
import it.univaq.citydiscover.database.Database;
import it.univaq.citydiscover.model.City;
import it.univaq.citydiscover.utility.Preferences;
import it.univaq.citydiscover.utility.RequestVolley;


public class ListFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<City> data = new ArrayList<>();
    private Adapter adapter = new Adapter(data);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(adapter);

        if(Preferences.load(requireContext(), "firstStart", true)){
            download();
        } else {
            load();
        }
    }


    private void load(){
        new Thread(() -> {
            data.addAll(Database.getInstance(requireContext()).cityDao().findAll());
            recyclerView.post(() -> adapter.notifyDataSetChanged());
        }).start();
    }

    private void download(){

        JsonArrayRequest request = new JsonArrayRequest(
                com.android.volley.Request.Method.GET,
                "https://comuni-ita.herokuapp.com/api/comuni",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        new Thread(() -> {
                            try {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject item = response.getJSONObject(i);
                                    City city = City.parseCity(item);
                                    if(city != null) data.add(city);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            new Thread(() -> {
                                Database.getInstance(requireContext()).cityDao().save(data);
                                Preferences.save(requireContext(), "firstStart", false);
                            }).start();

                            requireActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());

                        }).start();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {}
                }
        );
        RequestVolley.getInstance(requireContext()).getQueue().add(request);
    }

}
