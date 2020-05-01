package com.projects.randomWordController;

import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class RandomWordView extends JFrame {
    private JButton setWordButton = null;
    private JTextField newWordField = null;
    private JTextField currentWordField = null;
    private JButton getWordButton = null;

    public static final String GET_WORD_LABEL = "Get new word.";
    public static final String SET_WORD_LABEL = "Set new word.";
    
    public RandomWordView()
    {
        currentWordField = new JTextField(20);
        getWordButton = new JButton(GET_WORD_LABEL);
        newWordField = new JTextField(20);
        setWordButton = new JButton(SET_WORD_LABEL);

        JPanel content = new JPanel();
        content.add(currentWordField);
        content.add(getWordButton);
        content.add(newWordField);
        content.add(setWordButton);
        this.setContentPane(content);
        this.pack();
        this.setTitle("Random Word MVC Example");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    }
    
    public void addWordButtonListener(ActionListener al)
    {
        getWordButton.addActionListener(al);
    }
    
    public void setCurrentWord(String newWord)
    {
        currentWordField.setText(newWord);
    }

    public void addSetWordButtonListener(ActionListener al)
    {
        setWordButton.addActionListener(al);
    }

    public String getNewWordFromField()
    {
        return newWordField.getText();
    }

    public void clearNewWordField() {
        newWordField.setText("");
    }
}
