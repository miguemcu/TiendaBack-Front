/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BusinessLogic;

/**
 *
 * @author DELL
 */
import java.util.ArrayList;
    import javax.swing.text.*;

public class helperUI extends DocumentFilter {
    private final int maxChars;

    public helperUI(int maxChars) {
        this.maxChars = maxChars;
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        if ((fb.getDocument().getLength() + string.length()) <= maxChars) {
            super.insertString(fb, offset, string, attr);
        } else {
            java.awt.Toolkit.getDefaultToolkit().beep();
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if ((fb.getDocument().getLength() - length + text.length()) <= maxChars) {
            super.replace(fb, offset, length, text, attrs);
        } else {
            java.awt.Toolkit.getDefaultToolkit().beep();
        }
    }
    static public void limpiarCampos(ArrayList<javax.swing.JTextField> campos){
        for (var txt:campos){
            txt.setText("");
        }
    }
    static public void limitarCampos(ArrayList<javax.swing.JTextField> campos,helperUI filter){
        for (var txt:campos){
            ((AbstractDocument) txt.getDocument()).setDocumentFilter(filter);
                    }
    }
}
