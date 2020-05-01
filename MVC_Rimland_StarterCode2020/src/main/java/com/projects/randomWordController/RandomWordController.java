package com.projects.randomWordController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class RandomWordController {
    private RandomWordModel model;
    private RandomWordView view;
    
    public RandomWordController(final RandomWordModel model, final RandomWordView view)
    {
        this.model = model;
        this.view = view;
        
        class ButtonListener implements ActionListener {
            public void actionPerformed(ActionEvent e)
            {
                if(e.getActionCommand().equals(RandomWordView.GET_WORD_LABEL))
                    view.setCurrentWord(model.getWord());
                else if(e.getActionCommand().equals(RandomWordView.SET_WORD_LABEL))
                    model.putWord(view.getNewWordFromField());
                    view.clearNewWordField();
            }
        }

        
        view.addWordButtonListener(new ButtonListener());
        view.addSetWordButtonListener(new ButtonListener());

    }
}
