package nutricelia.com.ListasCompra;

import java.util.ArrayList;

public class ListaCompra {
    public String nombre;
    private ArrayList lista;

    public ListaCompra(String nombre, Object product) {
        this.nombre = nombre;
        lista.add(product);
    }

    public void anadirProducto(Object product) {
        this.lista.add(product);
    }

    public void eliminatProducto(Object product) {
        lista.remove(product);
    }

    public ArrayList getLista() {
        return lista;
    }
}
