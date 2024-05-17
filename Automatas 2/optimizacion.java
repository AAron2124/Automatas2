
public class optimizacion {

    public static void main(String[] args) {
        //Manejo de sub expresiones, AAron Colombres
        noOpti();
        opti();

    }

    public static void noOpti() {
        long startTime = System.nanoTime();
        int cantidadArticulo1 = 4;
        double precioArticulo1 = 20.0;
        int cantidadArticulo2 = 6;
        double precioArticulo2 = 30.0;
        double impuesto = 0.20;

        // Cálculo del costo total del artículo 1 con impuestos
        double costoTotalArticulo1 = cantidadArticulo1 * precioArticulo1 * (1 + impuesto);//Se repite

        // Cálculo del costo total del artículo 2 con impuestos
        double costoTotalArticulo2 = cantidadArticulo2 * precioArticulo2 * (1 + impuesto);//Se repite

        // Cálculo del costo total de la compra
        double costoTotalCompra = costoTotalArticulo1 + costoTotalArticulo2;

        System.out.println("Costo total de la compra: " + costoTotalCompra);
        long endTime = System.nanoTime();
        System.out.println("Duración no opti: " + (endTime - startTime) / 1e6 + " ms");

    }

    public static void opti() {
        long startTime = System.nanoTime();
        int cantidadArticulo1 = 4;
        double precioArticulo1 = 20.0;
        int cantidadArticulo2 = 6;
        double precioArticulo2 = 30.0;
        double impuesto = 0.20;

        // Cálculo del impuesto total una sola vez
        double impuestoTotal = 1 + impuesto;

        // Cálculo del costo total del artículo 1 con impuestos
        double costoTotalArticulo1 = cantidadArticulo1 * precioArticulo1 * impuestoTotal;

        // Cálculo del costo total del artículo 2 con impuestos
        double costoTotalArticulo2 = cantidadArticulo2 * precioArticulo2 * impuestoTotal;

        // Cálculo del costo total de la compra
        double costoTotalCompra = costoTotalArticulo1 + costoTotalArticulo2;

        System.out.println("Costo total de la compra: " + costoTotalCompra);
        long endTime = System.nanoTime();
        System.out.println("Duración opti: " + (endTime - startTime) / 1e6 + " ms");
    }

}
