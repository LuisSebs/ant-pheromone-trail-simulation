package hormigas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import processing.core.PApplet;

public class Hormigas extends PApplet {

    int alto = 100;
    int ancho = 150;
    int celda = 4;
    int hormigas = 1;
    ModeloHormigas modelo;

    @Override
    public void setup(){
        background(50);
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
                // Dibujamos colonia
                if (modelo.mundo[i][j].estado == 1){
                    fill(0,0,255);
                }else if (modelo.mundo[i][j].estado == 2){ // Dibujamos comida
                    fill(255,0,0);
                } else {
                    fill(50);
                }             
                rect(j * celda, i * celda, celda, celda);
            }
        }

        // Dibujamos hormigas
        for (Hormiga h : modelo.hormigas){
            fill(0,255,0);
            rect(h.posX * modelo.tamanio, h.posY * modelo.tamanio, modelo.tamanio, modelo.tamanio);
        }

        // Siguiente paso
        modelo.siguiente();
        
    }

    class Celda {

        int celdaX, celdaY;
        int estado;
        int feromonas;

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
            this.feromonas = 0;
        }
    }

    class Hormiga {

        int posX;
        int posY;
        int direccion;

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
        }
    }

    class ModeloHormigas{

        int ancho;
        int alto;
        int tamanio;
        int generacion;
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
            int inicioColX = 10;
            int inicioColY = 10;
            int tamanioCol = 4;
            for (int i = 0 ; i < tamanioCol ; i++){
                for (int j = 0; j < tamanioCol ; j++ ){
                    mundo[inicioColX + i][inicioColY + j].estado = 1;
                }
            }
            // Agregamos comida
            int inicioComX = 80;
            int inicioComY = 100;
            int tamanioCom = 4;
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
                    if (( (0 <= newX) && (newX <= alto) ) && ( (0 <= newY) && (newY <= ancho))){
                        return true;
                    }
                    break;
                case 1: 
                    newX = x-1;
                    newY = y;                   
                    if (( (0 <= newX) && (newX <= alto) ) && ( (0 <= newY) && (newY <= ancho))){
                        return true;
                    }
                    break;
                case 2:
                    newX = x-1;
                    newY = y+1;
                    if (( (0 <= newX) && (newX <= alto) ) && ( (0 <= newY) && (newY <= ancho))){
                        return true;
                    }
                    break;
                case 3:
                    newX = x;
                    newY = y+1;
                    if (( (0 <= newX) && (newX <= alto) ) && ( (0 <= newY) && (newY <= ancho))){
                        return true;
                    }
                    break;
                case 4:
                    newX = x+1;
                    newY = y+1;
                    if (( (0 <= newX) && (newX <= alto) ) && ( (0 <= newY) && (newY <= ancho))){
                        return true;
                    }
                    break;
                case 5:
                    newX = x+1;
                    newY = y;
                    if (( (0 <= newX) && (newX <= alto) ) && ( (0 <= newY) && (newY <= ancho))){
                        return true;
                    }
                    break;
                case 6:
                    newX = x+1;
                    newY = y-1;
                    if (( (0 <= newX) && (newX <= alto) ) && ( (0 <= newY) && (newY <= ancho))){
                        return true;
                    }
                    break;
                case 7:
                    newX = x;
                    newY = y-1;
                    if (( (0 <= newX) && (newX <= alto) ) && ( (0 <= newY) && (newY <= ancho))){
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
                    if (( (0 <= newX) && (newX <= alto) ) && ( (0 <= newY) && (newY <= ancho))){
                        return mundo[newX][newY];
                    }
                    break;
                case 1: 
                    newX = x-1;
                    newY = y;                   
                    if (( (0 <= newX) && (newX <= alto) ) && ( (0 <= newY) && (newY <= ancho))){
                        return mundo[newX][newY];
                    }
                    break;
                case 2:
                    newX = x-1;
                    newY = y+1;
                    if (( (0 <= newX) && (newX <= alto) ) && ( (0 <= newY) && (newY <= ancho))){
                        return mundo[newX][newY];
                    }
                    break;
                case 3:
                    newX = x;
                    newY = y+1;
                    if (( (0 <= newX) && (newX <= alto) ) && ( (0 <= newY) && (newY <= ancho))){
                        return mundo[newX][newY];
                    }
                    break;
                case 4:
                    newX = x+1;
                    newY = y+1;
                    if (( (0 <= newX) && (newX <= alto) ) && ( (0 <= newY) && (newY <= ancho))){
                        return mundo[newX][newY];
                    }
                    break;
                case 5:
                    newX = x+1;
                    newY = y;
                    if (( (0 <= newX) && (newX <= alto) ) && ( (0 <= newY) && (newY <= ancho))){
                        return mundo[newX][newY];
                    }
                    break;
                case 6:
                    newX = x+1;
                    newY = y-1;
                    if (( (0 <= newX) && (newX <= alto) ) && ( (0 <= newY) && (newY <= ancho))){
                        return mundo[newX][newY];
                    }
                    break;
                case 7:
                    newX = x;
                    newY = y-1;
                    if (( (0 <= newX) && (newX <= alto) ) && ( (0 <= newY) && (newY <= ancho))){
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
                    if (( (0 <= newX) && (newX <= alto) ) && ( (0 <= newY) && (newY <= ancho))){
                        h.posX = newX;
                        h.posY = newY;
                        h.direccion = direccion;
                    }
                    break;
                case 1: 
                    newX = x-1;
                    newY = y;                   
                    if (( (0 <= newX) && (newX <= alto) ) && ( (0 <= newY) && (newY <= ancho))){
                        h.posX = newX;
                        h.posY = newY;
                        h.direccion = direccion;
                    }
                    break;
                case 2:
                    newX = x-1;
                    newY = y+1;
                    if (( (0 <= newX) && (newX <= alto) ) && ( (0 <= newY) && (newY <= ancho))){
                        h.posX = newX;
                        h.posY = newY;
                        h.direccion = direccion;
                    }
                    break;
                case 3:
                    newX = x;
                    newY = y+1;
                    if (( (0 <= newX) && (newX <= alto) ) && ( (0 <= newY) && (newY <= ancho))){
                        h.posX = newX;
                        h.posY = newY;
                        h.direccion = direccion;
                    }
                    break;
                case 4:
                    newX = x+1;
                    newY = y+1;
                    if (( (0 <= newX) && (newX <= alto) ) && ( (0 <= newY) && (newY <= ancho))){
                        h.posX = newX;
                        h.posY = newY;
                        h.direccion = direccion;
                    }
                    break;
                case 5:
                    newX = x+1;
                    newY = y;
                    if (( (0 <= newX) && (newX <= alto) ) && ( (0 <= newY) && (newY <= ancho))){
                        h.posX = newX;
                        h.posY = newY;
                        h.direccion = direccion;
                    }
                    break;
                case 6:
                    newX = x+1;
                    newY = y-1;
                    if (( (0 <= newX) && (newX <= alto) ) && ( (0 <= newY) && (newY <= ancho))){
                        h.posX = newX;
                        h.posY = newY;
                        h.direccion = direccion;
                    }
                    break;
                case 7:
                    newX = x;
                    newY = y-1;
                    if (( (0 <= newX) && (newX <= alto) ) && ( (0 <= newY) && (newY <= ancho))){
                        h.posX = newX;
                        h.posY = newY;
                        h.direccion = direccion;
                    }
                    break;
            }
        }

        public void siguiente(){
            for (Hormiga h : hormigas){
                boolean next = false;
                do{
                    int dir = direccionAleatoriaFrente(h.direccion);
                    if (puedeMoverse(h, dir)){
                        moverHormiga(h, dir);
                        System.out.println(h.posX);
                        next = true;
                    }else{
                        h.direccion = map.get(h.direccion);
                    }
                }while(!next);                         
            }
            generacion += 1;
        }
    }

    public static void main (String args[]){
        PApplet.main(new String[]{ "hormigas.Hormigas" });
    }


}