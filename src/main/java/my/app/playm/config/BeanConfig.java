package my.app.playm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.ServerSocket;

@Configuration
public class BeanConfig {
    @Value("${socketPort}")
    private int socketPort;

    @Bean
    public ServerSocket serverSocket() {
        try {
            return new ServerSocket(socketPort);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalStateException("Unable to create server socket!");
        }
    }
}
