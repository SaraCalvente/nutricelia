package nutricelia.com.ListasCompra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class GestorListasCompra {

    final private HashMap<String, ArrayList> mapa;

    public GestorListasCompra() {
        this.mapa =  new HashMap<String, ArrayList>();
    }

    synchronized private void anadirProducto(Object product, String nameLista) {
        if (!mapa.containsKey(nameLista)){
            mapa.put(nameLista, new ArrayList());
        }
        mapa.get(nameLista).add(product);
    }

    public void eliminatProducto(Object product) {
        lista.remove(product);
    }

}
