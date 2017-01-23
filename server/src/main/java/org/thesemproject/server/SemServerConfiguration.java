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

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletContext;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 * Gestisce i server attivi nel sistema
 *
 * @author The Sem Project
 */
public class SemServerConfiguration {

    private final Map<String, SemServer> configurations;
    private static ServletContext context;
    private String adminPassword;

    private final static String configurationFile = "/WEB-INF/openSemServer/config.xml";

    private static SemServerConfiguration instance;

    private SemServerConfiguration() {
        configurations = new HashMap<>();
        adminPassword = "administrator";
        //Legge dalla configurazione
    }

    /**
     * Ritorna l'istanza della configurazione
     *
     * @return istanza di configurazione
     */
    public static SemServerConfiguration getInstance() {
        if (instance == null) {
            instance = new SemServerConfiguration();
            context = null;
        }
        return instance;
    }

    /**
     * Inizializza la configurazione
     *
     * @param context contesto servlet
     */
    public void init(final ServletContext context) {
        this.context = context;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    context.log("Inizializzazione openSemServer");
                    InputStream resourceContent = context.getResourceAsStream(configurationFile);
                    if (resourceContent != null) {
                        SAXBuilder saxBuilder = new SAXBuilder();
                        Document d = saxBuilder.build(resourceContent);
                        List<Element> servers = d.getRootElement().getChildren();
                        String pd = d.getRootElement().getAttributeValue("adminPassword");
                        if (pd != null) {
                            adminPassword = pd;
                        }
                        for (Element server : servers) {
                            SemServer sem = new SemServer(server);
                            context.log("Trovata configurazione: " + sem.getName());
                            configurations.put(sem.getName(), sem);
                        }
                        resourceContent.close();
                        context.log("Inizializzazione servers");
                        for (SemServer sem : configurations.values()) {
                            context.log("Init server: " + sem.getName());
                            sem.init();
                        }
                        context.log("openSemServer inizializzato");
                        System.gc();
                    }
                } catch (Exception e) {
                    context.log("Exception " + e);
                }
            }
        });
        t.setDaemon(true);
        t.start();

    }

    /**
     * Ritorna il contesto
     *
     * @return contesto
     */
    public static ServletContext getContext() {
        return context;
    }

    /**
     * Ritorna i nomi dei server
     *
     * @return nomi dei server
     */
    public Set<String> getServersName() {
        return configurations.keySet();
    }

    /**
     * Ritorna la lista dei server
     *
     * @return lista dei server
     */
    public List<SemServer> getServers() {
        return new ArrayList(configurations.values());
    }

    /**
     * Aggiunge ed inizializza un server
     *
     * @param sem
     */
    public void setServer(SemServer sem) {
        removeServer(sem.getName());
        configurations.put(sem.getName(), sem);
        sem.init();
        System.gc();
        updateConfiguration();
    }

    /**
     * Ritorna il server dato il nome
     *
     * @param name nome del server
     * @return server
     */
    public SemServer getServer(String name) {
        return configurations.get(name);
    }

    /**
     * Spegne e rimuove un server
     *
     * @param name nome del server
     */
    public void removeServer(String name) {
        SemServer se = configurations.get(name);
        if (se != null) {
            se.shutdown();
        }
        configurations.remove(name);
        updateConfiguration();
    }

    /**
     * Aggiorna la configurazione
     */
    private void updateConfiguration() {
        try {
            Document doc = new Document();
            Element servers = new Element("servers");
            servers.setAttribute("adminPassword", adminPassword);
            doc.addContent(servers);
            for (SemServer server : configurations.values()) {
                servers.addContent(server.getXML());
            }
            XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
            try {
                FileOutputStream fos = new FileOutputStream(context.getRealPath(configurationFile));
                outputter.output(doc, fos);
                fos.close();
            } catch (Exception e) {
                context.log("Exception " + e);
            }

        } catch (Exception e) {
            context.log("Exception " + e);
        }

    }

    /**
     * Aggiorna la password amministratore
     *
     * @param pwd password
     */
    public void updatePassword(String pwd) {
        adminPassword = pwd;
        updateConfiguration();
    }

    public void shutdown() {
        context.log("Shutdown server");
        for (SemServer sem : configurations.values()) {
            sem.shutdown();
        }
        context.log("Shutdown done");
    }

    /**
     * Ritorna la password amministratore
     *
     * @return password
     */
    public String getAdminPassword() {
        return adminPassword;
    }

    /**
     * Indirizzo di redirect standard
     */
    public static final String REDIRECT_CONTROLLER = "redirect:/controller";

}
