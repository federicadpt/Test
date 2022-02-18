package it.univaq.citydiscover.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import it.univaq.citydiscover.R;
import it.univaq.citydiscover.model.City;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{

    private final List<City> data;

    public Adapter(List<City> data){
        this.data=data;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_city,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.onBind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView title;
        private final TextView subtitle;

        public ViewHolder(View view){
            super(view);

            title=view.findViewById(R.id.textTitle);
            subtitle=view.findViewById(R.id.textSubtitle);
            view.findViewById(R.id.layoutRoot).setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //è la città i-esima
             City city=data.get(getAdapterPosition());
             //serve un bundle per passare i dati
             Bundle bundle = new Bundle();
             bundle.putSerializable("city",city);
             Navigation.findNavController(view).navigate(R.id.action_navList_to_detailActivity, bundle);
        }


        public void onBind(City city){
            title.setText(city.getName());
            String extras= String.format("Codice provincia %s, CAP %s",city.getProvinceCode(),city.getCap());
            subtitle.setText(extras);
        }


    }
}
