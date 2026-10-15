package prototype;

/**
 * Interfaz del patrón Prototype.
 * Cualquier clase que quiera poder "copiarse a sí misma" debe implementar clonar().
 */
public interface Prototipo<T> {
    T clonar();
}
