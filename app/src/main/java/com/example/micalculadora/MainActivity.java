package com.example.micalculadora;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private TextView txtResult;
    private String currentInput = "";
    private String operator = "";
    private double firstValue = 0;
    private boolean isOperatorPressed = false;
    private boolean decimalUsed = false; // Cambiado a false por defecto
    private String history = ""; // Variable para almacenar el historial

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtResult = findViewById(R.id.txtResult);
        Button btnHistory = findViewById(R.id.btnHistory); // Botón para mostrar historial

        setNumberButtonListeners();
        setOperationButtonListeners();

        // Listener para el botón de historial
        btnHistory.setOnClickListener(view -> showHistory());
    }

    private void setNumberButtonListeners() {
        int[] numberButtonIds = {
                R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3,
                R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7,
                R.id.btn8, R.id.btn9, R.id.btnDecimal
        };

        View.OnClickListener listener = view -> {
            Button button = (Button) view;

            if (isOperatorPressed) {
                currentInput = "";
                isOperatorPressed = false;
                decimalUsed = false;
            }

            String input = button.getText().toString();

            if (input.equals(".") && decimalUsed) {
                return;
            }

            if (input.equals(".")) {
                decimalUsed = true;
            }

            currentInput += input;
            txtResult.setText(currentInput);
        };

        for (int id : numberButtonIds) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    private void setOperationButtonListeners() {
        int[] operationButtonIds = {
                R.id.btnSum, R.id.btnSub, R.id.btnMul, R.id.btnDiv, R.id.btnEqual, R.id.btnClear, R.id.btnDelete
        };

        View.OnClickListener listener = view -> {
            Button button = (Button) view;
            String op = button.getText().toString();

            if (op.equals("C")) {
                clear();
                return;
            }

            if (op.equals("=")) {
                calculate();
                return;
            }

            if (op.equals("DEL")) {
                deleteLastCharacter();
                return;
            }

            // Validar si el input actual está vacío antes de permitir un operador
            if (currentInput.isEmpty()) {
                return;
            }

            if (!currentInput.isEmpty()) {
                firstValue = Double.parseDouble(currentInput);
            }

            operator = op;
            isOperatorPressed = true;
            decimalUsed = false; // Resetear uso del decimal después de presionar un operador
        };

        for (int id : operationButtonIds) {
            findViewById(id).setOnClickListener(listener);
        }
    }

    @SuppressLint("SetTextI18n")
    private void calculate() {
        if (operator.isEmpty() || currentInput.isEmpty()) {
            return;
        }

        double secondValue = Double.parseDouble(currentInput);
        double result = 0;

        switch (operator) {
            case "+":
                result = firstValue + secondValue;
                break;
            case "-":
                result = firstValue - secondValue;
                break;
            case "*":
                result = firstValue * secondValue;
                break;
            case "/":
                if (secondValue != 0) {
                    result = firstValue / secondValue;
                } else {
                    txtResult.setText("Error");
                    return;
                }
                break;
        }

        txtResult.setText(String.valueOf(result));

        // Actualizar historial
        history += firstValue + " " + operator + " " + secondValue + " = " + result + "\n";

        firstValue = result;
        currentInput = String.valueOf(result);
        operator = ""; // Limpiar el operador después de calcular
        decimalUsed = false; // Resetear uso del decimal después de calcular
    }

    private void clear() {
        currentInput = "";
        firstValue = 0;
        operator = "";
        isOperatorPressed = false;
        decimalUsed = false; // Resetear uso del decimal
        txtResult.setText("0");
        history = ""; // Limpiar historial
    }

    private void deleteLastCharacter() {
        if (!currentInput.isEmpty()) {
            if (currentInput.charAt(currentInput.length() - 1) == '.') {
                decimalUsed = false;
            }
            currentInput = currentInput.substring(0, currentInput.length() - 1);
            if (currentInput.isEmpty()) {
                txtResult.setText("0");
            } else {
                txtResult.setText(currentInput);
            }
        }
    }

    // Método para mostrar el historial
    private void showHistory() {
        // Opción 1: Mostrar historial en un Toast
        if (!history.isEmpty()) {
            Toast.makeText(this, history, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "No hay historial", Toast.LENGTH_SHORT).show();
        }

        // Opción 2: Mostrar historial en un AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Historial de Operaciones");
        builder.setMessage(history.isEmpty() ? "No hay historial" : history);
        builder.setPositiveButton("OK", null);
        builder.show();
    }
}
