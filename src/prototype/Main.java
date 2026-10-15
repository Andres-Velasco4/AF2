package prototype;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/** Menú interactivo por consola. */
public class Main {

    private static final Scanner in = new Scanner(System.in);
    private static final Repositorio repo = new Repositorio();

    public static void main(String[] args) {
        // Modo de pruebas automáticas:  java prototype.Main --pruebas
        if (args.length > 0 && args[0].equals("--pruebas")) {
            cargarEjemplos();
            System.exit(PruebaIndependencia.ejecutar(repo, System.out) ? 0 : 1);
        }

        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Elija una opción: ");
            System.out.println();
            switch (opcion) {
                case 1: crearPlantilla(); break;
                case 2: consultar("PLANTILLAS REGISTRADAS", repo.getPlantillas()); break;
                case 3: generarCopia(); break;
                case 4: modificarGenerado(); break;
                case 5: consultar("DOCUMENTOS GENERADOS (COPIAS)", repo.getGenerados()); break;
                case 6: PruebaIndependencia.ejecutar(repo, System.out); break;
                case 7: cargarEjemplos(); System.out.println("Se cargaron 3 plantillas de ejemplo."); break;
                case 0: System.out.println("Hasta luego."); break;
                default: System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    // ------------------------------------------------------------ menú
    private static void mostrarMenu() {
        System.out.println("\n========== SISTEMA DE DOCUMENTOS (PATRÓN PROTOTYPE) ==========");
        System.out.println("1. Crear y registrar una plantilla");
        System.out.println("2. Consultar plantillas");
        System.out.println("3. Generar un documento clonando una plantilla");
        System.out.println("4. Modificar un documento generado");
        System.out.println("5. Consultar documentos generados");
        System.out.println("6. Verificar independencia de las copias (prueba automática)");
        System.out.println("7. Cargar plantillas de ejemplo");
        System.out.println("0. Salir");
    }

    // ------------------------------------------------------------ opción 1
    private static void crearPlantilla() {
        System.out.println("Tipo de documento: 1) Propuesta comercial  2) Factura electrónica  3) Acta de reunión");
        int tipo = leerEntero("Tipo: ");
        if (tipo < 1 || tipo > 3) { System.out.println("Tipo no válido."); return; }

        String numero = leerTexto("Número del documento: ");
        String fecha = leerTexto("Fecha (AAAA-MM-DD): ");
        String responsable = leerTexto("Responsable: ");

        Documento nueva;
        if (tipo == 1) {
            String cliente = leerTexto("Cliente: ");
            String descripcion = leerTexto("Descripción del servicio: ");
            double valor = leerDecimal("Valor: ");
            String vigencia = leerTexto("Vigencia: ");
            nueva = new PropuestaComercial(numero, fecha, responsable, cliente, descripcion, valor, vigencia);
        } else if (tipo == 2) {
            String cliente = leerTexto("Cliente: ");
            double subtotal = leerDecimal("Subtotal: ");
            double impuestos = leerDecimal("Impuestos: ");
            nueva = new FacturaElectronica(numero, fecha, responsable, cliente, subtotal, impuestos);
        } else {
            String temas = leerTexto("Temas tratados: ");
            List<String> participantes = separar(leerTexto("Participantes (separados por coma): "));
            List<String> compromisos = separar(leerTexto("Compromisos (separados por coma): "));
            nueva = new ActaReunion(numero, fecha, responsable, temas, participantes, compromisos);
        }
        repo.registrarPlantilla(nueva);
        System.out.println("Plantilla registrada:\n" + nueva);
    }

    // ------------------------------------------------------------ opción 3
    private static void generarCopia() {
        if (repo.getPlantillas().isEmpty()) { System.out.println("Primero registre una plantilla."); return; }
        listarResumen(repo.getPlantillas());
        int pos = leerEntero("Número de la plantilla a clonar: ") - 1;
        if (pos < 0 || pos >= repo.getPlantillas().size()) { System.out.println("Número no válido."); return; }
        Documento copia = repo.generarCopia(pos);
        System.out.println("Documento generado por CLONACIÓN (no con new):\n" + copia);
    }

    // ------------------------------------------------------------ opción 4
    private static void modificarGenerado() {
        if (repo.getGenerados().isEmpty()) { System.out.println("Aún no hay documentos generados."); return; }
        listarResumen(repo.getGenerados());
        int pos = leerEntero("Número del documento a modificar: ") - 1;
        if (pos < 0 || pos >= repo.getGenerados().size()) { System.out.println("Número no válido."); return; }
        Documento doc = repo.getGenerados().get(pos);

        List<String> campos = doc.campos();
        for (int i = 0; i < campos.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + campos.get(i) + " = " + doc.getCampo(campos.get(i)));
        }
        int c = leerEntero("Número del atributo a cambiar: ") - 1;
        if (c < 0 || c >= campos.size()) { System.out.println("Número no válido."); return; }
        String nuevo = leerTexto("Nuevo valor para '" + campos.get(c) + "': ");
        if (doc.setCampo(campos.get(c), nuevo)) {
            System.out.println("Documento actualizado:\n" + doc);
            System.out.println("(La plantilla original no se tocó: use la opción 2 para comprobarlo.)");
        } else {
            System.out.println("Valor no válido para ese atributo (se esperaba un número).");
        }
    }

    // ------------------------------------------------------------ consultas
    private static void consultar(String titulo, ArrayList<Documento> lista) {
        System.out.println("=== " + titulo + " ===");
        if (lista.isEmpty()) { System.out.println("(vacío)"); return; }
        for (int i = 0; i < lista.size(); i++) {
            System.out.println("#" + (i + 1) + "  " + lista.get(i));
        }
    }

    private static void listarResumen(ArrayList<Documento> lista) {
        for (int i = 0; i < lista.size(); i++) {
            Documento d = lista.get(i);
            System.out.println("  " + (i + 1) + ". " + d.getTipo() + " - número " + d.getCampo("numero") + " (id=" + d.getId() + ")");
        }
    }

    // ------------------------------------------------------------ ejemplos
    private static void cargarEjemplos() {
        repo.registrarPlantilla(new PropuestaComercial("PROP-001", "2026-10-01", "Ana Torres",
                "Empresa Alfa S.A.S.", "Desarrollo de sitio web corporativo", 5000000, "30 días"));
        repo.registrarPlantilla(new FacturaElectronica("FE-100", "2026-10-01", "Ana Torres",
                "Cliente Base", 1000000, 190000));
        repo.registrarPlantilla(new ActaReunion("ACT-001", "2026-10-01", "Luis Gómez",
                "Revisión de avance del proyecto",
                List.of("Ana", "Luis", "Marta"), List.of("Entregar informe")));
    }

    // ------------------------------------------------------------ utilidades de lectura
    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        if (!in.hasNextLine()) { System.out.println("\n(Fin de la entrada)"); System.exit(0); }
        String linea = in.nextLine().trim();
        System.out.println(linea);   // eco: deja visible lo escrito cuando la entrada viene de un archivo
        return linea;
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            try { return Integer.parseInt(leerTexto(mensaje)); }
            catch (NumberFormatException e) { System.out.println("Escriba un número entero."); }
        }
    }

    private static double leerDecimal(String mensaje) {
        while (true) {
            try { return Double.parseDouble(leerTexto(mensaje).replace(',', '.')); }
            catch (NumberFormatException e) { System.out.println("Escriba un número."); }
        }
    }

    private static List<String> separar(String texto) {
        List<String> lista = new ArrayList<>();
        for (String parte : texto.split(",")) {
            if (!parte.trim().isEmpty()) lista.add(parte.trim());
        }
        return lista;
    }
}
