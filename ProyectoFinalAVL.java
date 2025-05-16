// Esta Clase es para poder representar un nodo del Árbol AVL
class Node {
    int valor;
    Node izquierda, derecha;
    int altura;

    public Node(int valor) {
        this.valor = valor;
        this.altura = 1;
    }
}

// Esta Clase ayuda a que contenga toda la lógica del Árbol AVL
class AVLTree {
    Node raiz;
    int contadorRotaciones = 0;

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
        System.out.println("Se aplico rotación DERECHA (LL) sobre nodo " + y.valor);
        contadorRotaciones++;
        Node x = y.izquierda;
        Node T2 = x.derecha;
        x.derecha = y;
        y.izquierda = T2;
        actualizarAltura(y);
        actualizarAltura(x);
        return x;
    }

    Node rotarIzquierda(Node x) {
        System.out.println("Se aplico rotación IZQUIERDA (RR) sobre nodo " + x.valor);
        contadorRotaciones++;
        Node y = x.derecha;
        Node T2 = y.izquierda;
        y.izquierda = x;
        x.derecha = T2;
        actualizarAltura(x);
        actualizarAltura(y);
        return y;
    }

    Node insertar(Node nodo, int valor) {
        if (nodo == null) {
            System.out.println("Insertando nuevo nodo: " + valor);
            return new Node(valor);
        }

        if (valor < nodo.valor) {
            nodo.izquierda = insertar(nodo.izquierda, valor);
        } else if (valor > nodo.valor) {
            nodo.derecha = insertar(nodo.derecha, valor);
        } else {
            System.out.println("Valor duplicado ignorado: " + valor);
            return nodo;
        }

        actualizarAltura(nodo);
        int fb = getFactorBalance(nodo);
        System.out.println("Nodo " + nodo.valor + " tiene factor de balance: " + fb);

        if (fb > 1 && valor < nodo.izquierda.valor) {
            System.out.println("Rotación LL requerida en nodo " + nodo.valor);
            return rotarDerecha(nodo);
        }

        if (fb < -1 && valor > nodo.derecha.valor) {
            System.out.println("Rotación RR requerida en nodo " + nodo.valor);
            return rotarIzquierda(nodo);
        }

        if (fb > 1 && valor > nodo.izquierda.valor) {
            System.out.println("Rotación LR requerida en nodo " + nodo.valor);
            nodo.izquierda = rotarIzquierda(nodo.izquierda);
            return rotarDerecha(nodo);
        }

        if (fb < -1 && valor < nodo.derecha.valor) {
            System.out.println("Rotación RL requerida en nodo " + nodo.valor);
            nodo.derecha = rotarDerecha(nodo.derecha);
            return rotarIzquierda(nodo);
        }

        return nodo;
    }

    void printTree(Node nodo) {
        String[] salida = generarArbolASCII(nodo);
        for (String linea : salida) {
            System.out.println(linea);
        }
        System.out.println("Total de rotaciones realizadas: " + contadorRotaciones);
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

// Esta es la Clase principal del código y contiene el método main()
public class ProyectoFinalAVL {
    public static void main(String[] args) {
        java.util.Scanner scanner = new java.util.Scanner(System.in);

        while (true) {
            AVLTree arbol = new AVLTree(); // Se crea nuevo árbol
            arbol.contadorRotaciones = 0;  // Reinicia contador

            System.out.println("\n--- Nuevo Árbol AVL ---");
            System.out.println("Porfavor Ingrese los números para insertar en el Árbol AVL (escriba -1 o 'exit' para terminar este árbol):");

            while (true) {
                System.out.print("Insertar número: ");
                String entrada = scanner.nextLine();
                if (entrada.equals("exit") || entrada.equals("-1")) break;

                try {
                    int valor = Integer.parseInt(entrada);
                    arbol.raiz = arbol.insertar(arbol.raiz, valor);
                    System.out.println("Árbol AVL correctamente actualizado:");
                    arbol.printTree(arbol.raiz);
                } catch (NumberFormatException e) {
                    System.out.println("Por favor, ingrese un número entero válido.");
                }
            }

            System.out.print("¿Quisiera crear otro árbol AVL? (si/no): ");
            String opcion = scanner.nextLine().toLowerCase();
            if (!opcion.equals("s") && !opcion.equals("si")) {
                System.out.println("Programa finalizado correctamente.");
                break;
            }
        }

        scanner.close();
    }
}