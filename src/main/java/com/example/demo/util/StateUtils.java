package com.example.demo.util;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.logging.Logger;

public abstract class StateUtils {
    public final Logger logger = Logger.getLogger(this.getClass().getName());
    public final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
}
