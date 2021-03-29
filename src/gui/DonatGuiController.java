package gui;

import domain.Donat;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class DonatGuiController {
    @FXML
    private Label donatLabel=new Label();
    @FXML
    private Button donatButton= new Button();

    private StringBuilder content = new StringBuilder();
    private float j = 0;
    private float i = 0;
    private Donat donat=new Donat();

    public void buttonOnClick(MouseEvent mouseEvent) {
        donatButton.setDisable(true);
        AnimationTimer timer=new AnimationTimer() {
            @Override
            public void handle(long now) {
                onUpdate();
            }
        };
        timer.start();
    }
    private void onUpdate(){
        donat.render_frame(i,j);
        for (int k = 0; k < donat.screen_width; k++) {
            for (int l = 0; l < donat.screen_height; l++) {
                //System.out.print(donat.output[l][k]);
                content.append(donat.output[l][k]);
                donat.zbuffer[l][k]=0.0f;
                donat.output[l][k]=' ';
            }
            //System.out.print('\n');
            content.append('\n');
        }
        //System.out.println(content.toString());
        donatLabel.setText(content.toString());
        content.delete(0,(content.length()-1));
        i = i + 0.05f;
        j = j + 0.05f;
    }
}
