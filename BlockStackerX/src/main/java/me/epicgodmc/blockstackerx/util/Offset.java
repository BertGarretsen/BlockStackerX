package me.epicgodmc.blockstackerx.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Location;
import org.mineacademy.fo.Valid;

@NoArgsConstructor
public class Offset {

    @Getter
    @Setter
    Double x;
    @Getter
    @Setter
    Double y;
    @Getter
    @Setter
    Double z;

    public Offset(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Offset(String x, String y, String z) {
        parseX(x);
        parseY(y);
        parseZ(z);
    }

    public Offset(String[] data) {
        if (data.length == 3) {
            parseX(data[0]);
            parseY(data[1]);
            parseZ(data[2]);
        }
    }

    public static Offset of(double x, double y, double z) {
        return new Offset(x, y, z);
    }

    public static Offset of(String data) {
        return new Offset(data.split(","));
    }

    public Location calc(Location origin) {
        return origin.add(this.x, this.y, this.z);
    }

    public Location calc(SimpleLocation simpleLocation) {
        return simpleLocation.add(this.x, this.y, this.z).getLocation();
    }

    public boolean isValid() {
        return x != null && y != null && z != null;
    }

    public void parseX(String x) {
        if (Valid.isInteger(x) || Valid.isDecimal(x)) {
            this.x = Double.parseDouble(x);
        }
    }

    public void parseY(String y) {
        if (Valid.isInteger(y) || Valid.isDecimal(y)) {
            this.y = Double.parseDouble(y);
        }
    }

    public void parseZ(String z) {
        if (Valid.isInteger(z) || Valid.isDecimal(z)) {
            this.z = Double.parseDouble(z);
        }
    }


    @Override
    public String toString() {
        return x+","+y+","+z;
    }
}
