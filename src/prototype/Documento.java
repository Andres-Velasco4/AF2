package prototype;

import java.util.ArrayList;
import java.util.List;

/**
 * Prototipo abstracto: define lo que tienen en común todos los documentos
 * (atributos generales) y obliga a cada tipo concreto a saber clonarse.
 *
 * Mecanismo de clonación elegido: CONSTRUCTOR DE COPIA con copia profunda.
 * Cada subclase implementa clonar() como "return new MiClase(this)", y su
 * constructor de copia duplica también las colecciones mutables (ArrayList).
 */
public abstract class Documento implements Prototipo<Documento> {

    private static int contadorId = 0;

    private final int id;          // identificador único del objeto (cada copia tiene el suyo)
    private String numero;         // atributo general
    private String fecha;          // atributo general
    private String responsable;    // atributo general
    private boolean plantilla;     // true = es plantilla, false = documento generado
    private int origenId;          // id de la plantilla de la que se clonó (0 = no es copia)

    /** Constructor normal: se usa para crear una plantilla desde cero. */
    protected Documento(String numero, String fecha, String responsable) {
        this.id = ++contadorId;
        this.numero = numero;
        this.fecha = fecha;
        this.responsable = responsable;
        this.plantilla = false;
        this.origenId = 0;
    }

    /** Constructor de copia: es la base de la clonación. */
    protected Documento(Documento otro) {
        this.id = ++contadorId;            // la copia es un objeto NUEVO, con id propio
        this.numero = otro.numero;         // Strings son inmutables: copiar la referencia es seguro
        this.fecha = otro.fecha;
        this.responsable = otro.responsable;
        this.plantilla = false;            // una copia nunca nace como plantilla
        this.origenId = otro.id;           // se recuerda de quién se clonó
    }

    /** Copia profunda de una lista de textos (nueva lista, mismos textos inmutables). */
    protected static ArrayList<String> copiarLista(List<String> original) {
        return new ArrayList<>(original);
    }

    // ---------- Métodos que cada documento concreto debe implementar ----------
    public abstract String getTipo();
    @Override
    public abstract Documento clonar();
    protected abstract List<String> camposParticulares();
    protected abstract String getCampoParticular(String campo);
    /** Devuelve false si el valor no es válido para ese campo (ej. texto en un número). */
    protected abstract boolean setCampoParticular(String campo, String valor);

    // ---------- Acceso genérico a los atributos (generales + particulares) ----------
    public List<String> campos() {
        List<String> lista = new ArrayList<>(List.of("numero", "fecha", "responsable"));
        lista.addAll(camposParticulares());
        return lista;
    }

    public String getCampo(String campo) {
        switch (campo) {
            case "numero": return numero;
            case "fecha": return fecha;
            case "responsable": return responsable;
            default: return getCampoParticular(campo);
        }
    }

    public boolean setCampo(String campo, String valor) {
        switch (campo) {
            case "numero": this.numero = valor; return true;
            case "fecha": this.fecha = valor; return true;
            case "responsable": this.responsable = valor; return true;
            default: return setCampoParticular(campo, valor);
        }
    }

    // ---------- Getters ----------
    public int getId() { return id; }
    public boolean esPlantilla() { return plantilla; }
    public int getOrigenId() { return origenId; }
    public void marcarComoPlantilla() { this.plantilla = true; }

    /** Todos los atributos en texto: sirve para mostrar y para comparar antes/después. */
    public String detalleCompleto() {
        StringBuilder sb = new StringBuilder();
        for (String c : campos()) {
            sb.append("   - ").append(c).append(": ").append(getCampo(c)).append("\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        String rol = plantilla ? "PLANTILLA" : "COPIA (clonada de la plantilla id=" + origenId + ")";
        return "[" + getTipo() + "] id=" + id + " | " + rol + "\n" + detalleCompleto();
    }
}
