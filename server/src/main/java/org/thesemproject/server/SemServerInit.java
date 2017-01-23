/*
 * Copyright 2016 The Sem Project.
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

import javax.servlet.ServletConfig;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 *
 * @author The Sem Project
 */
public class SemServerInit extends HttpServlet implements ServletContextListener {

    /**
     * Istanza del singeton
     */
    private static SemServerInit instance;

    /**
     * Ritorna l'istanza del singleton
     *
     * @return istanza del singleton
     */
    public static SemServerInit getInstance() {
        return instance;
    }

    /**
     * Inizializzazione della servlet. All'inizializzazione della servlet tutti
     * i parametri vengono impostati
     *
     * @param servletConfig configurazione della servlet
     * @throws ServletException eccezione in caso di errore
     */
    public void init(ServletConfig servletConfig) throws ServletException {
        instance = this;
        super.init(servletConfig);
        systemInit();
    }

    /**
     * Inizializzazione del sistema
     */
    public void systemInit() {
        SemServerConfiguration.getInstance().init(getServletContext());
    }

    /**
     * Evento richiamato all'inizializzazione del contesto Alternativo
     * all'inizializzazione come servlet
     *
     * @param servletContextEvent evento di inizializzazione del contesto
     */
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        instance = this;
        systemInit();
    }

    /**
     * Metodo non implementato
     *
     * @param servletContextEvent evento di distruzione contesto
     */
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        SemServerConfiguration.getInstance().shutdown();
    }
}
