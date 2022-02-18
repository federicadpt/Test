package it.univaq.citydiscover.database;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import it.univaq.citydiscover.model.City;

//il costruttore della classe DB non può essere privato perchè si occupa room dell'instanziazione quindi non si può definire privato

@androidx.room.Database(version=1,entities={City.class})
public abstract class Database extends RoomDatabase {

    private volatile static Database instance= null;

    public static synchronized Database getInstance(Context context){  //devo conoscere il contesto dell'applicazione perchè il database sarà sempre realizzato all'interno della parte privata dell'applicazione

    if(instance==null){
        synchronized (Database.class){
            if(instance==null) instance= Room.databaseBuilder(
                    context,
                    Database.class,
                    "database.db"
            ).build();
        }
    }
    return instance;
    }

    public abstract CityDao cityDao();
}
