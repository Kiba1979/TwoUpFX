package com.kiba.twoupfx;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import org.testfx.framework.junit.ApplicationTest;
import org.testng.annotations.Test;


public class TwoUpMainTest extends ApplicationTest {

    private final TwoUpMain twoUpMain = new TwoUpMain();

    @Override
    public void start (Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(twoUpMain.getClass().getResource("two-up.fxml"));
        stage.setScene(loader.load());
        stage.show();
        stage.toFront();
    }

    @Test
    public void testStart() {

    }

    @Test
    public void testMain() {

    }
}