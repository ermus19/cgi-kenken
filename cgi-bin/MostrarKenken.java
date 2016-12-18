
import java.io.File;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

/**
 * @author Akiyoshi
 */
public class MostrarKenken {

    public static class Casilla {

        private final String columna;
        private final String fila;
        private String tipo;
        private String solucion;
        private String operacion;
        private String polId;

        public Casilla(String col, String fil, String type, String op) {
            this.columna = col;
            this.fila = fil;
            this.tipo = type;
            this.operacion = op;
            this.solucion = "";
            this.polId = "";

        }

        public Casilla(String col, String fil, String type) {
            this.columna = col;
            this.fila = fil;
            this.tipo = type;
            this.operacion = "";
            this.solucion = "";
            this.polId = "";
        }

        private Casilla(String col, String fil) {
            this.columna = col;
            this.fila = fil;
            this.tipo = "";
            this.operacion = "";
            this.solucion = "";
            this.polId = "";
        }

        private Casilla(String col, String fil, String type, String op, String sol) {
            this.columna = col;
            this.fila = fil;
            this.tipo = type;
            this.operacion = op;
            this.solucion = sol;
            this.polId = "";
        }

        public void setTipo(String typ) {
            this.tipo = typ;
        }

        public void setPolId(String id) {
            this.polId = id;
        }

        public String getPolId() {
            return polId;
        }

        public String getColumna() {
            return columna;
        }

        public String getTipo() {
            return tipo;
        }

        public String getOperacion() {
            return operacion;
        }

        public void setOperacion(String op) {
            this.operacion = op;
        }

        public String getSolucion() {
            return solucion;
        }

        public void setSolucion(String sol) {
            this.solucion = sol;
        }

        public String getFila() {
            return fila;
        }

    }

    private static final ArrayList<ArrayList<Casilla>> Filas = new ArrayList<>();

    public static void main(String args[]) {

        String queryValue = System.getenv("QUERY_STRING");

        File xml = new File(queryValue);

        if (xml.exists()) {
            parse(xml);
            mapearPoliminos();
            String gamePage = buildGame();
            System.out.println(gamePage);
        } else {
            String errPage = buildLanding("Error, arhivo no encontrado en el servidor ...");
            System.out.println(errPage);

        }

    }

    private static void parse(File xml) {
        try {

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = (Document) dBuilder.parse(xml);
            doc.getDocumentElement().normalize();

            NodeList filas = doc.getElementsByTagName("fila");

            for (int i = 0; i < filas.getLength(); i++) {

                Node filaNode = filas.item(i);

                if (filaNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element filaElement = (Element) filaNode;
                    ArrayList<Casilla> ListaCasillas = new ArrayList<>();

                    if (filas.item(i).hasChildNodes()) {

                        NodeList casillas = filas.item(i).getChildNodes();

                        for (int j = 0; j < casillas.getLength(); j++) {

                            Node casillasNode = casillas.item(j);

                            if (casillasNode.getNodeType() == Node.ELEMENT_NODE) {

                                Element casillaElem = (Element) casillasNode;
                                String fil = String.valueOf(i + 1);
                                String col = casillaElem.getAttribute("numero");
                                String norte = casillaElem.getAttribute("norte");
                                String este = casillaElem.getAttribute("este");
                                String sur = casillaElem.getAttribute("sur");
                                String oeste = casillaElem.getAttribute("oeste");
                                Casilla nuevaCasilla = new Casilla(col, fil);

                                if (norte.equalsIgnoreCase("true") && este.equalsIgnoreCase("false")
                                        && sur.equalsIgnoreCase("false") && oeste.equalsIgnoreCase("true")) {

                                    nuevaCasilla.setTipo("arriba-izquierda");

                                } else if (norte.equalsIgnoreCase("true") && este.equalsIgnoreCase("false")
                                        && sur.equalsIgnoreCase("true") && oeste.equalsIgnoreCase("false")) {

                                    nuevaCasilla.setTipo("arriba-abajo");

                                } else if (norte.equalsIgnoreCase("false") && este.equalsIgnoreCase("true")
                                        && sur.equalsIgnoreCase("false") && oeste.equalsIgnoreCase("true")) {

                                    nuevaCasilla.setTipo("derecha-izquierda");

                                } else if (norte.equalsIgnoreCase("true") && este.equalsIgnoreCase("true")
                                        && sur.equalsIgnoreCase("false") && oeste.equalsIgnoreCase("false")) {

                                    nuevaCasilla.setTipo("arriba-derecha");

                                } else if (norte.equalsIgnoreCase("false") && este.equalsIgnoreCase("false")
                                        && sur.equalsIgnoreCase("true") && oeste.equalsIgnoreCase("true")) {

                                    nuevaCasilla.setTipo("abajo-izquierda");

                                } else if (norte.equalsIgnoreCase("false") && este.equalsIgnoreCase("true")
                                        && sur.equalsIgnoreCase("true") && oeste.equalsIgnoreCase("false")) {

                                    nuevaCasilla.setTipo("abajo-derecha");

                                } else if (norte.equalsIgnoreCase("false") && este.equalsIgnoreCase("true")
                                        && sur.equalsIgnoreCase("true") && oeste.equalsIgnoreCase("true")) {

                                    nuevaCasilla.setTipo("abajo-derecha-izquierda");

                                } else if (norte.equalsIgnoreCase("true") && este.equalsIgnoreCase("true")
                                        && sur.equalsIgnoreCase("false") && oeste.equalsIgnoreCase("true")) {

                                    nuevaCasilla.setTipo("arriba-derecha-izquierda");

                                } else if (norte.equalsIgnoreCase("true") && este.equalsIgnoreCase("true")
                                        && sur.equalsIgnoreCase("true") && oeste.equalsIgnoreCase("false")) {

                                    nuevaCasilla.setTipo("derecha-arriba-abajo");

                                } else if (norte.equalsIgnoreCase("true") && este.equalsIgnoreCase("false")
                                        && sur.equalsIgnoreCase("true") && oeste.equalsIgnoreCase("true")) {

                                    nuevaCasilla.setTipo("izquierda-arriba-abajo");

                                }

                                if (casillas.item(j).hasChildNodes()) {

                                    NodeList operaciones = casillas.item(j).getChildNodes();

                                    for (int x = 0; x < operaciones.getLength(); x++) {

                                        Node operacionesNode = operaciones.item(x);

                                        if (operacionesNode.getNodeType() == Node.ELEMENT_NODE) {

                                            Element operacionesElem = (Element) operacionesNode;

                                            if (operacionesElem.getTextContent().contains("x") || operacionesElem.getTextContent().contains("+")
                                                    || operacionesElem.getTextContent().contains("-") || operacionesElem.getTextContent().contains("÷")) {
                                                String atributo = operacionesElem.getTextContent();
                                                nuevaCasilla.setOperacion(atributo);

                                            } else {
                                                String atributo = operacionesElem.getTextContent();
                                                nuevaCasilla.setSolucion(atributo);
                                            }

                                        }

                                    }

                                }

                                ListaCasillas.add(nuevaCasilla);

                            }
                        }
                    }
                    Filas.add(ListaCasillas);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String buildGame() {
        String htmlCode = "Content-Type: text/html; charset='utf-8'\n\n";
        htmlCode += "<html>\n"
                + "<head>\n"
                + "	<title>KenKen</title>\n"
                + "	<meta name=\"author\" content=\"Ruben Rodriguez 2016\">\n"
                + "	<meta charset = \"UTF-8\">\n"
                + "	<link rel=\"stylesheet\" href=\"../css/style.css\">		<!--RUTA-->\n"
                + "     <script type=\"text/javascript\" src = \"../js/script.js\"></script>                      <!--RUTA-->\n"
                + "</head>\n"
                + "<body>\n"
                + "	<a href=\"../index.html\"><h1 id = \"encabezado\">Kenken</h1></a>\n        <!--RUTA-->"
                + " <div id = \"tabla\">\n"
                + " <p>Inserte números entre 1 y " + String.valueOf(Filas.size()) + " en las casillas:</p>"
                + " <table " + "id = \"kenken." + String.valueOf(Filas.size()) + "\">\n"
                + "    <tbody>\n";

        for (int i = 0; i < Filas.size(); i++) {

            htmlCode += "        <tr>\n";

            for (int j = 0; j < Filas.get(i).size(); j++) {

                htmlCode += "           <td id =\"" + Filas.get(i).get(j).getFila() + ":" + Filas.get(i).get(j).getColumna() + ":" + Filas.get(i).get(j).getPolId() + "\" "
                        + "class =\"" + Filas.get(i).get(j).getTipo() + "\">\n";
                if (!Filas.get(i).get(j).getOperacion().equalsIgnoreCase("")) {
                    htmlCode += "               <span class=\"operacion\">" + Filas.get(i).get(j).getOperacion() + "</span>\n";
                }
                if (!Filas.get(i).get(j).getSolucion().equalsIgnoreCase("")) {
                    htmlCode += "               <input type='text' value= \"" + Filas.get(i).get(j).getSolucion() + "\" maxlength=\"1\" autocomplete=\"off\" onkeyup = \"master(this)\">\n";
                } else {
                    htmlCode += "               <input type='text' maxlength=\"1\" autocomplete=\"off\" onkeyup = \"master(this)\">\n";
                }
                htmlCode += "           </td>\n";
            }

            htmlCode += "        </tr>\n";
        }
        htmlCode += "       </tbody>\n"
                + "   </table>\n"
                + " </div>\n"
                + "<div id = \"console\">\n"
                + "	 <h3 id = \"sub\">Opciones disponibles</h3>\n"
                + "	 <input class=\"button\" type=\"button\" value=\"Comprobar Kenken\" onclick=\"comprobarKenken();\"/>\n"
                + "	 <input class=\"button\" type=\"button\" value=\"Limpiar Kenken\" onclick=\"limpiar();\"/>\n"
                + "	 <input class=\"button\" type=\"button\" value=\"Volver a la página principal...\" onclick= \"window.location = '../index.html';\"/>        <!--RUTA-->\n"
                + "   <h3 id = \"sub\">Eventos:</h3>\n"
                + "	 <textarea id= \"log\" rows=\"24\" cols=\"85\" disabled>\n"
                + "   </textarea>\n"
                + " </div>\n"
                + "</body>\n"
                + "</html>";
               
        return htmlCode;
    }

    private static String buildLanding(String status) {
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
                + "	<ul id = \"menu\">\n"
                + "         <li><a href=\"../index.html\">Principal</a></li>		<!--RUTA-->\n"
                + "         <li><a href=\"../upload.html\">Cargar kenken</a></li>		<!--RUTA-->\n"
                + "         <li><a href=\"../cgi-bin/list.cgi\">Pasatiempos disponibles</a></li>		<!--RUTA-->\n"
                + "	</ul>\n"
                + "         <div id = \"contenido\">"
                + "            <p>" + status + "</p>\n"
                + "     </div>\n"
                + "         <footer id = \"foot\">\n"
                + "             <address>\n"
                + "                 Written by <a href=\"mailto:rrodrc04@estudiantes.unileon.es\">Ruben Rodriguez</a><br>\n"
                + "		</address>\n"
                + "		© 2016\n"
                + "	</footer>\n"
                + "</body>\n"
                + "</html>";
        return html;
    }

    private static void mapearPoliminos() {
        int id = 1;
        for (int i = 0; i < Filas.size(); i++) {
            for (int j = 0; j < Filas.get(i).size(); j++) {
                if (Filas.get(i).get(j).getPolId().equalsIgnoreCase("")) {
                    assignId(i, j, id);
                    id++;
                }
            }
        }
    }

    private static void assignId(int i, int j, int id) {
        int lim = Filas.size() - 1;

        if (Filas.get(i).get(j).getPolId().equalsIgnoreCase("")) {

            int col = Integer.valueOf(Filas.get(i).get(j).getColumna()) - 1;
            int fil = Integer.valueOf(Filas.get(i).get(j).getFila()) - 1;

            Filas.get(i).get(j).setPolId(String.valueOf(id));

            if (Filas.get(i).get(j).getTipo().equalsIgnoreCase("arriba-izquierda")) {

                col = col + 1;
                if ((col <= lim && col >= 0) && (fil <= lim && fil >= 0)) {
                    assignId(fil, col, id);
                }
                fil = fil + 1;
                if ((col <= lim && col >= 0) && (fil <= lim && fil >= 0)) {
                    assignId(fil, col, id);
                }

            } else if (Filas.get(i).get(j).getTipo().equalsIgnoreCase("arriba-derecha")) {

                int temp = col - 1;
                if ((temp <= lim && temp >= 0) && (fil <= lim && fil >= 0)) {
                    assignId(fil, temp, id);
                }
                temp = fil + 1;
                if ((col <= lim && col >= 0) && (temp <= lim && temp >= 0)) {
                    assignId(temp, col, id);
                }

            } else if (Filas.get(i).get(j).getTipo().equalsIgnoreCase("abajo-izquierda")) {

                int temp = col + 1;
                if ((temp <= lim && temp >= 0) && (fil <= lim && fil >= 0)) {
                    assignId(fil, temp, id);
                }
                temp = fil - 1;
                if ((col <= lim && col >= 0) && (temp <= lim && temp >= 0)) {
                    assignId(temp, col, id);
                }

            } else if (Filas.get(i).get(j).getTipo().equalsIgnoreCase("abajo-derecha")) {

                int temp = col - 1;
                if ((temp <= lim && temp >= 0) && (fil <= lim && fil >= 0)) {
                    assignId(fil, temp, id);
                }
                temp = fil - 1;
                if ((col <= lim && col >= 0) && (fil <= lim && fil >= 0)) {
                    assignId(fil, col, id);
                }

            } else if (Filas.get(i).get(j).getTipo().equalsIgnoreCase("derecha-izquierda")) {

                int temp = fil - 1;
                if ((col <= lim && col >= 0) && (temp <= lim && temp >= 0)) {
                    assignId(temp, col, id);
                }
                temp = fil + 1;
                if ((col <= lim && col >= 0) && (temp <= lim && temp >= 0)) {
                    assignId(temp, col, id);
                }

            } else if (Filas.get(i).get(j).getTipo().equalsIgnoreCase("arriba-abajo")) {

                int temp = col + 1;
                if ((temp <= lim && temp >= 0) && (fil <= lim && fil >= 0)) {
                    assignId(fil, temp, id);
                }
                temp = col - 1;
                if ((temp <= lim && temp >= 0) && (fil <= lim && fil >= 0)) {
                    assignId(fil, temp, id);
                }

            } else if (Filas.get(i).get(j).getTipo().equalsIgnoreCase("derecha-arriba-abajo")) {

                col = col - 1;
                if ((col <= lim && col >= 0) && (fil <= lim && fil >= 0)) {
                    assignId(fil, col, id);
                }

            } else if (Filas.get(i).get(j).getTipo().equalsIgnoreCase("izquierda-arriba-abajo")) {

                col = col + 1;
                if ((col <= lim && col >= 0) && (fil <= lim && fil >= 0)) {
                    assignId(fil, col, id);
                }

            } else if (Filas.get(i).get(j).getTipo().equalsIgnoreCase("arriba-derecha-izquierda")) {

                fil = fil + 1;
                if ((col <= lim && col >= 0) && (fil <= lim && fil >= 0)) {
                    assignId(fil, col, id);
                }

            } else if (Filas.get(i).get(j).getTipo().equalsIgnoreCase("abajo-derecha-izquierda")) {

                fil = fil - 1;
                if ((col <= lim && col >= 0) && (fil <= lim && fil >= 0)) {
                    assignId(fil, col, id);
                }

            }
        }
    }
}
