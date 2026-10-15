package prototype;

import java.util.ArrayList;

/** Almacena las plantillas y los documentos generados en dos ArrayList. */
public class Repositorio {

    private final ArrayList<Documento> plantillas = new ArrayList<>();
    private final ArrayList<Documento> generados = new ArrayList<>();

    public void registrarPlantilla(Documento d) {
        d.marcarComoPlantilla();
        plantillas.add(d);
    }

    /** Aquí ocurre el patrón: el documento se crea CLONANDO la plantilla, no con "new". */
    public Documento generarCopia(int indicePlantilla) {
        Documento copia = plantillas.get(indicePlantilla).clonar();
        generados.add(copia);
        return copia;
    }

    public ArrayList<Documento> getPlantillas() { return plantillas; }
    public ArrayList<Documento> getGenerados() { return generados; }
}
