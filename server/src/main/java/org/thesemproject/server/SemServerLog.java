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
package org.thesemproject.server;

import org.thesemproject.commons.utils.Log;

/**
 * Gestione del log del server
 *
 * @author The Sem Project
 */
public class SemServerLog {

    private static boolean init = false;

    /**
     * Stampa un messaggio nel log del J2EE container e sul log Log4J
     *
     * @param message messaggio
     */
    public static void info(String message) {
        if (SemServerConfiguration.getContext() != null) {
            if (!init) {
                Log.setContext(SemServerConfiguration.getContext());
                init = true;
            }
            //SemServerConfiguration.getContext().log(message);
        } 
        Log.info(message);

    }

    /**
     * Stampa una eccezione nel log del J2EE container e lo stack trace sul
     * Log4J
     *
     * @param message eccezione
     */
    public static void info(Exception message) {
        if (SemServerConfiguration.getContext() != null) {
            SemServerConfiguration.getContext().log("Exception: " + message.getLocalizedMessage());

        } else {
            System.out.println(message.getLocalizedMessage());
        }
        org.thesemproject.commons.utils.Log.printStackTrace(message);
    }
}
