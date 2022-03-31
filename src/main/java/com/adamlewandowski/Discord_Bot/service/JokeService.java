package com.adamlewandowski.Discord_Bot.service;

import com.adamlewandowski.Discord_Bot.model.Joke;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;

@Service
public class JokeService {

    public String getJoke() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        URL apiUrl = new URL("https://api.chucknorris.io/jokes/random");
        Joke joke = objectMapper.readValue(apiUrl, Joke.class);
        return joke.getValue();
    }
}