package mx.feliperomero.guardias;

import java.io.Serializable;
import java.util.Date;

public class Guardia implements Serializable {
    private String id;
    private String date;
    private String site;
    private String units;

    public Guardia() {
    }

    public Guardia(String date, String site, String units) {
        this.date = date;
        this.site = site;
        this.units = units;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    @Override
    public String toString() {
        return date + "\n" + site + "\n" + units;
    }
}
