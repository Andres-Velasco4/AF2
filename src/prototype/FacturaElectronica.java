package prototype;

import java.util.List;
import java.util.Locale;

/** Prototipo concreto: Factura electrónica. */
public class FacturaElectronica extends Documento {

    private String cliente;
    private double subtotal;
    private double impuestos;

    public FacturaElectronica(String numero, String fecha, String responsable,
                              String cliente, double subtotal, double impuestos) {
        super(numero, fecha, responsable);
        this.cliente = cliente;
        this.subtotal = subtotal;
        this.impuestos = impuestos;
    }

    /** Constructor de copia. */
    private FacturaElectronica(FacturaElectronica otra) {
        super(otra);
        this.cliente = otra.cliente;
        this.subtotal = otra.subtotal;
        this.impuestos = otra.impuestos;
    }

    @Override
    public FacturaElectronica clonar() {
        return new FacturaElectronica(this);
    }

    public double getTotal() { return subtotal + impuestos; }

    @Override
    public String getTipo() { return "Factura electrónica"; }

    @Override
    protected List<String> camposParticulares() {
        return List.of("cliente", "subtotal", "impuestos");
    }

    @Override
    protected String getCampoParticular(String campo) {
        switch (campo) {
            case "cliente": return cliente;
            case "subtotal": return String.format(Locale.US, "%.2f", subtotal);
            case "impuestos": return String.format(Locale.US, "%.2f", impuestos);
            default: return null;
        }
    }

    @Override
    protected boolean setCampoParticular(String campo, String v) {
        switch (campo) {
            case "cliente": cliente = v; return true;
            case "subtotal":
                try { subtotal = Double.parseDouble(v.replace(',', '.')); return true; }
                catch (NumberFormatException e) { return false; }
            case "impuestos":
                try { impuestos = Double.parseDouble(v.replace(',', '.')); return true; }
                catch (NumberFormatException e) { return false; }
            default: return false;
        }
    }
}
