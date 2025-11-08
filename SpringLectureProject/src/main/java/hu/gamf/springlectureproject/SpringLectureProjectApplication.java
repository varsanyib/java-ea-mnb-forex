package hu.gamf.springlectureproject;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
public class SpringLectureProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringLectureProjectApplication.class, args);
    }

}

