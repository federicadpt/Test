package it.univaq.citydiscover.utility;

import java.util.List;

import it.univaq.citydiscover.model.City;

public interface OnRequestListener {
    void onRequestCompleted(List<City>data);
    void onRequestUpdate(int progress);
}
