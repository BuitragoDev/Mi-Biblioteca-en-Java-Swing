package Logica;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class DateNumericDocumentFilter extends DocumentFilter {

    private final int maxLength;

    public DateNumericDocumentFilter(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        if (string != null && isValidInput(string, fb.getDocument().getLength())) {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if (text != null && isValidInput(text, fb.getDocument().getLength() - length + text.length())) {
            super.replace(fb, offset, length, text, attrs);
        }
    }

    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        super.remove(fb, offset, length);
    }

    private boolean isValidInput(String input, int currentLength) {
        // Verificar que la longitud total no exceda el límite
        if (currentLength + input.length() > maxLength) {
            return false;
        }
        
        // Verificar si solo contiene dígitos o '/'
        return input.matches("[\\d/]*");
    }
}
