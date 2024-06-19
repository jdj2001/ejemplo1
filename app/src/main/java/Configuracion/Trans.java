package Configuracion;

public class Trans
{
    //VERSION DB
    public static final int Version = 1;
    //nombre BD
    public static final String DBname = "PM012P";//constante, no se puede modificar a menos que se sobreescriba
    //consume muchos recursos de memoria
    //tabla personas
    public static final String TablePersonas = "personas";
    //propiedades
    public static final String id = "id";
    public static final String nombres = "nombres";
    public static final String apellidos = "apellidos";
    public static final String edad = "edad";
    public static final String correo = "correo";
    public static final String foto = "foto";

    // ELEMENTOS DDL PARA CREAR OBJETOS DE BD
    //CREAR TABLA
    public static final String CreateTablePersonas = "CREATE TABLE " + TablePersonas + " ( " + "id INTEGER PRIMARY KEY AUTOINCREMENT, nombres TEXT, apellidos TEXT, edad INTEGER, correo TEXT, foto TEXT )";

    public static final String SelectAllPerson = "SELECT * FROM " + TablePersonas;

    public static final String DropTablePersonas = "DROP TABLE IF EXISTS " + TablePersonas;
}

