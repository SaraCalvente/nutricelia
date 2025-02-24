package nutricelia.com.ListasCompra;

import java.util.ArrayList;
import java.util.HashMap;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import org.hibernate.annotations.CreationTimestamp;
import io.quarkus.hibernate.reactive.panache.PanacheEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
@Entity
@Table(name = "ListaCompra")
public class GestorBuyList extends PanacheEntity {
    @Column(nullable = false)
    String nombre;

    @Column(unique = true, nullable = false)
    String id;
    final private HashMap<String, ArrayList> map; //<NombreDeLaLista, Lista<Producto>>

    public GestorBuyList() {
        this.map =  new HashMap<String, ArrayList>();
    }

    synchronized private void addProduct(Object product, String nameList) {
        if (!map.containsKey(nameList)){
            map.put(nameList, new ArrayList());
        }
        map.get(nameList).add(product);
    }

    synchronized public Object deleteProduct(Object product, String nameList, String nameProduct) {
        Object deletedProduct = null;
        if (map.containsKey(nameList)){
            deletedProduct = searchProduct(map.get(nameList),nameProduct);
            if (deletedProduct != null){
                map.get(nameList).remove(deletedProduct);
            }
        }
        return deletedProduct; // MODIFICAR
    }
    synchronized private Object searchProduct(ArrayList list, String nameProduct) { //implementado
        for(Object product : list ){
            if (product == nameProduct){ //Aqui, product tendría que tener un atributo nombre al que hacerle un get para comparar
                return product;
            }
        }
        return null;
    }

    synchronized public ArrayList getList(String nameList) {
        ArrayList productList = map.get(nameList);
        if (productList == null)
            return null;

        return productList;
    }

}
