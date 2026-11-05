public class Entrega2 {

    //estructuras base

    // usado en todos los puntos (1 al 7): nodo con dato y puntero al siguiente
    static class Nodo {
        Object contenido;
        Nodo siguiente;

        Nodo(Object contenido) {
            this.contenido = contenido;
        }
    }

    // usada en puntos 1, 3, 4, 5 y 6: pila (lifo); con capacidad > 0 es limitada (punto 1)
    static class Pila {
        Nodo primeroDeLaPila;
        int tamano = 0;
        int capacidad; // punto 1: limite de la pila (0 = sin limite)

        Pila() { this(0); }

        Pila(int capacidad) {
            this.capacidad = capacidad;
        }

        boolean estaVacia() { return primeroDeLaPila == null; }

        // punto 1: verifica si la pila limitada ya llego a su capacidad
        boolean estaLlena() { return capacidad > 0 && tamano >= capacidad; }

        // push (puntos 1, 3, 4, 5, 6)
        boolean apilar(Object nuevoObjeto) {
            if (estaLlena()) return false; // punto 1: no deja apilar si esta llena
            Nodo nuevoNodo = new Nodo(nuevoObjeto);
            nuevoNodo.siguiente = primeroDeLaPila;
            primeroDeLaPila = nuevoNodo;
            tamano++;
            return true;
        }

        // pop (puntos 1, 3, 4, 5, 6)
        Object sacar() {
            if (estaVacia()) return null;
            Object dato = primeroDeLaPila.contenido;
            primeroDeLaPila = primeroDeLaPila.siguiente;
            tamano--;
            return dato;
        }

        // peek (puntos 5 y 6)
        Object ver() {
            return estaVacia() ? null : primeroDeLaPila.contenido;
        }

        String mostrar() {
            if (estaVacia()) return "[vacia]";
            String texto = "";
            Nodo actual = primeroDeLaPila;
            while (actual != null) {
                texto += actual.contenido + (actual.siguiente != null ? ", " : "");
                actual = actual.siguiente;
            }
            return "[" + texto + "] (el primero es el tope)";
        }
    }

    // usada en punto 3: cola lineal (fifo) con puntero al primero y al ultimo
    static class Cola {
        Nodo primero, ultimo;

        boolean estaVacia() { return primero == null; }

        void encolar(Object o) {
            Nodo nuevo = new Nodo(o);
            if (estaVacia()) {
                primero = ultimo = nuevo;
            } else {
                ultimo.siguiente = nuevo;
                ultimo = nuevo;
            }
        }

        Object desencolar() {
            if (estaVacia()) return null;
            Object dato = primero.contenido;
            primero = primero.siguiente;
            if (primero == null) ultimo = null;
            return dato;
        }

        String mostrar() {
            if (estaVacia()) return "[vacia]";
            String texto = "";
            Nodo actual = primero;
            while (actual != null) {
                texto += actual.contenido + (actual.siguiente != null ? ", " : "");
                actual = actual.siguiente;
            }
            return "[" + texto + "] (el primero es el frente)";
        }
    }

    // usada en puntos 2 y 7: cola circular (el ultimo nodo apunta de nuevo al primero)
    static class ColaCircular {
        Nodo ultimo; // ultimo.siguiente es el primero
        int tamano = 0;
        int capacidad;

        ColaCircular(int capacidad) {
            this.capacidad = capacidad;
        }

        boolean estaVacia() { return ultimo == null; }

        boolean encolar(Object o) {
            if (tamano >= capacidad) return false; // cola llena
            Nodo nuevo = new Nodo(o);
            if (estaVacia()) {
                nuevo.siguiente = nuevo; // un solo nodo se apunta a si mismo
                ultimo = nuevo;
            } else {
                nuevo.siguiente = ultimo.siguiente; // el nuevo apunta al primero
                ultimo.siguiente = nuevo;
                ultimo = nuevo;
            }
            tamano++;
            return true;
        }

        Object desencolar() {
            if (estaVacia()) return null;
            Nodo primero = ultimo.siguiente;
            if (primero == ultimo) {
                ultimo = null;
            } else {
                ultimo.siguiente = primero.siguiente;
            }
            tamano--;
            Object dato = primero.contenido;
            primero.siguiente = null; // el garbage collector puede liberar este nodo
            return dato;
        }

        Object ver() {
            return estaVacia() ? null : ultimo.siguiente.contenido;
        }

        // puntos 2 y 7: el primero pasa al final moviendo solo el puntero "ultimo"
        Object rotar() {
            if (estaVacia()) return null;
            ultimo = ultimo.siguiente;
            return ultimo.contenido;
        }

        String mostrar() {
            if (estaVacia()) return "[vacia]";
            String texto = "";
            Nodo actual = ultimo.siguiente;
            for (int i = 0; i < tamano; i++) {
                texto += actual.contenido + (i < tamano - 1 ? ", " : "");
                actual = actual.siguiente;
            }
            return "[" + texto + "]";
        }
    }

    //punto 1: pila de platos (pila limitada)

    static void punto1() {
        System.out.println("PUNTO 1 - Pila de platos en el escurridor (limite de 4)");
        Pila platos = new Pila(4); // punto 1: pila con capacidad maxima de 4 platos
        String[] lavados = {"Plato blanco", "Plato azul", "Plato verde", "Plato rojo", "Plato amarillo"};

        for (int i = 0; i < lavados.length; i++) {
            // punto 1: intenta apilar; el quinto plato debe ser rechazado
            boolean entro = platos.apilar(lavados[i]);
            System.out.println("apilar(" + lavados[i] + ") -> " + (entro ? "OK" : "NO CABE, la pila esta llena"));
        }
        System.out.println("Pila: " + platos.mostrar());

        // punto 1: desapila todo (primero sale el ultimo plato que se puso)
        while (!platos.estaVacia()) {
            System.out.println("sacar() -> " + platos.sacar());
        }
        System.out.println("sacar() con pila vacia -> " + platos.sacar());

        System.out.println(); // linea en blanco para separar del siguiente punto
    }

    //punto 2: lista de reproduccion (cola ciclica)

    static void punto2() {
        System.out.println("PUNTO 2 - Lista de reproduccion del celular (cola ciclica de 4 canciones)");
        ColaCircular lista = new ColaCircular(4); // punto 2: cola circular de capacidad 4
        String[] canciones = {"Vivir mi vida", "Bailando", "Ojitos lindos", "La camisa negra", "Hawai"};

        for (int i = 0; i < canciones.length; i++) {
            // punto 2: encola las canciones; la quinta no cabe
            boolean entro = lista.encolar(canciones[i]);
            System.out.println("encolar(" + canciones[i] + ") -> " + (entro ? "OK" : "NO CABE, la lista esta llena"));
        }
        System.out.println("Cola: " + lista.mostrar());

        // punto 2: suenan 6 canciones; al terminar la ultima vuelve a sonar la primera
        String orden = "";
        for (int i = 0; i < 6; i++) {
            orden += lista.rotar() + (i < 5 ? " -> " : "");
        }
        System.out.println("Orden en que suenan: " + orden);

        System.out.println(); // linea en blanco para separar del siguiente punto
    }

    //punto 3: fila del banco (invertir cola)

    static void punto3() {
        System.out.println("PUNTO 3 - Fila del banco (invertir una cola)");
        Cola fila = new Cola();
        String[] personas = {"Ana", "Luis", "Marta", "Pedro", "Sofia"};
        for (int i = 0; i < personas.length; i++) fila.encolar(personas[i]);

        System.out.println("Fila original: " + fila.mostrar());

        // punto 3: pasa todo de la cola a una pila (se invierte el orden)
        Pila auxiliar = new Pila();
        while (!fila.estaVacia()) auxiliar.apilar(fila.desencolar());
        // punto 3: pasa todo de la pila de vuelta a la cola (ya invertida)
        while (!auxiliar.estaVacia()) fila.encolar(auxiliar.sacar());

        System.out.println("Fila invertida: " + fila.mostrar());

        System.out.println(); // linea en blanco para separar del siguiente punto
    }

    //punto 4: ropa en el closet (busqueda segura)

    // punto 4: busca sin perder datos; retorna la posicion desde el tope (1 = tope) o -1
    static int busquedaSegura(Pila closet, Object buscada) {
        Pila auxiliar = new Pila();
        int posicion = -1;
        int contador = 1;

        // punto 4: pasa todo a una pila auxiliar mirando cada prenda
        while (!closet.estaVacia()) {
            Object actual = closet.sacar();
            auxiliar.apilar(actual);
            if (posicion == -1 && actual.equals(buscada)) posicion = contador;
            contador++;
        }
        // punto 4: devuelve todo a la pila original en su orden inicial
        while (!auxiliar.estaVacia()) closet.apilar(auxiliar.sacar());
        return posicion;
    }

    static void punto4() {
        System.out.println("PUNTO 4 - Pila de ropa en el closet (busqueda segura)");
        Pila closet = new Pila();
        String[] ropa = {"Camiseta blanca", "Jean azul", "Chaqueta negra", "Buso gris", "Pantaloneta roja"};
        for (int i = 0; i < ropa.length; i++) closet.apilar(ropa[i]);

        System.out.println("Pila antes: " + closet.mostrar());

        String[] busquedas = {"Chaqueta negra", "Gorra verde"};
        for (int i = 0; i < busquedas.length; i++) {
            // punto 4: ejecuta la busqueda segura para cada prenda
            int pos = busquedaSegura(closet, busquedas[i]);
            System.out.println("buscar(" + busquedas[i] + ") -> " + (pos == -1 ? "NO ESTA" : "esta en la posicion " + pos + " desde arriba"));
        }

        System.out.println("Pila despues: " + closet.mostrar());

        System.out.println(); // linea en blanco para separar del siguiente punto
    }

    //punto 5: pedidos del restaurante (cola con 2 pilas)

    // punto 5: cola construida con dos pilas (una de entrada y una de salida)
    static class ColaConDosPilas {
        Pila entrada = new Pila();
        Pila salida = new Pila();

        // punto 5: encolar = apilar en la pila de entrada
        void encolar(Object o) {
            entrada.apilar(o);
        }

        // punto 5: si la pila de salida esta vacia, vuelca la de entrada (invierte el orden)
        void trasladar() {
            if (salida.estaVacia()) {
                while (!entrada.estaVacia()) salida.apilar(entrada.sacar());
            }
        }

        // punto 5: desencolar = sacar de la pila de salida
        Object desencolar() {
            trasladar();
            return salida.sacar();
        }

        boolean estaVacia() {
            return entrada.estaVacia() && salida.estaVacia();
        }
    }

    static void punto5() {
        System.out.println("PUNTO 5 - Pedidos del restaurante (cola hecha con 2 pilas)");
        ColaConDosPilas pedidos = new ColaConDosPilas();

        String[] mesas = {"Mesa 1: Arepa con huevo", "Mesa 2: Jugo de mora", "Mesa 3: Sopa de lentejas", "Mesa 4: Empanadas"};
        for (int i = 0; i < mesas.length; i++) {
            pedidos.encolar(mesas[i]); // punto 5: llegan 4 pedidos
            System.out.println("llega pedido -> " + mesas[i]);
        }

        // punto 5: se despachan 2 pedidos (salen en el mismo orden en que llegaron)
        System.out.println("despachar() -> " + pedidos.desencolar());
        System.out.println("despachar() -> " + pedidos.desencolar());

        // punto 5: llega uno nuevo a mitad del proceso y el orden se mantiene
        pedidos.encolar("Mesa 5: Tinto");
        System.out.println("llega pedido -> Mesa 5: Tinto");

        while (!pedidos.estaVacia()) {
            System.out.println("despachar() -> " + pedidos.desencolar());
        }

        System.out.println(); // linea en blanco para separar del siguiente punto
    }

    //punto 6: pila de monedas (ordenar pila)

    // punto 6: ordena la pila (menor arriba) usando una sola pila auxiliar
    static void ordenarPila(Pila original) {
        Pila auxiliar = new Pila();
        while (!original.estaVacia()) {
            int temporal = (Integer) original.sacar();
            // punto 6: devuelve a la original las mayores que "temporal" para ponerlo en su lugar
            while (!auxiliar.estaVacia() && (Integer) auxiliar.ver() > temporal) {
                original.apilar(auxiliar.sacar());
            }
            auxiliar.apilar(temporal);
        }
        // punto 6: regresa todo a la pila original (queda la menor en el tope)
        while (!auxiliar.estaVacia()) original.apilar(auxiliar.sacar());
    }

    static void punto6() {
        System.out.println("PUNTO 6 - Pila de monedas (ordenar de menor a mayor valor)");
        Pila monedas = new Pila();
        int[] valores = {500, 100, 1000, 50, 200, 1000};
        for (int i = 0; i < valores.length; i++) monedas.apilar(valores[i]);

        System.out.println("Pila antes:   " + monedas.mostrar());
        ordenarPila(monedas); // punto 6: ordena
        System.out.println("Pila despues: " + monedas.mostrar());

        System.out.println(); // linea en blanco para separar del siguiente punto
    }

    // turnos del computador (cola ciclica por turnos)

    // cada estudiante tiene nombre y minutos que necesita usar el computador
    static class Estudiante {
        String nombre;
        int minutosRestantes;

        Estudiante(String nombre, int minutosRestantes) {
            this.nombre = nombre;
            this.minutosRestantes = minutosRestantes;
        }
    }

    static void punto7() {
        System.out.println("PUNTO 7 - Turnos del computador de la sala (20 minutos por turno)");
        ColaCircular turnos = new ColaCircular(4);
        // los 4 estudiantes en la cola circular
        turnos.encolar(new Estudiante("Laura", 40));
        turnos.encolar(new Estudiante("Andres", 20));
        turnos.encolar(new Estudiante("Camila", 60));
        turnos.encolar(new Estudiante("Juan", 15));

        int numeroTurno = 1;
        while (!turnos.estaVacia()) {
            Estudiante actual = (Estudiante) turnos.ver(); // punto 7: le toca al primero de la fila
            actual.minutosRestantes -= 20;

            if (actual.minutosRestantes > 0) {
                // punto 7: todavia necesita mas tiempo, pasa al final de la fila
                System.out.println("Turno " + numeroTurno + ": " + actual.nombre + " -> le faltan " + actual.minutosRestantes + " min, vuelve a la fila");
                turnos.rotar();
            } else {
                // punto 7: ya termino, sale de la cola
                System.out.println("Turno " + numeroTurno + ": " + actual.nombre + " -> termino, sale de la fila");
                turnos.desencolar();
            }
            numeroTurno++;
        }
    }

    // main

    public static void main(String[] args) {
        punto1();
        punto2();
        punto3();
        punto4();
        punto5();
        punto6();
        punto7();
    }
}
