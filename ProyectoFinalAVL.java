// Esta Clase es la que permite representar correctamente un nodo del Árbol AVL
class Node {
    int valor;
    Node izquierda, derecha;
    int altura;

    public Node(int valor) {
        this.valor = valor;
        this.altura = 1;
    }
}

// Esta Clase nos ayuda a poder representar correctamente el Árbol AVL y sus respectivas operaciones
class AVLTree {
    Node raiz;

    int getAltura(Node nodo) {
        return (nodo == null) ? 0 : nodo.altura;
    }

    int getFactorBalance(Node nodo) {
        return (nodo == null) ? 0 : getAltura(nodo.izquierda) - getAltura(nodo.derecha);
    }

    void actualizarAltura(Node nodo) {
        nodo.altura = 1 + Math.max(getAltura(nodo.izquierda), getAltura(nodo.derecha));
    }

    Node rotarDerecha(Node y) {
        Node x = y.izquierda;
        Node T2 = x.derecha;
        x.derecha = y;
        y.izquierda = T2;
        actualizarAltura(y);
        actualizarAltura(x);
        return x;
    }

    Node rotarIzquierda(Node x) {
        Node y = x.derecha;
        Node T2 = y.izquierda;
        y.izquierda = x;
        x.derecha = T2;
        actualizarAltura(x);
        actualizarAltura(y);
        return y;
    }

    Node insertar(Node nodo, int valor) {
        if (nodo == null) return new Node(valor);
        if (valor < nodo.valor) nodo.izquierda = insertar(nodo.izquierda, valor);
        else if (valor > nodo.valor) nodo.derecha = insertar(nodo.derecha, valor);
        else return nodo; // No se duplica
        actualizarAltura(nodo);
        int fb = getFactorBalance(nodo);
        if (fb > 1 && valor < nodo.izquierda.valor) return rotarDerecha(nodo); // LL
        if (fb < -1 && valor > nodo.derecha.valor) return rotarIzquierda(nodo); // RR
        if (fb > 1 && valor > nodo.izquierda.valor) {
            nodo.izquierda = rotarIzquierda(nodo.izquierda);
            return rotarDerecha(nodo); // LR
        }
        if (fb < -1 && valor < nodo.derecha.valor) {
            nodo.derecha = rotarDerecha(nodo.derecha);
            return rotarIzquierda(nodo); // RL
        }
        return nodo;
    }

    void printTree(Node nodo) {
        String[] salida = generarArbolASCII(nodo);
        for (String linea : salida) {
            System.out.println(linea);
        }
    }

    String[] generarArbolASCII(Node nodo) {
        if (nodo == null) return new String[]{""};

        String rootStr = String.valueOf(nodo.valor);
        String[] izq = generarArbolASCII(nodo.izquierda);
        String[] der = generarArbolASCII(nodo.derecha);

        int anchoIzq = maxAncho(izq);
        int anchoDer = maxAncho(der);
        int alto = Math.max(izq.length, der.length);

        izq = ajustarAltura(izq, anchoIzq, alto);
        der = ajustarAltura(der, anchoDer, alto);

        String raizLinea = esp(anchoIzq) + rootStr + esp(anchoDer);
        String ramaLinea = (nodo.izquierda != null || nodo.derecha != null)
                ? esp(anchoIzq - 1) + "/" + esp(rootStr.length()) + "\\" + esp(anchoDer - 1)
                : esp(raizLinea.length());

        String[] resultado = new String[alto + 2];
        resultado[0] = raizLinea;
        resultado[1] = ramaLinea;

        for (int i = 0; i < alto; i++) {
            resultado[i + 2] = izq[i] + esp(rootStr.length()) + der[i];
        }

        return resultado;
    }

    String esp(int n) {
        return " ".repeat(Math.max(0, n));
    }

    int maxAncho(String[] arr) {
        int max = 0;
        for (String s : arr) max = Math.max(max, s.length());
        return max;
    }

    String[] ajustarAltura(String[] arr, int ancho, int alto) {
        String[] resultado = new String[alto];
        for (int i = 0; i < alto; i++) {
            resultado[i] = (i < arr.length ? arr[i] : "") + esp(ancho - (i < arr.length ? arr[i].length() : 0));
        }
        return resultado;
    }
}

// Esta es la Clase principal del programa
public class ProyectoFinalAVL {
    public static void main(String[] args) {
        java.util.Scanner scanner = new java.util.Scanner(System.in);

        while (true) {
            AVLTree arbol = new AVLTree();
            System.out.println("\n--- Nuevo Árbol AVL ---");
            System.out.println("Ingrese los números para insertar en el Árbol AVL (escriba -1 o 'exit' para terminar este árbol):");

            while (true) {
                System.out.print("Insertar número: ");
                String entrada = scanner.nextLine();
                if (entrada.equals("exit") || entrada.equals("-1")) break;

                try {
                    int valor = Integer.parseInt(entrada);
                    arbol.raiz = arbol.insertar(arbol.raiz, valor);
                    System.out.println("Árbol AVL actualizado:");
                    arbol.printTree(arbol.raiz);
                } catch (NumberFormatException e) {
                    System.out.println("Por favor, ingrese un número entero válido.");
                }
            }

            System.out.print("¿Desea crear otro árbol AVL? (si/no): ");
            String opcion = scanner.nextLine().toLowerCase();
            if (!opcion.equals("s") && !opcion.equals("si")) {
                System.out.println("Programa finalizado correctamente.");
                break;
            }
        }

        scanner.close();
    }
}