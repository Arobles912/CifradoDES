/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package cifradodes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 *
 * @author Ángel
 */
public class CifradoDES {

    Cipher ecipher;
    Cipher dcipher;

    CifradoDES(SecretKey key) throws Exception {
        ecipher = Cipher.getInstance("DES");
        dcipher = Cipher.getInstance("DES");
        ecipher.init(Cipher.ENCRYPT_MODE, key);
        dcipher.init(Cipher.DECRYPT_MODE, key);
    }

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

    public static void main(String[] args) {
        try {
            final String ruta = "D:\\Instituto\\Programacion de servicios y procesos\\Hola.txt";
            final String ruta2 = "D:\\Instituto\\Programacion de servicios y procesos\\Hola2.txt";
            File archivo = new File(ruta);
            File archivoEncriptado = new File(ruta2);
            FileReader fr = new FileReader(archivo);
            BufferedReader br = new BufferedReader(fr);
            FileReader fr2 = new FileReader(archivoEncriptado);
            BufferedReader br2 = new BufferedReader(fr2);
            FileWriter fw = new FileWriter(archivoEncriptado);
            BufferedWriter bw = new BufferedWriter(fw);
            String line = "";
            String textoEncriptado = "";
            SecretKey key;
            key = KeyGenerator.getInstance("DES").generateKey();
            CifradoDES encrypter = new CifradoDES(key);
            while ((line = br.readLine()) != null) {
                System.out.println("Linea: " + line);
                textoEncriptado = encrypter.encriptar(line);
                bw.write(textoEncriptado);
                bw.newLine();
                System.out.println("Texto encriptado: " + textoEncriptado);
            }
            br.close();
            bw.close();
            String line2 = "";
            String desencriptado = "";
            while ((line2 = br2.readLine()) != null) {
                desencriptado = encrypter.desencriptar(line2);
                System.out.println("Texto desencriptado: " + desencriptado);
            }
           br2.close();
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
