package nutricelia.com.ListasCompra;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

public class GestorListasCompra {

    final private HashMap<String, ArrayList> mapa; //<NombreLista, Lista<Producto>>

    public GestorListasCompra() {
        this.mapa =  new HashMap<String, ArrayList>();
    }

    synchronized private void anadirProducto(Object product, String nameLista) {
        if (!mapa.containsKey(nameLista)){
            mapa.put(nameLista, new ArrayList());
        }
        mapa.get(nameLista).add(product);
    }

    synchronized public Object eliminarProducto(Object product, String nameLista, String nameProduct) {
        Object productoEliminado = null;
        if (mapa.containsKey(nameLista)){
            productoEliminado = buscaProducto(mapa.get(nameLista),nameProduct);
            if (productoEliminado != null){
                mapa.get(nameLista).remove(productoEliminado);
            }
        }
        return productoEliminado; // MODIFICAR
    }
    synchronized private Object buscaProducto(ArrayList lista, String nameProduct) { //implementado
        for(Object product : lista ){
            if (product == nameProduct){ //Aqui, product tendría que tener un atributo nombre al que hacerle un get para comparar
                return product;
            }
        }
        return null;
    }

}
