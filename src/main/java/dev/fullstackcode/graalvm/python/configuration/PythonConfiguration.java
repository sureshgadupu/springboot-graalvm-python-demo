package dev.fullstackcode.graalvm.python.configuration;


import dev.fullstackcode.graalvm.python.service.WelcomeService;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;


@Configuration
public class PythonConfiguration {

    ResourceLoader resourceLoader;


    public PythonConfiguration(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Bean
    public WelcomeService welcomeService() throws IOException {

        String userHomeDirectory = "/root";

        String venvExePath = resourceLoader.getResource("file:" + userHomeDirectory + "/.local/vfs/venv/bin/graalpy").getURL().toString();

        String sitePath = resourceLoader.getResource("file:" + userHomeDirectory + "/.local/vfs/venv/lib/python3.10/site-packages").getURL().toString();


        Context context = Context.newBuilder("python").
                allowIO(true).
                allowAllAccess(true)
                .option("python.Executable", venvExePath)
                .option("python.PythonPath", sitePath)
                .build();

        Resource resource = resourceLoader.getResource("classpath:/vfs/python/WelcomeServiceImpl.py");

        Source source = Source
                .newBuilder("python", new InputStreamReader(resource.getInputStream()), "WelComeServiceImpl").build();

        context.eval(source);

        Value welcomeServiceImplClass = context.getPolyglotBindings().getMember("WelcomeServiceImpl");
        Value welcomeServiceImpl = welcomeServiceImplClass.newInstance();

        WelcomeService welcomeService = welcomeServiceImpl.as(WelcomeService.class);

        return welcomeService;

    }

}
