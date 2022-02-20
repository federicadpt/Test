package it.univaq.citydiscover.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import it.univaq.citydiscover.model.City;

@Dao
public interface CityDao {
    @Insert(onConflict= OnConflictStrategy.REPLACE)  //LO USO PER DIRE CHE USERO' TALE METODO ANCHE COME UPDATE
    void save(List<City> cities);

    @Query("SELECT * FROM cities")
    List<City> findAll();

    @Query("SELECT * FROM cities WHERE name LIKE :text")
    City findCityByName(String text);

}
