function master(casilla) {

    var input = casilla.value;
    var num = parseFloat(input);

    if (isNaN(num)) {
        casilla.value = "";
    }

    var id = document.getElementsByTagName("table")[0].getAttribute("id");
    var temp = id.split(".");
    var filasKenken = temp[temp.length - 1];
    var table = document.getElementById(id);
    var celdas = [];
    var id = 0;

    for (var i = 0, row; row = table.rows[i]; i++) {
        for (var j = 0, col; col = row.cells[j]; j++) {

            var op = "";
            var raw = col.getAttribute("id").split(":");
            var fila = raw[0];
            var columna = raw[1];
            var poliminoID = raw[2];

            if (col.hasChildNodes()) {
                var hijos = col.children;
                var nuevaCelda;

                for (var x = 0; x < hijos.length; x++) {
                    if (hijos[x].className !== "operacion") {
                        var valor = hijos[x].value;

                    }
                    if (hijos[x].className == "operacion") {
                        var op = hijos[x].innerText;
                    }

                    nuevaCelda = new Celda(id, fila, columna, poliminoID, op, valor);
                    nuevaCelda.dato = Validate(nuevaCelda.valor, 1, filasKenken);


                }
                id++;
                celdas.push(nuevaCelda);

            }
        }

    }

    for (var i = 0; i < celdas.length; i++) {
        celdas[i].repetidoF = CheckRows(celdas[i], celdas);
        celdas[i].repetidoC = CheckCols(celdas[i], celdas);
        celdas[i].polimino = CheckPoliminos(celdas[i], celdas);
    }
    var eventos = "";
    for (var x = 0; x < celdas.length; x++) {
        var str = print(table, celdas[x]);
        if (str != "") {
            eventos = eventos.concat(str);
            eventos = eventos.concat("\n");
        }
    }
    document.getElementById('log').value = eventos;
}

function CheckRows(currentCelda, celdas) {

    if (currentCelda.valor == "") {
        return false;
    }
    for (var x = celdas.length - 1; x >= 0; x--) {
        if (currentCelda.fila == celdas[x].fila) {
            if (currentCelda.id != celdas[x].id) {
                if (currentCelda.valor == celdas[x].valor) {
                    return true;
                }
            }
        }
    }

}

function CheckCols(currentCelda, celdas) {

    if (currentCelda.valor == "") {
        return false;
    }
    for (var x = celdas.length - 1; x >= 0; x--) {
        if (currentCelda.columna == celdas[x].columna) {
            if (currentCelda.id != celdas[x].id) {
                if (currentCelda.valor == celdas[x].valor) {
                    return true;
                }
            }
        }
    }
}

function print(table, celda) {

    var fila = celda.fila;
    var columna = celda.columna;
    var polimino = celda.polId;
    var a = celda.dato;
    var b = celda.repetidoF;
    var f = celda.repetidoC;
    var c = celda.polimino;
    var currentEventos = "";


    if (!a) {
        currentEventos = currentEventos.concat("Dato erróneo en la fila ", fila, " y la columna ", columna, ".\n");
    }

    if (!c) {
        currentEventos = currentEventos.concat("Elemento en fila ", fila, " y columna ", columna, " formando polinomio incorrecto.", "\n");
    }

    if (b) {
        currentEventos = currentEventos.concat("Elemento repetido en fila ", fila, " y columna ", columna, ".\n");
    }
    if (f) {
        currentEventos = currentEventos.concat("Elementos repetidos en fila ", fila, " y columna ", columna, ".\n");
    }

    for (var i = 0, row; row = table.rows[i]; i++) {
        for (var j = 0, col; col = row.cells[j]; j++) {

            if (fila - 1 == i && columna - 1 == j) {

                var classes = col.className.split(" ");

                if (!a || b || f || !c) {

                    col.className += ' invalid';

                } else {
                    if (classes.length > 1) {
                        col.className = classes[0];
                    }
                }
            }
        }
    }
    if (currentEventos != "") {
        return currentEventos;
    } else {
        return "";
    }


}

function Celda(id, fila, columna, poliminoID, op, valor) {
    this.id = id;
    this.fila = fila;
    this.columna = columna;
    this.poliminoID = poliminoID;
    this.operacion = op;
    this.valor = valor;
}

function Validate(str, min, max) {
    var num = parseFloat(str);
    if (str == "") {
        return true;
    } else if (num >= min && num <= max) {
        return true;
    } else {
        return false;
    }
}

function CheckPoliminos(celda, celdas) {

    var fila = celda.fila;
    var columna = celda.columna;
    var values = [];
    var polId = celda.poliminoID;
    var op;

    for (var x = 0; x < celdas.length; x++) {

        var currentPolId = celdas[x].poliminoID;
        var currentValor = celdas[x].valor;

        if (polId == currentPolId) {
            if (currentValor == "") {
                return true;
            } else {

                if (celdas[x].operacion != "") {
                    op = celdas[x].operacion;
                }
                values.push(currentValor);
            }

        }

    }

    if (!comprobarOperacion(op, values)) {
        return false;
    } else {
        return true;
    }

}

function comprobarOperacion(op, values) {

    var temp = op.split("");
    var symbol = temp[temp.length - 1];
    var resultado = "";
    var empty = "";


    for (var i = 0; i < temp.length - 1; i++) {
        resultado = resultado.concat(temp[i]);
    }

    values.sort();
    values.reverse();

    if (symbol == "-") {

        var operando = parseFloat(values[0]);

        for (var x = 1; x < values.length; x++) {
            operando -= parseFloat(values[x]);
        }
    } else if (symbol == "+") {

        var operando = parseFloat(values[0]);

        for (var x = 1; x < values.length; x++) {
            operando += parseFloat(values[x]);
        }

    } else if (symbol == "x") {
        var operando = parseFloat(values[0]);

        for (var x = 1; x < values.length; x++) {
            operando *= parseFloat(values[x]);
        }
    } else if (symbol == "÷") {
        var operando = parseFloat(values[0]) / parseFloat(values[1]);
    }

    if (operando != resultado) {
        return false;
    } else {
        return true;
    }
}

function limpiar() {

    var check = confirm("¿Seguro que desea limpiar el tablero?");
    if (check == true) {
        var id = document.getElementsByTagName("table")[0].getAttribute("id");
        var table = document.getElementById(id);

        for (var i = 0, row; row = table.rows[i]; i++) {

            for (var j = 0, col; col = row.cells[j]; j++) {

                var classes = col.className.split(" ");
                if (classes.length > 1) {
                    col.className = classes[0];
                }

                if (col.hasChildNodes()) {
                    var hijos = col.children;
                    for (var x = 0; x < hijos.length; x++) {
                        if (hijos[x].className !== "operacion") {
                            hijos[x].value = "";
                        }
                    }
                }

            }
        }

        document.getElementById('log').value = "";

    }

}

function comprobarKenken() {

    var id = document.getElementsByTagName("table")[0].getAttribute("id");
    var table = document.getElementById(id);

    for (var i = 0, row; row = table.rows[i]; i++) {
        for (var j = 0, col; col = row.cells[j]; j++) {

            var classes = col.className.split(" ");

            if (col.hasChildNodes()) {
                var hijos = col.children;
                for (var x = 0; x < hijos.length; x++) {
                    if (hijos[x].className !== "operacion") {
                        if (hijos[x].value == "") {
                            alert("Kenken incompleto :(");
                            return;
                        } else if (classes.length > 1) {
                            alert("Kenken incompleto :(");
                            return;
                        }
                    }
                }
            }

        }
    }

    for (var i = 0, row; row = table.rows[i]; i++) {
        for (var j = 0, col; col = row.cells[j]; j++) {

            var classes = col.className.split(" ");
            col.className += ' valid';

        }
    }
    alert("Kenken completado :)");
}

