package service;

public interface IConvierteDatos {

    //Obtiene datos del api y distribuye a la clase para organizar
    <T> T obtenerDatos(String json, Class<T> clase);


}
