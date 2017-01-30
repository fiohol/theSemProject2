/*
 * Copyright 2017 The Sem Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.thesemproject.commons.utils;

/**
 * Gestisce il messaggio di ritorno di un webservice che verrà convertito in
 * JSON
 *
 * @author The Sem Project
 */
public class Message {

    private String message;
    private String content;

    /**
     * Crea un nuovo messaggio di risposta
     */
    public Message() {
        message = new String();
        content = new String();
    }

    /**
     * Crea un nuovo messaggio di risposta
     *
     * @param message messaggio
     * @param content contenuto in formato JSON
     */
    public Message(String message, String content) {
        this.message = message;
        this.content = content;
    }

    /**
     * Ritorna il messaggio
     *
     * @return messaggio
     */
    public String getMessage() {
        return message;
    }

    /**
     * Imposta il messaggio
     *
     * @param message messaggio
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Ritorna il contenuto
     *
     * @return contenuto
     */
    public String getContent() {
        return content;
    }

    /**
     * Imposta il contenuto
     *
     * @param content contenuto
     */
    public void setContent(String content) {
        this.content = content;
    }

}
