package prototype;

import java.util.List;
import java.util.Locale;

/** Prototipo concreto: Propuesta comercial. */
public class PropuestaComercial extends Documento {

    private String cliente;
    private String descripcionServicio;
    private double valor;
    private String vigencia;

    public PropuestaComercial(String numero, String fecha, String responsable,
                              String cliente, String descripcionServicio,
                              double valor, String vigencia) {
        super(numero, fecha, responsable);
        this.cliente = cliente;
        this.descripcionServicio = descripcionServicio;
        this.valor = valor;
        this.vigencia = vigencia;
    }

    /** Constructor de copia. */
    private PropuestaComercial(PropuestaComercial otra) {
        super(otra);
        this.cliente = otra.cliente;
        this.descripcionServicio = otra.descripcionServicio;
        this.valor = otra.valor;             // tipo primitivo: se copia el valor
        this.vigencia = otra.vigencia;
    }

    @Override
    public PropuestaComercial clonar() {
        return new PropuestaComercial(this);
    }

    @Override
    public String getTipo() { return "Propuesta comercial"; }

    @Override
    protected List<String> camposParticulares() {
        return List.of("cliente", "descripcionServicio", "valor", "vigencia");
    }

    @Override
    protected String getCampoParticular(String campo) {
        switch (campo) {
            case "cliente": return cliente;
            case "descripcionServicio": return descripcionServicio;
            case "valor": return String.format(Locale.US, "%.2f", valor);
            case "vigencia": return vigencia;
            default: return null;
        }
    }

    @Override
    protected boolean setCampoParticular(String campo, String v) {
        switch (campo) {
            case "cliente": cliente = v; return true;
            case "descripcionServicio": descripcionServicio = v; return true;
            case "valor":
                try { valor = Double.parseDouble(v.replace(',', '.')); return true; }
                catch (NumberFormatException e) { return false; }
            case "vigencia": vigencia = v; return true;
            default: return false;
        }
    }
}
