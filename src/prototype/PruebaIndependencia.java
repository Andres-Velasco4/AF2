package prototype;

import java.io.PrintStream;

/**
 * Prueba automática de independencia. Para cada plantilla:
 *  1) clona dos veces,
 *  2) modifica TODOS los atributos de la primera copia,
 *  3) comprueba que ni la plantilla ni la segunda copia cambiaron.
 */
public class PruebaIndependencia {

    public static boolean ejecutar(Repositorio repo, PrintStream out) {
        boolean todoOk = true;
        if (repo.getPlantillas().isEmpty()) {
            out.println("No hay plantillas registradas para probar.");
            return false;
        }

        for (Documento plantilla : repo.getPlantillas()) {
            out.println("--------------------------------------------------");
            out.println("Probando plantilla id=" + plantilla.getId() + " (" + plantilla.getTipo() + ")");

            String antes = plantilla.detalleCompleto();       // foto del original
            Documento copia1 = plantilla.clonar();
            Documento copia2 = plantilla.clonar();

            todoOk &= verificar(out, "Las copias son objetos distintos del original (referencias diferentes)",
                    copia1 != plantilla && copia2 != plantilla && copia1 != copia2);
            todoOk &= verificar(out, "La copia nace con los mismos datos que la plantilla",
                    copia1.detalleCompleto().equals(antes));

            // Se modifica TODO en la copia 1
            for (String campo : copia1.campos()) {
                if (!copia1.setCampo(campo, "MODIFICADO")) {
                    copia1.setCampo(campo, "12345");          // campos numéricos
                }
            }

            todoOk &= verificar(out, "La copia 1 sí cambió (la modificación se aplicó)",
                    !copia1.detalleCompleto().equals(antes));
            todoOk &= verificar(out, "La plantilla original NO cambió",
                    plantilla.detalleCompleto().equals(antes));
            todoOk &= verificar(out, "La copia 2 NO cambió (las copias son independientes entre sí)",
                    copia2.detalleCompleto().equals(antes));
        }
        out.println("--------------------------------------------------");
        out.println(todoOk ? "RESULTADO FINAL: TODAS LAS PRUEBAS PASARON"
                           : "RESULTADO FINAL: HAY PRUEBAS FALLIDAS");
        return todoOk;
    }

    private static boolean verificar(PrintStream out, String descripcion, boolean condicion) {
        out.println("  [" + (condicion ? "PASA" : "FALLA") + "] " + descripcion);
        return condicion;
    }
}
