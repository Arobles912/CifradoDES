# CifradoDES
Aplicacion de cifrado de ficheros utilizando el cifrado DES en Java.
## Descripción
Es un programa sencillo en el que se pedirá al usuario introducir la ruta de un archivo (preferiblemente un documento de texto como un .txt), y este creara otro documento con el contenido cifrado, acto seguido, descifrará ese mismo contenido, y lo mostrará por pantalla.
## Código

### Constructor y campos globales
Tiene dos instancias de Cipher, una para el cifrado (ecipher) y otra para el descifrado (dcipher).
El constructor inicializa las instancias de cifrado y descifrado con la clave proporcionada.
```java
public class CifradoDES {

    Cipher ecipher;
    Cipher dcipher;

    CifradoDES(SecretKey key) throws Exception {
        ecipher = Cipher.getInstance("DES");
        dcipher = Cipher.getInstance("DES");
        ecipher.init(Cipher.ENCRYPT_MODE, key);
        dcipher.init(Cipher.DECRYPT_MODE, key);
    }
```

### Métodos principales
Tiene dos métodos principales: encriptar(String str) y desencriptar(String str), que toman una cadena y devuelven la versión cifrada o descifrada de esa cadena.
```java
 public String encriptar(String str) throws Exception {
        // Encode the string into bytes using utf-8
        byte[] utf8 = str.getBytes("UTF8");

        // Encrypt
        byte[] enc = ecipher.doFinal(utf8);

        // Encode bytes to base64 to get a string
        return Base64.getEncoder().encodeToString(enc);

    }

    public String desencriptar(String str) throws Exception {
        // Decode base64 to get bytes
        byte[] dec = Base64.getDecoder().decode(str);

        byte[] utf8 = dcipher.doFinal(dec);

        // Decode using utf-8
        return new String(utf8, "UTF8");
    }
```
### Método creación archivo encriptado
Este método toma un archivo de texto y crea un archivo encriptado a partir de él.
Lee el archivo línea por línea, lo cifra usando el método encriptar() y escribe las líneas cifradas en un nuevo archivo.
```java
public static void creacionArchivoEncriptado(File archivo, File archivoEncriptado, CifradoDES encrypter) {
        try {
            FileReader fr = new FileReader(archivo);
            BufferedReader br = new BufferedReader(fr);
            FileReader fr2 = new FileReader(archivoEncriptado);

            FileWriter fw = new FileWriter(archivoEncriptado);
            BufferedWriter bw = new BufferedWriter(fw);
            String line = "";
            String textoEncriptado = "";
            while ((line = br.readLine()) != null) {
                System.out.println("Linea: " + line);
                textoEncriptado = encrypter.encriptar(line);
                bw.write(textoEncriptado);
                bw.newLine();
                System.out.println("Texto encriptado: " + textoEncriptado);
            }
            br.close();
            bw.close();
        } catch (Exception e) {
            e.getMessage();
        }
    }
```

### Main
En este método se inicia el programa, solicita al usuario que introduzca la ruta del archivo que se desea encriptar.
Genera una clave secreta DES utilizando KeyGenerator, y crea una instancia de CifradoDES con la clave generada.
Verifica si el archivo especificado existe, si es así, crea un archivo para almacenar el texto encriptado.
Llama al método creacionArchivoEncriptado() para realizar la encriptación del archivo, luego, lee el archivo encriptado línea por línea y lo desencripta utilizando el método desencriptar().
Y finalmente, imprime el texto desencriptado en la consola.
```java
 public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.println("Introduce la ruta del archivo a encriptar:");
            final String ruta = sc.nextLine();
            final String rutaFormated = ruta.substring(0, ruta.length() - 4);
            final String ruta2 = rutaFormated + "Encriptado.txt";
            SecretKey key;
            key = KeyGenerator.getInstance("DES").generateKey();
            CifradoDES encrypter = new CifradoDES(key);
            File archivo = new File(ruta);
            if (!archivo.isFile()) {
                System.out.println("No se ha podido encontrar el archivo especificado.");
            } else {
                File archivoEncriptado = new File(ruta2);
                if (!archivoEncriptado.isFile()) {
                    archivoEncriptado.createNewFile();
                }
                creacionArchivoEncriptado(archivo, archivoEncriptado, encrypter);
                FileReader fr = new FileReader(archivoEncriptado);
                BufferedReader br = new BufferedReader(fr);
                String line2 = "";
                String desencriptado = "";
                while ((line2 = br.readLine()) != null) {
                    desencriptado = encrypter.desencriptar(line2);
                    System.out.println("Texto desencriptado: " + desencriptado);
                }
                br.close();
            }

        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
```
### Código de cifrado DES (métodos Cifrar() y Descifrar() y constructor)
https://www.knowledgefactory.net/2019/10/java-des-encryption-and-decryption-with-example.html
