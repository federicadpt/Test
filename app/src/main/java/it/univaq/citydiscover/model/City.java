package it.univaq.citydiscover.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

@Entity(tableName="cities")
public class City implements Serializable {

    public static City parseCity(JSONObject item){
        try{
            City city=new City();
            city.setCode(Integer.parseInt(item.getString("codice")));
            city.setName(item.getString("nome"));
            city.setCadastralCode(item.getString("codiceCatastale"));
            city.setCap(item.getString("cap"));
            city.setPrefix(item.getString("prefisso"));
            city.setProvinceCode(item.getString("provincia"));

            JSONObject itemCoordinates = item.optJSONObject("coordinate");
            city.setLatitude(itemCoordinates.getDouble("lat"));
            city.setLongitude(itemCoordinates.getDouble("lng"));

            return city;
        }catch(JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @PrimaryKey(autoGenerate=true)
    private long id;
    private int code;
    private String name;
    @ColumnInfo(name="cadastral_code")
    private String cadastralCode;
    private String cap;
    private String prefix;
    @ColumnInfo(name="province_code")
    private String provinceCode;
    private double latitude;
    private double longitude;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCadastralCode() {
        return cadastralCode;
    }

    public void setCadastralCode(String cadastralCode) {
        this.cadastralCode = cadastralCode;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


}
