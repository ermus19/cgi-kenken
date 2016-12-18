
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * @author Akiyoshi
 */
public class ListarArchivos {

    private final static String CURRENTPATH = System.getProperty("user.dir"); //RUTA
    private static ArrayList<Archivo> ListaArchivos = new ArrayList<>();

    public static class ComparadorKenken implements Comparator<Archivo> {

        @Override
        public int compare(Archivo xml1, Archivo xml2) {
            return xml1.getTam() - xml2.getTam();
        }
    }

    public static class Archivo {

        private final String name;
        private final int tam;

        Archivo(String name, int tam) {
            this.name = name;
            this.tam = tam;
        }

        public String getName() {
            return name;
        }

        public int getTam() {
            return tam;
        }

    }

    public static void main(String args[]) {
        String html = "Content-Type: text/html; charset='utf-8'\n\n";
        html += "<html>\n"
                + "<head>\n"
                + "	<title>KenKen</title>\n"
                + "	<meta name=\"author\" content=\"Ruben Rodriguez 2016\">\n"
                + "	<meta charset = \"UTF-8\">\n"
                + "	<link rel=\"stylesheet\" href=\"../css/style.css\">  <!--RUTA-->\n"
                + "</head>\n"
                + "<body>\n"
                + "	<a href=\"../index.html\"><h1 id = \"encabezado\">Kenken</h1></a>\n"
                + "	<h1 id = \"subencabezado\">Listado de solitarios disponibles:</h1>\n"
                + "	<ul id = \"menu\">\n"
                + "         <li><a href=\"../index.html\">Principal</a></li>		<!--RUTA-->\n"
                + "         <li><a href=\"../upload.html\">Cargar kenken</a></li>		<!--RUTA-->\n"
                + "         <li><a href=\"../cgi-bin/list.cgi\">Pasatiempos disponibles</a></li>		<!--RUTA-->\n"
                + "	</ul>\n"
                + "         <div id = \"contenido\">\n"
                + "             <br>\n";

        String definitiva = getList(CURRENTPATH, html);

        definitiva += " <br>\n"
                + "     </div>\n"
                + "         <footer id = \"foot\">\n"
                + "             <address>\n"
                + "                 Written by <a href=\"mailto:rrodrc04@estudiantes.unileon.es\">Ruben Rodriguez</a><br>\n"
                + "		</address>\n"
                + "                 © 2016\n"
                + "         </footer>\n"
                + "</body>\n"
                + "</html>";
        System.out.println(definitiva);

    }

    private static String getList(String rute, String html) {

        File folder = new File(rute); //Ruta donde se guardan los xml 
        File[] listOfFiles = folder.listFiles();
        int count = 0;

        for (int i = listOfFiles.length - 1; i >= 0; i--) {
            if (listOfFiles[i].isFile()) {
                String extension = "";
                int index = listOfFiles[i].getName().lastIndexOf('.');
                if (index > 0) {
                    extension = listOfFiles[i].getName().substring(index + 1);
                }
                if (extension.equalsIgnoreCase("xml")) {
                    count++;
                    ValidadorXML validador = new ValidadorXML(listOfFiles[i].getName(), CURRENTPATH);
                    int tam = validador.parseTam();
                    String CurrentName = listOfFiles[i].getName();
                    Archivo nuevo = new Archivo(CurrentName, tam);
                    ListaArchivos.add(nuevo);
                }
            }
        }
        if (count == 0) {
            html += "               <p>Ningún archivo encontrado...  :(</p>\n";
            return html;
        } else {

            html += "               <p>Pulsa sobre el solitario deseado para mostrarlo e interactuar con él!</p>\n";

            Collections.sort(ListaArchivos, new ComparadorKenken());
            
            int tam = -1;

            for (int i = 0; i < ListaArchivos.size(); i++) {
                
                if(tam != ListaArchivos.get(i).getTam()){
                    html +=             "<h3 id = \"sub\">Tamaño " + ListaArchivos.get(i).getTam() +" </h3>\n";
                    tam = ListaArchivos.get(i).getTam();
                }
                html += "               <p style = 'text-indent:30pt'>";
                html += "               <a href=\"show.cgi?" + ListaArchivos.get(i).getName() + "\">" + "- " + ListaArchivos.get(i).getName()
                        + " con tamaño de : " + ListaArchivos.get(i).getTam() + "x" + +ListaArchivos.get(i).getTam() + "</a><br>\n";
                html += "               </p>\n";
            }

            return html;
        }
    }

}
