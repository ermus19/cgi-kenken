
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * @author Akiyoshi
 */
public class SubirArchivos {

    private final static String CURRENTPATH = System.getProperty("user.dir"); //RUTA

    public static void main(String args[]) throws IOException {

        int contador = 0;
        String FileName = "";

        while (true) {
            FileName = "kenken" + contador + ".xml";
            if (!getList(CURRENTPATH, FileName)) {
                break;
            } else {
                contador++;
            }
        }

        File f = new File(FileName);
        writeFile(f);

        int errCheck = checkXML(f);

        if (errCheck == 0) {
            buildLanding(" Archivo subido y validado correctamente.<br> Nombre del archivo creado : " + FileName);
        } else if (errCheck == 1) {
            buildLanding(" Se ha producido un error, el archivo no es válido.");
            try {
                f.delete();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private static int checkXML(File validar) throws IOException {

        String FileName = validar.getName();
        ValidadorXML validador = new ValidadorXML(FileName, CURRENTPATH);
        int status = validador.validate();
        return status;
    }

    private static boolean getList(String rute, String check) {

        File folder = new File(rute); //Ruta donde se guardan los xml 
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                String extension = "";
                String name = listOfFiles[i].getName();
                int index = listOfFiles[i].getName().lastIndexOf('.');
                if (index > 0) {
                    extension = listOfFiles[i].getName().substring(index + 1);
                }
                if (extension.equalsIgnoreCase("xml") && name.equalsIgnoreCase(check)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void buildLanding(String status) {

        String html = "";
        html += "Content-Type: text/html; charset='utf-8'\n\n";
        html += "<html>\n"
                + "<head>\n"
                + "	<title>KenKen</title>\n"
                + "	<meta name=\"author\" content=\"Ruben Rodriguez 2016\">\n"
                + "	<meta charset = \"UTF-8\">\n"
                + "	<link rel=\"stylesheet\" href=\"../css/style.css\">		<!--RUTA-->\n"
                + "</head>\n"
                + "<body>\n"
                + "	<a href=\"../index.html\"><h1 id = \"encabezado\">Kenken</h1></a>\n"
                + "	<h2 id = \"subencabezado\">Resultado de subida de archivo...</h2>\n"
                + "	<ul id = \"menu\">\n"
                + "         <li><a href=\"../index.html\">Principal</a></li>		<!--RUTA-->\n"
                + "         <li><a href=\"../upload.html\">Cargar kenken</a></li>		<!--RUTA-->\n"
                + "         <li><a href=\"../cgi-bin/list.cgi\">Pasatiempos disponibles</a></li>		<!--RUTA-->\n"
                + "	</ul>\n"
                + "         <div id = \"contenido\">"
                + "             <p>" + status + "</p>\n"
                + "         </div>\n"
                + "		<footer id = \"foot\">\n"
                + "                 <address>\n"
                + "			Written by <a href=\"mailto:rrodrc04@estudiantes.unileon.es\">Ruben Rodriguez</a><br>\n"
                + "                 </address>\n"
                + "             2016\n"
                + "            </footer>\n"
                + "</body>\n"
                + "</html>";

        System.out.println(html);

    }

    private static void writeFile(File f) {
        
        StringBuffer sc = new StringBuffer("");
        Scanner x = new Scanner(System.in, "UTF-8");
        String currentLine;
        boolean write = false;

        while (x.hasNextLine()) {

            currentLine = x.nextLine() + "\n";
            
            if(currentLine.trim().startsWith("</kenken>")){
                sc.append(currentLine);
                break;
            }
            
            if(write){
                sc.append(currentLine);
            }

            if (currentLine.trim().startsWith("<?xml")) {
                sc.append(currentLine);
                write = true;
            }
            
            
        }
        
        String Final = sc.toString();

        if (!f.exists()) {
            try {
                FileWriter fwriter = new FileWriter(f);
                BufferedWriter bwriter = new BufferedWriter(fwriter);
                bwriter.write(Final);
                bwriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
