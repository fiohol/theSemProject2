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
package org.thesemproject.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import javax.imageio.ImageIO;
import org.json.JSONObject;
import org.mcavallo.opencloud.Cloud;
import org.mcavallo.opencloud.Tag;
import org.thesemproject.commons.classification.ClassificationPath;
import org.thesemproject.commons.classification.NodeData;
import org.thesemproject.commons.utils.Message;
import org.thesemproject.commons.utils.interning.InternPool;
import org.thesemproject.server.SemService;
import org.thesemproject.server.SemService_Service;

/**
 * Driver per accedere al server e utilizzare i metodi
 *
 * @author The Sem Project
 *
 */
public class SemClientService {

    private final SemService service;
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Costruisce un nuovo driver
     *
     * @param serverAddress indirizzo
     * @throws SemClientException eccezione
     */
    public SemClientService(String serverAddress) throws SemClientException {
        URL wsdlLocation;
        try {
            wsdlLocation = new URL("http://" + serverAddress + "/SemService?wsdl");
        } catch (MalformedURLException ex) {
            throw new SemClientException(ex);
        }
        SemService_Service sss = new SemService_Service(wsdlLocation);
        service = sss.getSemServicePort();
    }

    /**
     * Ritorna l'elenco dei server
     *
     * @return elenco dei server
     * @throws SemClientException eccezione
     */
    public String[] getServers() throws SemClientException {

        try {
            String response = service.getServersNames();
            Message message = getMessage(response);
            return mapper.readValue(message.getContent(), String[].class);
        } catch (SemClientException se) {
            throw se;
        } catch (Exception e) {
            throw new SemClientException(e);
        }
    }

    //-------------------- PARSER ------------------------------------------
    /**
     * Ritorna il testo a partire da un file
     *
     * @param server server a cui si chiede il servizio
     * @param fileName percorso del file
     * @return testo
     * @throws SemClientException eccezione nell'esecuzione
     */
    public String getText(String server, File fileName) throws SemClientException {
        try {
            String content = service.getTextFromFile(server, fileName.getAbsolutePath());
            return getStringContent(content);
        } catch (SemClientException se) {
            throw se;
        } catch (Exception e) {
            throw new SemClientException(e);
        }
    }

    /**
     * Ritorna il testo a partire da uno stream binario
     *
     * @param server server a cui si chiede il servizio
     * @param bytes binario
     * @return testo
     * @throws SemClientException eccezione nell'esecuzione
     *
     */
    public String getText(String server, byte[] bytes) throws SemClientException {
        try {
            String content = service.getTextFromBinary(server, bytes);
            return getStringContent(content);
        } catch (SemClientException se) {
            throw se;
        } catch (Exception e) {
            throw new SemClientException(e);
        }
    }

    /**
     * Identifica la lingua in cui il testo è scritto
     *
     * @param server server a cui si chiede il servizio
     * @param text testo
     * @return lingua
     * @throws SemClientException eccezione nell'esecuzione
     */
    public String getLanguage(String server, String text) throws SemClientException {
        try {
            String content = service.getLanguage(server, text);
            return getStringContent(content);
        } catch (SemClientException se) {
            throw se;
        } catch (Exception e) {
            throw new SemClientException(e);
        }
    }

    /**
     * Estrae le immagini dal file
     *
     * @param server server a cui si chiede il servizio
     * @param file percorso del file
     * @return mappa nome immagine binario immagine
     * @throws org.thesemproject.client.SemClientException eccezione
     */
    public Map<String, BufferedImage> getImages(String server, File file) throws SemClientException {
        try {
            List<byte[]> content = service.getImagesFromFile(server, file.getAbsolutePath());
            return getMapContent(content);
        } catch (SemClientException se) {
            throw se;
        } catch (Exception e) {
            throw new SemClientException(e);
        }
    }

    /**
     * Estrae le immagini dal binario
     *
     * @param server server a cui si chiede il servizio
     * @param bytes binario
     * @return immagini
     * @throws SemClientException eccezione nell'esecuzione
     */
    public Map<String, BufferedImage> getImages(String server, byte[] bytes) throws SemClientException {
        try {
            List<byte[]> content = service.getImagesFromBinary(server, bytes);
            return getMapContent(content);
        } catch (SemClientException se) {
            throw se;
        } catch (Exception e) {
            throw new SemClientException(e);
        }
    }

    /**
     * Estrae l'immagine più grande da un file
     *
     * @param server server a cui si chiede il servizio
     * @param file percorso file
     * @return immagine
     * @throws SemClientException eccezione nell'esecuzione
     */
    public BufferedImage getPicture(String server, File file) throws SemClientException {
        try {
            byte[] content = service.getImageFromFile(server, file.getAbsolutePath());
            return getBufferedImage(content);
        } catch (SemClientException se) {
            throw se;
        } catch (Exception e) {
            throw new SemClientException(e);
        }
    }

    /**
     * Estrae l'immagine dal binario
     *
     * @param server server a cui si chiede il servizio
     * @param bytes binario
     * @return immagine
     * @throws SemClientException eccezione nell'esecuzione
     */
    public BufferedImage getPicture(String server, byte[] bytes) throws SemClientException {
        try {
            byte[] content = service.getImageFromBinary(server, bytes);
            return getBufferedImage(content);
        } catch (SemClientException se) {
            throw se;
        } catch (Exception e) {
            throw new SemClientException(e);
        }
    }

//-------------------- CLASSIFICAZIONE----------------------------------
    /**
     * Classifica un testo
     *
     * @param server server a cui si chiede il servizio
     * @param text testo
     * @return lista delle possibili classificazioni
     * @throws org.thesemproject.client.SemClientException eccezione
     */
    public List<ClassificationPath> getClassifications(String server, String text) throws SemClientException {
        try {
            String content = service.getClassificationsFromText(server, text);
            return getList(content);
        } catch (SemClientException se) {
            throw se;
        } catch (Exception e) {
            throw new SemClientException(e);
        }
    }

    /**
     * Classifica un file
     *
     * @param server server a cui si chiede il servizio
     * @param file file
     * @return lista delle possibili classificazioni
     * @throws SemClientException eccezione nell'esecuzione
     */
    public List<ClassificationPath> getClassifications(String server, File file) throws SemClientException {
        try {
            String content = service.getClassificationsFromFile(server, file.getAbsolutePath());
            return getList(content);
        } catch (SemClientException se) {
            throw se;
        } catch (Exception e) {
            throw new SemClientException(e);
        }
    }

    /**
     * Classifica un binario
     *
     * @param server server a cui si chiede il servizio
     * @param bytes binario
     * @return lista delle possibili classificazioni
     * @throws SemClientException eccezione nell'esecuzione
     */
    public List<ClassificationPath> getClassifications(String server, byte[] bytes) throws SemClientException {
        try {
            String content = service.getClassificationsFromBinary(server, bytes);
            return getList(content);
        } catch (SemClientException se) {
            throw se;
        } catch (Exception e) {
            throw new SemClientException(e);
        }
    }

    /**
     * Ritorna il nodo root di classificazione
     *
     * @param server server a cui si chiede il servizio
     * @return nodo Root
     * @throws SemClientException eccezione nell'esecuzione
     */
    public NodeData getClassificationTree(String server) throws SemClientException {
        try {
            String content = service.getClassificationTree(server);
            return getNodeData(content);
        } catch (SemClientException se) {
            throw se;
        } catch (Exception e) {
            throw new SemClientException(e);
        }
    }

    //-------------------- SEGMENTAZIONE------------------------------------
    /**
     * Segmenta (e classifica) un testo
     *
     * @param server server a cui si chiede il servizio
     * @param text testo
     * @return segmentazione
     * @throws SemClientException eccezione nell'esecuzione
     */
    public JSONObject getSegmentation(String server, String text) throws SemClientException {
        try {
            String content = service.getSegmentationFromText(server, text);
            return getDocument(content);
        } catch (SemClientException se) {
            throw se;
        } catch (Exception e) {
            throw new SemClientException(e);
        }
    }

    /**
     * Segmenta (e classifica) un file
     *
     * @param server server a cui si chiede il servizio
     * @param file file
     * @return segmentazione
     * @throws SemClientException eccezione nell'esecuzione
     */
    public JSONObject getSegmentation(String server, File file) throws SemClientException {
        try {
            String content = service.getSegmentationFromFile(server, file.getAbsolutePath());
            return getDocument(content);
        } catch (SemClientException se) {
            throw se;
        } catch (Exception e) {
            throw new SemClientException(e);
        }
    }

    /**
     * Segmenta (e classifica) un flusso binario
     *
     * @param server server a cui si chiede il servizio
     * @param bytes flusso binario
     * @return segmentazione
     * @throws SemClientException eccezione nell'esecuzione
     */
    public JSONObject getSegmentation(String server, byte[] bytes) throws SemClientException {
        try {
            String content = service.getSegmentationFromBinary(server, bytes);
            return getDocument(content);
        } catch (SemClientException se) {
            throw se;
        } catch (Exception e) {
            throw new SemClientException(e);
        }
    }

    /**
     * Ritorna la versione html della segmentazione
     *
     * @param server server a cui si chiede il servizio
     * @param text testo da segmentare
     * @return testo html
     * @throws SemClientException eccezione nell'esecuzione
     */
    public String getFormattedHtml(String server, String text) throws SemClientException {
        try {
            String content = service.getHtmlSegmentationFromText(server, text);
            return getStringContent(content);
        } catch (SemClientException se) {
            throw se;
        } catch (Exception e) {
            throw new SemClientException(e);
        }
    }

    /**
     * Ritorna la versione html della segmentazione
     *
     * @param server server a cui si chiede il servizio
     * @param file file
     * @return testo html
     * @throws SemClientException eccezione nell'esecuzione
     */
    public String getFormattedHtml(String server, File file) throws SemClientException {
        try {
            String content = service.getHtmlSegmentationFromFile(server, file.getAbsolutePath());
            return getStringContent(content);
        } catch (SemClientException se) {
            throw se;
        } catch (Exception e) {
            throw new SemClientException(e);
        }
    }

    /**
     * Ritorna la versione html della segmentazione
     *
     * @param server server
     * @param bytes binario
     * @return testo html
     * @throws SemClientException eccezione nell'esecuzione
     */
    public String getFormattedHtml(String server, byte[] bytes) throws SemClientException {
        try {
            String content = service.getHtmlSegmentationFromBinary(server, bytes);
            return getStringContent(content);
        } catch (SemClientException se) {
            throw se;
        } catch (Exception e) {
            throw new SemClientException(e);
        }
    }

    //------------------------ TAG CLOUD -----------------------------------------------
    /**
     * Ritorna il risultato del tagcloud su un testo
     *
     * @param server server a cui si chiede il servizio
     * @param text testo
     * @param max massimo numero di tag
     * @return Tagcloud
     * @throws SemClientException eccezione nell'esecuzione
     */
    public Cloud tagCloud(String server, String text, int max) throws SemClientException {
        try {
            String content = service.tagCloud(server, text, max);
            return getCloud(content);
        } catch (SemClientException se) {
            throw se;
        } catch (Exception e) {
            throw new SemClientException(e);
        }
    }

    /**
     * Ritorna il risultato del tagcloud su una lista di testi
     *
     * @param server server a cui si chiede il servizio
     * @param texts testi
     * @param max massimo numero di tag
     * @return Tagcloud
     * @throws SemClientException eccezione nell'esecuzione
     */
    public Cloud tagCloud(String server, List<String> texts, int max) throws SemClientException {
        try {
            String content = service.tagClouds(server, texts, max);
            return getCloud(content);
        } catch (SemClientException se) {
            throw se;
        } catch (Exception e) {
            throw new SemClientException(e);
        }
    }

    //------------------------ RANK -----------------------------------------------
    /**
     *
     * Aggiunge un valutatore di rank
     *
     * @param server server a cui si chiede il servizio
     * @param name nome del valutatore
     * @param field field
     * @param fieldConditionOperator condizione
     * @param fieldConditionValue valore
     * @param startPeriod inizio
     * @param endPeriod fine
     * @param duration durata
     * @param durationCondition condizione di durata
     * @param score punteggio
     * @return messaggio
     * @throws SemClientException eccezione nell'esecuzione
     */
    public String addEvaluation(String server,
            String name,
            String field,
            String fieldConditionOperator,
            String fieldConditionValue,
            int startPeriod,
            int endPeriod,
            double duration,
            String durationCondition,
            double score) throws SemClientException {
        try {

            String message = service.addEvaluation(server, name, field, fieldConditionOperator, fieldConditionValue, startPeriod, endPeriod, duration, durationCondition, score);
            return getStringContent(message);
        } catch (SemClientException se) {
            throw se;
        } catch (Exception e) {
            throw new SemClientException(e);
        }
    }

    /**
     * Ripulisce i valutatori
     *
     * @param server server a cui si chiede il servizio
     * @param evaluatorName nome del valutatore
     * @return messaggio
     * @throws SemClientException eccezione nell'esecuzione
     */
    public String clearEvaluations(String server, String evaluatorName) throws SemClientException {
        try {
            String message = service.clearEvaluation(server, evaluatorName);
            return getStringContent(message);
        } catch (SemClientException se) {
            throw se;
        } catch (Exception e) {
            throw new SemClientException(e);
        }
    }

    /**
     * Ritorna la rappresentazione dello stato del server
     *
     * @param server server a cui si chiede il servizio
     * @return rappresentazione dello stato del server
     * @throws SemClientException eccezione nell'esecuzione
     */
    public String getServerDetails(String server) throws SemClientException {
        try {
            String content = service.getServerDetails(server);
            return getStringContent(content);
        } catch (SemClientException se) {
            throw se;
        } catch (Exception e) {
            throw new SemClientException(e);
        }
    }

    private Map<String, BufferedImage> getMapContent(List<byte[]> content) throws SemClientException {
        Map<String, BufferedImage> ret = new HashMap<>();
        for (int i = 0; i < content.size(); i++) {
            ret.put("Image_" + i, getBufferedImage(content.get(i)));
        }
        return ret;
    }

    private BufferedImage getBufferedImage(byte[] imageInByte) throws SemClientException {
        InputStream in = new ByteArrayInputStream(imageInByte);
        BufferedImage bImageFromConvert;
        try {
            bImageFromConvert = ImageIO.read(in);
        } catch (IOException ex) {
            throw new SemClientException(ex);
        }
        return bImageFromConvert;
    }

    private List<ClassificationPath> getList(String content) throws SemClientException {
        String ret = getContent(content);
        try {
            List<ClassificationPath> cps = new ArrayList<>();
            List<String> values = mapper.readValue(ret, List.class);
            for (String value : values) {
                JsonNode node = mapper.readTree(value);
                ClassificationPath cp = new ClassificationPath(node);
                cps.add(cp);
            }
            return cps;
        } catch (IOException e) {
            throw new SemClientException(e);
        } catch (Exception e) {
            throw new SemClientException(e);
        }
    }

    private NodeData getNodeData(String content) throws SemClientException {
        String messageContent = getStringContent(content);
        try {

            List<String> csv = new ArrayList<>();
            StringTokenizer st = new StringTokenizer(messageContent, "\r\n");
            while (st.hasMoreTokens()) {
                csv.add(st.nextToken());
            }
            return NodeData.getNodeData(csv, new InternPool());

        } catch (Exception e) {
            throw new SemClientException(e);
        }
    }

    private JSONObject getDocument(String content) throws SemClientException {
        String doc = getContent(content);
        try {
            JSONObject ret = new JSONObject(doc);
            return ret;
        } catch (Exception e) {
            throw new SemClientException(e);
        }

    }

    private Cloud getCloud(String content) throws SemClientException {
        try {
            Message message = getMessage(content);
            List<String> ret = mapper.readValue(message.getContent(), List.class);
            Cloud c = new Cloud();
            for (String tag : ret) {
                JSONObject t = new JSONObject(tag);
                c.addTag(new Tag(t.getString("name"), t.getString("link"), t.getDouble("score")));
            }
            return c;
        } catch (SemClientException se) {
            throw se;
        } catch (IOException e) {
            throw new SemClientException(e);
        } catch (Exception e) {
            throw new SemClientException(e);
        }
    }

    private String getStringContent(String response) throws SemClientException {
        try {
            return mapper.readValue(getMessage(response).getContent(), String.class);
        } catch (SemClientException se) {
            throw se;
        } catch (Exception e) {
            throw new SemClientException(e);
        }
    }

    private String getContent(String response) throws SemClientException {
        try {
            return getMessage(response).getContent();
        } catch (SemClientException se) {
            throw se;
        } catch (Exception e) {
            throw new SemClientException(e);
        }
    }

    private Message getMessage(String content) throws SemClientException {

        try {
            Message message = mapper.readValue(content, Message.class);
            if (message.getMessage().equalsIgnoreCase("Error")) {
                throw new SemClientException(message.getMessage());
            }
            return message;
        } catch (IOException e) {
            throw new SemClientException(e);
        } catch (Exception e) {
            throw new SemClientException(e);
        }
    }

}
