package prototype;

import java.util.ArrayList;
import java.util.List;

/**
 * Prototipo concreto: Acta de reunión.
 * Es el caso más importante para el patrón porque tiene COLECCIONES mutables
 * (participantes y compromisos): ahí es donde una copia superficial fallaría.
 */
public class ActaReunion extends Documento {

    private String temas;
    private ArrayList<String> participantes;
    private ArrayList<String> compromisos;

    public ActaReunion(String numero, String fecha, String responsable,
                       String temas, List<String> participantes, List<String> compromisos) {
        super(numero, fecha, responsable);
        this.temas = temas;
        this.participantes = new ArrayList<>(participantes);
        this.compromisos = new ArrayList<>(compromisos);
    }

    /** Constructor de copia: COPIA PROFUNDA de las listas. */
    private ActaReunion(ActaReunion otra) {
        super(otra);
        this.temas = otra.temas;
        this.participantes = copiarLista(otra.participantes);   // lista NUEVA
        this.compromisos = copiarLista(otra.compromisos);       // lista NUEVA
    }

    @Override
    public ActaReunion clonar() {
        return new ActaReunion(this);
    }

    public void agregarParticipante(String nombre) { participantes.add(nombre); }

    @Override
    public String getTipo() { return "Acta de reunión"; }

    @Override
    protected List<String> camposParticulares() {
        return List.of("temas", "participantes", "compromisos");
    }

    @Override
    protected String getCampoParticular(String campo) {
        switch (campo) {
            case "temas": return temas;
            case "participantes": return String.join(", ", participantes);
            case "compromisos": return String.join(", ", compromisos);
            default: return null;
        }
    }

    @Override
    protected boolean setCampoParticular(String campo, String v) {
        switch (campo) {
            case "temas": temas = v; return true;
            case "participantes": reemplazar(participantes, v); return true;
            case "compromisos": reemplazar(compromisos, v); return true;
            default: return false;
        }
    }

    /**
     * Modifica la lista EN SITIO (clear + addAll) en vez de asignar una lista nueva.
     * Así, si la copia compartiera la lista con el original (copia superficial),
     * el cambio se vería también en el original y la prueba lo detectaría.
     */
    private static void reemplazar(ArrayList<String> lista, String textoSeparadoPorComas) {
        lista.clear();
        for (String parte : textoSeparadoPorComas.split(",")) {
            String limpio = parte.trim();
            if (!limpio.isEmpty()) lista.add(limpio);
        }
    }
}
