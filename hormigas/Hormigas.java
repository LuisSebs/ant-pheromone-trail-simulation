package hormigas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import processing.core.PApplet;

public class Hormigas extends PApplet {

    int alto = 100;
    int ancho = 150;
    int celda = 4;
    int hormigas = 4;
    ModeloHormigas modelo;

    @Override
    public void setup(){
        background(0,0,0);
        modelo = new ModeloHormigas(ancho, alto, celda, hormigas);
    }

    @Override
    public void settings() {
        size(ancho * celda, alto * celda);
    }

    @Override
    public void draw(){

        for (int i = 0; i < alto; i++){
            for (int j = 0; j < ancho; j++){   
                Celda c = modelo.mundo[i][j];         
                if (c.estado == 1){
                    // Dibujamos colonia
                    fill(90,75,50);
                }else if (c.estado == 2){
                    // Dibujamos comida
                    fill(255,0,0);
                }else{
                    // Dibujamos feromonas
                    fill(0,0,(int) c.feromonas * 5);
                }          
                rect(j * celda, i * celda, celda, celda);
            }
        }

        // Dibujamos hormigas
        for (Hormiga h : modelo.hormigas){
            //fill(90,75,50);
            fill(0,255,0);
            rect(h.posY * modelo.tamanio, h.posX * modelo.tamanio, modelo.tamanio, modelo.tamanio);
        }

        // Siguiente paso
        modelo.siguiente();
        
    }

    @Override
    public void mouseClicked(){
        modelo.siguiente();
    }

    class Celda {

        int celdaX, celdaY;
        /**
         * 0: no hay nada, 1: hay colonia 2: hay comida
         */
        int estado;
        double feromonas;

        /**
         * Constructor de una celda
         * @param celdaX coordenada en x
         * @param celdaY coordenada en y
         * @param estado 0: no hay nada, 1: hay colonia 2: hay comida
         */
        Celda(int celdaX, int celdaY, int estado){
            this.celdaX = celdaX;
            this.celdaY = celdaY;
            this.estado = estado;
            this.feromonas = 1.0;
        }
    }

    class Hormiga {

        int posX;
        int posY;
        int direccion;
        /** true si esta cargando comida, false en caso contrario */
        boolean comida;

        /**
         * Constructor de una hormiga
         * @param posX posicion en el eje X de la hormiga
         * @param posY posicion en el eje Y de la hormiga
         * @param direccion direccion en la que mira
         *    -----------
         *   | 0 | 1 | 2 |
         *   |-----------|
         *   | 7 |   | 3 |
         *   |-----------|
         *   | 6 | 5 | 4 |
         *    -----------
         */
        Hormiga(int posX, int posY, int direccion){
            this.posX = posX;
            this.posY = posY;
            this.direccion = direccion;    
            this.comida = false;        
        }
    }

    class ModeloHormigas{

        int ancho;
        int alto;
        int tamanio;
        int generacion;
        int minFeromonas = 1;
        int maxFeromonas = 150;
        int inicioColX;
        int inicioColY;
        Celda[][] mundo;
        ArrayList<Hormiga> hormigas;
        Random rnd = new Random();

        // HashMap para direcciones contrarias
        final HashMap<Integer, Integer> map = new HashMap<>();

        /**
         * Constructor modelo hormigas
         * @param ancho cantidad de celdas a lo ancho
         * @param alto cantidad de celdas a lo alto
         * @param tamanio tamanio en pixeles de cada celda
         * @param cantidad cantidad de hormigas
         */
        ModeloHormigas(int ancho, int alto, int tamanio, int cantidad){
            this.ancho = ancho;
            this.alto = alto;
            this.tamanio = tamanio;
            this.generacion = 0;
            // opuesto de las direcciones rectas
            map.put(1, 5);
            map.put(3, 7);
            map.put(5, 1);
            map.put(7, 3);
            // opuesto de las reicciones diagonales
            map.put(0, 4);
            map.put(2, 6);
            map.put(4, 0);
            map.put(6, 2);
            // Mundo
            mundo = new Celda[alto][ancho];            
            for (int i = 0; i < alto; i++){
                for (int j = 0; j < ancho; j++){
                    mundo[i][j] = new Celda(i, j, 0);
                }
            }
            // Agregamos colonia 
            this.inicioColX = 9; // celda 10
            this.inicioColY = 9; // Celda 10
            int tamanioCol = 4;
            for (int i = 0 ; i < tamanioCol ; i++){
                for (int j = 0; j < tamanioCol ; j++ ){
                    mundo[inicioColX + i][inicioColY + j].estado = 1;
                }
            }
            // Agregamos comida
            int inicioComX = 69; // celda 70
            int inicioComY = 89; // celda 90
            int tamanioCom = 10;
            for (int  i = 0; i < tamanioCom ; i++){
                for (int j = 0; j < tamanioCom ; j++){
                    mundo[inicioComX + i][inicioComY + j].estado = 2;
                }
            }
            // Agregamos Hormigas
            hormigas = new ArrayList<>();
            for (int i = 0; i < cantidad; i++){  
                hormigas.add(new Hormiga(inicioColX, inicioColY, rnd.nextInt(8)));
            }

        }

        /**
         * Regresa una direccion aleatoria (izquierda, derecha, frente)
         * @param direccion base
         * @return direccion.
         */
        public int direccionAleatoriaFrente(int direccion){

            int resultado = -1;
            switch (direccion) {
                case 0:
                    int[] op0 = {7,0,1};
                    resultado = op0[rnd.nextInt(3)];
                    break;
                case 1:
                    int[] op1 = {0,1,2};
                    resultado = op1[rnd.nextInt(3)];
                    break;
                case 2:
                    int[] op2 = {1,2,3};
                    resultado = op2[rnd.nextInt(3)];
                    break;
                case 3:
                    int[] op3 = {2,3,4}; 
                    resultado = op3[rnd.nextInt(3)];                   
                    break;
                case 4:
                    int[] op4 = {3,4,5};
                    resultado = op4[rnd.nextInt(3)];
                    break;
                case 5:
                    int[] op5 = {4,5,6};
                    resultado = op5[rnd.nextInt(3)];
                    break;
                case 6:
                    int[] op6 = {5,6,7};
                    resultado = op6[rnd.nextInt(3)];
                    break;
                case 7:
                    int[] op7 = {6,7,0};
                    resultado = op7[rnd.nextInt(3)];
                    break;
            }

            return resultado;
        }

        /**
         * Determina la direccion a moverse dependiendo
         * de la cantidad de feromonas en las celdas
         * a las que se puede mover.
         * @param h hormiga
         * @param direccion de la hormiga
         * @return direccion a la cual moverse
         * regresa -1 si no puede moverse dada
         * la direccion de la hormiga.
         */
        public int direccionAMoverse(Hormiga h, int direccion){

            int dic1 = -1; // direccion 1
            int dic2 = -1; // direccion 2
            int dic3 = -1; // direccion 3

            double p_d1 = 0.0; // probabilidad de moverse a dic1
            double p_d2 = 0.0; // probabilidad de moverse a dic2
            double p_d3 = 0.0; // probabilidad de moverse a dic3
    
            int resultado = -1;

            switch (direccion) {
                case 0:
                    // 7 0 1
                    dic1 = 7;
                    dic2 = 0;
                    dic3 = 1;
                    break;
                case 1:
                    // 0 1 2
                    dic1 = 0;
                    dic2 = 1;
                    dic3 = 2;
                    break;
                case 2:
                    // 1 2 3
                    dic1 = 1;
                    dic2 = 2;
                    dic3 = 3;
                    break;
                case 3:
                    // 2 3 4
                    dic1 = 2;
                    dic2 = 3;
                    dic3 = 4;                  
                    break;
                case 4:
                    // 3 4 5
                    dic1 = 3;
                    dic2 = 4;
                    dic3 = 5;
                    break;
                case 5:
                    // 4 5 6
                    dic1 = 4;
                    dic2 = 5;
                    dic3 = 6;
                    break;
                case 6:
                    // 5 6 7
                    dic1 = 5;
                    dic2 = 6;
                    dic3 = 7;
                    break;
                case 7:
                    // 6 7 0
                    dic1 = 6;
                    dic2 = 7;
                    dic3 = 0;
                    break;
            }
            
            // feromonas de las celdas
            double f_d1 = celdaAMoverse(h, dic1) == null ? 0.0: celdaAMoverse(h, dic1).feromonas;
            double f_d2 = celdaAMoverse(h, dic2) == null ? 0.0: celdaAMoverse(h, dic2).feromonas;
            double f_d3 = celdaAMoverse(h, dic3) == null ? 0.0: celdaAMoverse(h, dic3).feromonas;

            // Sumatoria de las feromonas
            double sumf = f_d1 + f_d2 + f_d3;

            // Probabilidades
            p_d1 = f_d1 / sumf;
            p_d2 = f_d2 / sumf;
            p_d3 = f_d3 / sumf;

            // Numero random
            double num = rnd.nextDouble();

            // Decision
            if (num < p_d1){
                resultado = dic1;
            }else if (num < p_d1 + p_d2){
                resultado = dic2;
            }else
                resultado = dic3;

            return resultado;
        }

        /**
         * Determina si una hormiga puede moverse dada
         * su posicion y una direccion
         * @param h hormiga a mover.
         * @param direccion base
         * @return true si puede moverse,
         * false en caso contrario.
         */
        public boolean puedeMoverse(Hormiga h, int direccion){

            int x = h.posX;
            int y = h.posY;

            int newX;
            int newY;

            switch (direccion) {
                case 0:
                    newX = x-1;
                    newY = y-1;
                    if (( (0 <= newX) && (newX <= alto-1) ) && ( (0 <= newY) && (newY <= ancho-1))){
                        return true;
                    }
                    break;
                case 1: 
                    newX = x-1;
                    newY = y;                   
                    if (( (0 <= newX) && (newX <= alto-1) ) && ( (0 <= newY) && (newY <= ancho-1))){
                        return true;
                    }
                    break;
                case 2:
                    newX = x-1;
                    newY = y+1;
                    if (( (0 <= newX) && (newX <= alto-1) ) && ( (0 <= newY) && (newY <= ancho-1))){
                        return true;
                    }
                    break;
                case 3:
                    newX = x;
                    newY = y+1;
                    if (( (0 <= newX) && (newX <= alto-1) ) && ( (0 <= newY) && (newY <= ancho-1))){
                        return true;
                    }
                    break;
                case 4:
                    newX = x+1;
                    newY = y+1;
                    if (( (0 <= newX) && (newX <= alto-1) ) && ( (0 <= newY) && (newY <= ancho-1))){
                        return true;
                    }
                    break;
                case 5:
                    newX = x+1;
                    newY = y;
                    if (( (0 <= newX) && (newX <= alto-1) ) && ( (0 <= newY) && (newY <= ancho-1))){
                        return true;
                    }
                    break;
                case 6:
                    newX = x+1;
                    newY = y-1;
                    if (( (0 <= newX) && (newX <= alto-1) ) && ( (0 <= newY) && (newY <= ancho-1))){
                        return true;
                    }
                    break;
                case 7:
                    newX = x;
                    newY = y-1;
                    if (( (0 <= newX) && (newX <= alto-1) ) && ( (0 <= newY) && (newY <= ancho-1))){
                        return true;
                    }
                    break;
            }

            return false;

        }

        /**
         * Regresa la celda a la cual se puede mover
         * una hormiga dada una direccion. Regresa
         * null en caso de que no se pueda mover
         * a una celda con esa direccion.
         * @param h hormiga
         * @param direccion de la hormiga
         * @return Celda si se puede mover,
         * null en caso contrario.
         */
        public Celda celdaAMoverse(Hormiga h, int direccion){

            int x = h.posX;
            int y = h.posY;

            int newX;
            int newY;

            switch (direccion) {
                case 0:
                    newX = x-1;
                    newY = y-1;
                    if (( (0 <= newX) && (newX <= alto-1) ) && ( (0 <= newY) && (newY <= ancho-1))){
                        return mundo[newX][newY];
                    }
                    break;
                case 1: 
                    newX = x-1;
                    newY = y;                   
                    if (( (0 <= newX) && (newX <= alto-1) ) && ( (0 <= newY) && (newY <= ancho-1))){
                        return mundo[newX][newY];
                    }
                    break;
                case 2:
                    newX = x-1;
                    newY = y+1;
                    if (( (0 <= newX) && (newX <= alto-1) ) && ( (0 <= newY) && (newY <= ancho-1))){
                        return mundo[newX][newY];
                    }
                    break;
                case 3:
                    newX = x;
                    newY = y+1;
                    if (( (0 <= newX) && (newX <= alto-1) ) && ( (0 <= newY) && (newY <= ancho-1))){
                        return mundo[newX][newY];
                    }
                    break;
                case 4:
                    newX = x+1;
                    newY = y+1;
                    if (( (0 <= newX) && (newX <= alto-1) ) && ( (0 <= newY) && (newY <= ancho-1))){
                        return mundo[newX][newY];
                    }
                    break;
                case 5:
                    newX = x+1;
                    newY = y;
                    if (( (0 <= newX) && (newX <= alto-1) ) && ( (0 <= newY) && (newY <= ancho-1))){
                        return mundo[newX][newY];
                    }
                    break;
                case 6:
                    newX = x+1;
                    newY = y-1;
                    if (( (0 <= newX) && (newX <= alto-1) ) && ( (0 <= newY) && (newY <= ancho-1))){
                        return mundo[newX][newY];
                    }
                    break;
                case 7:
                    newX = x;
                    newY = y-1;
                    if (( (0 <= newX) && (newX <= alto-1) ) && ( (0 <= newY) && (newY <= ancho-1))){
                        return mundo[newX][newY];
                    }
                    break;
            }

            return null;
        }

        /**
         * Mueve a la hormiga a una nueva posicion
         * dada una direccion,
         * @param h hormiga a mover
         * @param direccion a mover
         */
        public void moverHormiga(Hormiga h, int direccion){

            int x = h.posX;
            int y = h.posY;

            int newX;
            int newY;

            switch (direccion) {
                case 0:
                    newX = x-1;
                    newY = y-1;
                    if (( (0 <= newX) && (newX <= alto-1) ) && ( (0 <= newY) && (newY <= ancho-1))){
                        h.posX = newX;
                        h.posY = newY;
                        h.direccion = direccion;
                    }
                    break;
                case 1: 
                    newX = x-1;
                    newY = y;                   
                    if (( (0 <= newX) && (newX <= alto-1) ) && ( (0 <= newY) && (newY <= ancho-1))){
                        h.posX = newX;
                        h.posY = newY;
                        h.direccion = direccion;
                    }
                    break;
                case 2:
                    newX = x-1;
                    newY = y+1;
                    if (( (0 <= newX) && (newX <= alto-1) ) && ( (0 <= newY) && (newY <= ancho-1))){
                        h.posX = newX;
                        h.posY = newY;
                        h.direccion = direccion;
                    }
                    break;
                case 3:
                    newX = x;
                    newY = y+1;
                    if (( (0 <= newX) && (newX <= alto-1) ) && ( (0 <= newY) && (newY <= ancho-1))){
                        h.posX = newX;
                        h.posY = newY;
                        h.direccion = direccion;
                    }
                    break;
                case 4:
                    newX = x+1;
                    newY = y+1;
                    if (( (0 <= newX) && (newX <= alto-1) ) && ( (0 <= newY) && (newY <= ancho-1))){
                        h.posX = newX;
                        h.posY = newY;
                        h.direccion = direccion;
                    }
                    break;
                case 5:
                    newX = x+1;
                    newY = y;
                    if (( (0 <= newX) && (newX <= alto-1) ) && ( (0 <= newY) && (newY <= ancho-1))){
                        h.posX = newX;
                        h.posY = newY;
                        h.direccion = direccion;
                    }
                    break;
                case 6:
                    newX = x+1;
                    newY = y-1;
                    if (( (0 <= newX) && (newX <= alto-1) ) && ( (0 <= newY) && (newY <= ancho-1))){
                        h.posX = newX;
                        h.posY = newY;
                        h.direccion = direccion;
                    }
                    break;
                case 7:
                    newX = x;
                    newY = y-1;
                    if (( (0 <= newX) && (newX <= alto-1) ) && ( (0 <= newY) && (newY <= ancho-1))){
                        h.posX = newX;
                        h.posY = newY;
                        h.direccion = direccion;
                    }
                    break;
            }

        }

        /**
         * Aumenta las feromonas en la casilla
         * donde esta la hormiga.
         * @param h hormiga
         */
        public void dejarFeromona(Hormiga h){
            Celda c = mundo[h.posX][h.posY];
            c.feromonas = maxFeromonas;
        }

        /**
         * Evapora las feromonas poco a poco
         */
        public void evapora(){
            for (int i = 0; i < alto; i++){
                for (int j = 0; j < ancho; j++){
                    Celda c = mundo[i][j];
                    if (c.feromonas > minFeromonas){
                        c.feromonas--;
                    }
                }
            }
        }

        /**
         * Determina si hay comida dada
         * la posicion de la hormiga y
         * la direccion.
         * @param h hormiga
         * @param direccion direccion
         * @return true si hay comida, false en caso contrario
         */
        public boolean hayComida(Hormiga h, int direccion){
            Celda c = celdaAMoverse(h, direccion);            
            return c.estado == 2;
        }

                /**
         * Determina si hay colonia dada
         * la posicion de la hormiga y
         * la direccion.
         * @param h hormiga
         * @param direccion direccion
         * @return true si hay colonia, false en caso contrario
         */
        public boolean hayColonia(Hormiga h, int direccion){
            Celda c = celdaAMoverse(h, direccion);            
            return c.estado == 1;
        }

        /**
         * Regresa la distancia de la celda al nido
         * @param c celda
         * @return la distancia euclideana
         */
        public double distanciaAlNido(Celda c){

            Celda n = mundo[inicioColX][inicioColY];

            int x1 = n.celdaX;
            int y1 = n.celdaY;

            int x2 = c.celdaX;
            int y2 = c.celdaY;

            // Distancia euclideana
            double d = Math.sqrt(Math.pow((x2-x1), 2) + Math.pow((y2-y1), 2));

            return d;

        }

        /**
         * Mueve la hormiga a la celda a su
         * alrededor que este mas cerca al
         * nido.
         * @param h hormiga a mover.
         */
        public void moverHormigaAlNido(Hormiga h){

            int direccion = -1;
            double min = -1;

            for (int i = 0; i < 8 ; i++){
                Celda c  = celdaAMoverse(h, i);
                if (c != null){
                    double d = distanciaAlNido(c);
                    if (min != -1){
                        // comparamos
                        if(d < min){
                            direccion = i;
                            min = d;
                        }
                    }else{
                        direccion = i;
                        min = d;
                    }
                }
            }

            moverHormiga(h, direccion);                        
        }

        /**
         * Determina si la hormiga esta en el nido
         * @param h hormiga
         * @return true si esta en el nido, false
         * en caso contrario
         */
        public boolean estaEnElNido(Hormiga h){
            return (inicioColX == h.posX) && (inicioColY == h.posY);
        }

        /**
         * Siguiente ejecucion del algoritmo
         */
        public void siguiente(){
            for (Hormiga h : hormigas){
                boolean next = false;
                do{
                    if (h.comida){
                        if (estaEnElNido(h)){
                            h.comida = false;
                        }else{
                            moverHormigaAlNido(h);
                        }
                        next = true;              
                    }else{
                        int dir = direccionAMoverse(h, h.direccion);                        
                        if (puedeMoverse(h, dir)){
                            moverHormiga(h, dir);
                            if (mundo[h.posX][h.posY].estado == 2){
                                h.comida = true;
                            }
                            next = true;
                        }else{
                            h.direccion = map.get(h.direccion);
                        }
                    }
                }while(!next);                         
            }            
            generacion += 1;  
            /*
            evapora();
            for (Hormiga h : hormigas){
                boolean next = false;
                do{
                    // int dir = direccionAleatoriaFrente(h.direccion);  
                    int dir = direccionAMoverse(h, h.direccion);
                    if (puedeMoverse(h, dir)){
                        if (hayComida(h, dir) || hayColonia(h, dir)){
                            moverHormiga(h, dir);
                            dejarFeromona(h);
                            // hacemos que la hormiga se oriente a la direccion contraria
                            h.direccion = map.get(h.direccion);
                        }else{
                            dejarFeromona(h);
                            moverHormiga(h, dir);
                        }                                                                                                  
                        next = true;
                    }else{
                        // hacemos que la hormiga se oriente a la direccion contraria
                        h.direccion = map.get(h.direccion);
                    }
                }while(!next);                         
            }            
            generacion += 1;    
            */        
        }
    }

    public static void main (String args[]){
        PApplet.main(new String[]{ "hormigas.Hormigas" });
    }


}