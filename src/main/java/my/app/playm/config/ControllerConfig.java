package my.app.playm.config;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import lombok.extern.log4j.Log4j;
import my.app.playm.controller.Controller;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;

@Log4j
@Configuration
public class ControllerConfig {

    @Bean(name="PlayMController")
    public Controller getController() throws IOException {
        Controller controller = (Controller) getMainView().getController();
        return controller;
    }
    @Bean(name = "mainView")
    public View getMainView() throws IOException {
        return loadView("PlayM.fxml");
    }
    protected View loadView(String url) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        try(InputStream fxmlStream = getClass().getClassLoader().getResourceAsStream(url)){
            log.debug("Load view");
            loader.load(fxmlStream);
        }
        return new View(loader.getRoot(), loader.getController());
    }

    public class View {
        private Parent root;
        private Object controller;

        public View(Parent root, Object controller) {
            this.root = root;
            this.controller = controller;
        }

        public Parent getRoot() {
            return root;
        }

        public void setRoot(Parent root) {
            this.root = root;
        }

        public Object getController() {
            return controller;
        }

        public void setController(Object controller) {
            this.controller = controller;
        }
    }
}
