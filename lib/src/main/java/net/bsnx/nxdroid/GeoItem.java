package net.bsnx.nxdroid;

public class GeoItem {

    public static String kml(String content) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?><kml xmlns=\"http://earth.google.com/kml/2.0\">"+
                "<Document><name>NxDroid</name>"+
                content+
                "</Document></kml>";
    }

    public static String kml(GeoItem[] items) {
        StringBuilder sb = new StringBuilder();
        for (GeoItem item : items) {
            sb.append(item.toKmlMarker());
        }
        return GeoItem.kml(sb.toString());
    }

    public int id;
    public String title;
    public String desc;
    public String marker;
    public double lat;
    public double lng;
    public int document_id;

    @Override
    public String toString() {
        return "Marker " +
                "" + title +
                " " + desc +
                " " + marker +
                ", " + lat +
                "," + lng;
    }

    public String toUrl() {
        return "geo:"+this.lat+","+this.lng+"("+toString()+")";
    }

    public String toKmlMarker() {
        if (lat == 0 || lng == 0) {
            return "";
        }
        String d = toString().replace('<', '_');
        d = d.replace('>', '_');
        d = d.replace('"', '_');
        d = d.replace('&', '_');
        d = "DESC";
        return "<Placemark id=\""+id+"\">" +
                "   <name>ID-"+id+"</name>" +
                "   <visibility>1</visibility>"+
                "   <description>"+d+"</description>"+
                "<Point>" +
                "              <coordinates>"+lng+","+lat+",0</coordinates>" +
                "            </Point>"+
                "</Placemark>";
    }

    public GeoItem(int id, String title, String desc, String marker, double lat, double lng) {
        this.id = id;
        this.title = "null".equals(title) ? "" : title;
        this.desc = "null".equals(desc) ? "" : desc;
        this.marker = marker;
        this.lat = lat;
        this.lng = lng;
    }

    public double distanceTo(GeoItem other) {
        return Math.sqrt( (lat-other.lat)*(lat-other.lat)+(lng-other.lng)*(lng-other.lng) );
    }
}