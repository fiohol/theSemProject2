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

import org.thesemproject.commons.utils.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import org.jdom2.output.XMLOutputter;
import org.json.JSONObject;
import org.mcavallo.opencloud.Cloud;
import org.mcavallo.opencloud.Tag;

import org.thesemproject.commons.classification.ClassificationPath;
import org.thesemproject.engine.classification.TrainableNodeData;
import org.thesemproject.engine.segmentation.functions.rank.RankEvaluator;

/**
 * Interfaccia Webservice di openSemServer
 *
 * @author The Sem Project
 */
@WebService(serviceName = "SemService")
public class SemService {

    private final ObjectMapper mapper = new ObjectMapper();

    //Metodi di controllo 
    /**
     * Ritorna la lista dei server
     *
     * @return lista dei server
     */
    @WebMethod(operationName = "getServersNames")
    public String getServersNames() {
        List<SemServer> list = SemServerConfiguration.getInstance().getServers();
        String[] ret = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            ret[i] = list.get(i).getName();
        }
        return getServerMessage("Server Names", ret);
    }

    /**
     * Ritorna i dettagli di configurazione del server
     *
     * @param serverName server
     * @return dettagli del server
     */
    @WebMethod(operationName = "getServerDetails")
    public String getServerDetails(@WebParam(name = "server") String serverName) {
        return execute(serverName, "Server Details", new SemServerTask() {
            @Override
            public Object excecute(SemServer sem) throws Exception {
                XMLOutputter outp = new XMLOutputter();
                return outp.outputString(sem.getXML());
            }
        });
    }

    //Metodi di segmentazione
    /**
     * Ritorna la segmentazione HTML da un testo
     *
     * @param serverName server
     * @param text testo
     * @return segmentazione HTML
     */
    @WebMethod(operationName = "getHtmlSegmentationFromText")
    public String getHtmlSegmentationFromText(@WebParam(name = "server") String serverName, @WebParam(name = "text") final String text) {
        return execute(serverName, "HTML Segmentation from text", new SemServerTask() {
            @Override
            public Object excecute(SemServer sem) throws Exception {
                return sem.getFormattedHtml(text);
            }
        });
    }

    /**
     * Ritorna la segmentazione HTML da un streaming binario
     *
     * @param serverName server
     * @param binary binario
     * @return segmentazione HTML
     */
    @WebMethod(operationName = "getHtmlSegmentationFromBinary")
    public String getHtmlSegmentationFromBinary(@WebParam(name = "server") String serverName, @WebParam(name = "binary") final byte[] binary) {
        return execute(serverName, "HTML Segmentation from binary", new SemServerTask() {
            @Override
            public Object excecute(SemServer sem) throws Exception {
                return sem.getFormattedHtmlFromBinary(binary);
            }
        });
    }

    /**
     * Ritorna la segmentazione HTML da un file
     *
     * @param serverName server
     * @param fileName file
     * @return Segmentazione HTML
     */
    @WebMethod(operationName = "getHtmlSegmentationFromFile")
    public String getHtmlSegmentationFromFile(@WebParam(name = "server") String serverName, @WebParam(name = "fileName") final String fileName) {
        return execute(serverName, "HTML Segmentation from file", new SemServerTask() {
            @Override
            public Object excecute(SemServer sem) throws Exception {
                File f = new File(fileName);
                if (!f.exists()) {
                    throw new Exception("File not found");
                }
                return sem.getFormattedHtmlFromFile(f);
            }
        });

    }

    /**
     * Ritorna la segmentazione di un testo
     *
     * @param serverName server
     * @param text testo
     * @return documento
     */
    @WebMethod(operationName = "getSegmentationFromText")
    public String getSegmentationFromText(@WebParam(name = "server") String serverName, @WebParam(name = "text") final String text) {
        return execute(serverName, "Segmentation from text", new SemServerTask() {
            @Override
            public Object excecute(SemServer sem) throws Exception {
                return sem.getDocument(sem.getTextSegmentation(text));
            }
        });
    }

    /**
     * Ritorna la segmentazione dallo streaming binario
     *
     * @param serverName server
     * @param binary streaming binario
     * @return documento
     */
    @WebMethod(operationName = "getSegmentationFromBinary")
    public String getSegmentationFromBinary(@WebParam(name = "server") String serverName, @WebParam(name = "binary") final byte[] binary) {
        return execute(serverName, "Segmentation from binary", new SemServerTask() {
            @Override
            public Object excecute(SemServer sem) throws Exception {
                return sem.getDocument(sem.getBinarySegmentation(binary));
            }
        });
    }

    /**
     * Ritorna la segmentazione di un file
     *
     * @param serverName server
     * @param fileName file
     * @return documento
     */
    @WebMethod(operationName = "getSegmentationFromFile")
    public String getSegmentationFromFile(@WebParam(name = "server") String serverName, @WebParam(name = "fileName") final String fileName) {
        return execute(serverName, "Segmentation from file", new SemServerTask() {
            @Override
            public Object excecute(SemServer sem) throws Exception {
                File f = new File(fileName);
                if (!f.exists()) {
                    throw new Exception("File not found");
                }
                return sem.getDocument(sem.getFileSegmentation(f));
            }
        });
    }

    //Metodi di estrazione testi 
    /**
     * Ritorna un testo da uno streaming binario
     *
     * @param serverName server
     * @param binary streaming binario
     * @return testo piano
     */
    @WebMethod(operationName = "getTextFromBinary")
    public String getTextFromBinary(@WebParam(name = "server") String serverName, @WebParam(name = "binary") final byte[] binary) {
        return execute(serverName, "Text from binary", new SemServerTask() {
            @Override
            public Object excecute(SemServer sem) throws Exception {
                return sem.getTextFromBynary(binary, false);
            }
        });
    }

    /**
     * Ritorna il testo piano da un file
     *
     * @param serverName server
     * @param fileName nome del file
     * @return testo piano
     */
    @WebMethod(operationName = "getTextFromFile")
    public String getTextFromFile(@WebParam(name = "server") String serverName, @WebParam(name = "fileName") final String fileName) {
        return execute(serverName, "Text from file", new SemServerTask() {
            @Override
            public Object excecute(SemServer sem) throws Exception {
                File f = new File(fileName);
                if (!f.exists()) {
                    throw new Exception("File not found");
                }
                return sem.getTextFromFile(f, false);
            }
        });
    }

    /**
     * RItorna il testo formattato (HTML) dato uno stream binario
     *
     * @param serverName nome del server
     * @param binary stream binario
     * @return testo formattato
     */
    @WebMethod(operationName = "getFormattedTextFromBinary")
    public String getFormattedTextFromBinary(@WebParam(name = "server") String serverName, @WebParam(name = "binary") final byte[] binary) {
        return execute(serverName, "Formatted text from binary", new SemServerTask() {
            @Override
            public Object excecute(SemServer sem) throws Exception {
                return sem.getTextFromBynary(binary, true);
            }
        });
    }

    /**
     * RItorna il testo formattato (HTML) dato un file
     *
     * @param serverName nome del server
     * @param fileName nome del file
     * @return testo formattato
     */
    @WebMethod(operationName = "getFormattedTextFromFile")
    public String getFormattedTextFromFile(@WebParam(name = "server") String serverName, @WebParam(name = "fileName") final String fileName) {
        return execute(serverName, "Formatted text from file", new SemServerTask() {
            @Override
            public Object excecute(SemServer sem) throws Exception {
                File f = new File(fileName);
                if (!f.exists()) {
                    throw new Exception("File not found");
                }
                return sem.getTextFromFile(f, true);
            }
        });
    }

    /**
     * Ritorna la lingua dato un testo
     *
     * @param serverName nome del server
     * @param text testo
     * @return lingua (ISO)
     */
    @WebMethod(operationName = "getLanguage")
    public String getLanguage(@WebParam(name = "server") String serverName, @WebParam(name = "text") final String text) {
        return execute(serverName, "Language", new SemServerTask() {
            @Override
            public Object excecute(SemServer sem) throws Exception {
                return sem.getLanguageFromText(text);
            }
        });
    }

    //Metodi di classificazione
    /**
     * Classifica un testo
     *
     * @param serverName nome del server
     * @param text testo
     * @return classificazione
     */
    @WebMethod(operationName = "getClassificationsFromText")
    public String getClassificationsFromText(@WebParam(name = "server") String serverName, @WebParam(name = "text") final String text) {
        return execute(serverName, "Classification from text", new SemServerTask() {
            @Override
            public Object excecute(SemServer sem) throws Exception {
                return sem.getClassifications(text);
            }
        });
    }

    /**
     * Classifica uno streaming di byte
     *
     * @param serverName nome del server
     * @param binary streaming di byte
     * @return classificazione
     */
    @WebMethod(operationName = "getClassificationsFromBinary")
    public String getClassificationsFromBinary(@WebParam(name = "server") String serverName, @WebParam(name = "binary") final byte[] binary) {
        return execute(serverName, "Classification from binary", new SemServerTask() {
            @Override
            public Object excecute(SemServer sem) throws Exception {
                return sem.getClassifications(binary);
            }
        });
    }

    /**
     * Classifica un file
     *
     * @param serverName server
     * @param fileName nome del file
     * @return classificazione
     */
    @WebMethod(operationName = "getClassificationsFromFile")
    public String getClassificationsFromFile(@WebParam(name = "server") String serverName, @WebParam(name = "fileName") final String fileName) {
        return execute(serverName, "Classification from file", new SemServerTask() {
            @Override
            public Object excecute(SemServer sem) throws Exception {
                File f = new File(fileName);
                if (!f.exists()) {
                    throw new Exception("File not found");
                }
                return sem.getClassifications(f);
            }
        });
    }

    //Metodi per l'estrazione di immagini 
    /**
     * Estrae l'imagine da uno streaming di bytes
     *
     * @param serverName nome del server
     * @param binary streaming di bytes
     * @return immagine
     */
    @WebMethod(operationName = "getImageFromBinary")
    public byte[] getImageFromBinary(@WebParam(name = "server") String serverName, @WebParam(name = "binary") byte[] binary) {
        SemServer sem = getSemServer(serverName);
        if (sem != null) {
            try {
                return getBytesFromImage(sem.getPictureFromBinary(binary));
            } catch (IOException e) {
                return new byte[0];
            }
        }
        return new byte[0];
    }

    /**
     * Estrae l'immagine dal file
     *
     * @param serverName nome del server
     * @param filename file
     * @return immagine
     */
    @WebMethod(operationName = "getImageFromFile")
    public byte[] getImageFromFile(@WebParam(name = "server") String serverName, @WebParam(name = "fileName") String filename) {
        SemServer sem = getSemServer(serverName);
        if (sem != null) {
            try {
                java.io.File file = new File(filename);
                if (!file.exists()) {
                    return new byte[0];
                }
                BufferedImage bi = sem.getPictureFromFile(file);
                return getBytesFromImage(bi);
            } catch (Exception e) {
                return new byte[0];
            }
        }
        return new byte[0];
    }

    /**
     * Estrae le immaigni da uno streaming di bytes
     *
     * @param serverName nome del server
     * @param binary streaming di bytes
     * @return lista di immagini
     */
    @WebMethod(operationName = "getImagesFromBinary")
    public List<byte[]> getImagesFromBinary(@WebParam(name = "server") String serverName, @WebParam(name = "binary") byte[] binary) {
        SemServer sem = getSemServer(serverName);
        if (sem != null) {
            try {
                Map<String, BufferedImage> map = sem.getImagesFromBinary(binary);
                List<byte[]> ret = new ArrayList<>();
                for (BufferedImage b : map.values()) {
                    ret.add(getBytesFromImage(b));
                }
                return ret;
            } catch (IOException e) {
                return new ArrayList<>();
            }
        }
        return new ArrayList<>();
    }

    /**
     * Estrae le immagini da un file
     *
     * @param serverName nome del server
     * @param filename nome del file
     * @return lista di immagini
     */
    @WebMethod(operationName = "getImagesFromFile")
    public List<byte[]> getImagesFromFile(@WebParam(name = "server") String serverName, @WebParam(name = "fileName") String filename) {
        SemServer sem = getSemServer(serverName);
        if (sem != null) {
            try {
                java.io.File file = new File(filename);
                if (!file.exists()) {
                    return new ArrayList<>();
                }
                Map<String, BufferedImage> map = sem.getImagesFromFile(file);
                List<byte[]> ret = new ArrayList<>();
                for (BufferedImage b : map.values()) {
                    ret.add(getBytesFromImage(b));
                }
                return ret;
            } catch (Exception e) {
                return new ArrayList<>();
            }
        }
        return new ArrayList<>();
    }

    // Altri metodi
    /**
     * Ritorna l'albero di classificazione
     *
     * @param serverName sever
     * @return albero
     */
    @WebMethod(operationName = "getClassificationTree")
    public String getClassificationTree(@WebParam(name = "server") String serverName) {
        return execute(serverName, "Classification Tree", new SemServerTask() {
            @Override
            public Object excecute(SemServer sem) throws Exception {
                return TrainableNodeData.getCSVDocument(sem.getClassificationTree());
            }
        });
    }

    /**
     * Aggiunge un valutatore
     *
     * @param serverName nome del server
     * @param name nome dell'evaluator
     * @param field field
     * @param fieldConditionOperator operatore
     * @param fieldConditionValue valore
     * @param startPeriod inizio periodo
     * @param endPeriod fine periodo
     * @param duration durata
     * @param durationCondition condizione durata
     * @param score punteggio
     *
     * @return messaggio
     */
    @WebMethod(operationName = "addEvaluation")
    public String addEvaluation(@WebParam(name = "server") final String serverName,
            @WebParam(name = "name") final String name,
            @WebParam(name = "field") final String field,
            @WebParam(name = "fieldConditionOperator") final String fieldConditionOperator,
            @WebParam(name = "fieldConditionValue") final String fieldConditionValue,
            @WebParam(name = "startPeriod") final int startPeriod,
            @WebParam(name = "endPeriod") final int endPeriod,
            @WebParam(name = "duration") final double duration,
            @WebParam(name = "durationCondition") final String durationCondition,
            @WebParam(name = "score") final double score) {
        return execute(serverName, "Add evaluation", new SemServerTask() {
            @Override
            public Object excecute(SemServer sem) throws Exception {
                RankEvaluator evaluator = new RankEvaluator(field, fieldConditionOperator, fieldConditionValue, startPeriod, endPeriod, duration, durationCondition, score);
                sem.addEvaluation(name, evaluator);
                return "Evaluation correctly added";
            }
        });
    }

    /**
     * Rimuove un valutatore dal server
     *
     * @param serverName nome del server
     * @param evaluatorName nome del valutatore
     * @return messaggio
     */
    @WebMethod(operationName = "clearEvaluation")
    public String clearEvaluation(@WebParam(name = "server") final String serverName,
            @WebParam(name = "evaluatorName") final String evaluatorName) {
        return execute(serverName, "Clear evaluation", new SemServerTask() {
            @Override
            public Object excecute(SemServer sem) throws Exception {
                sem.clearEvaluations(evaluatorName);
                return "Evaluation correctly removed";
            }
        });
    }

    /**
     * Esegue il tagclouding
     *
     * @param serverName nome del server
     * @param text testo
     * @param max massimo numero di elmenti
     * @return tagclouding
     */
    @WebMethod(operationName = "tagCloud")
    public String tagCloud(@WebParam(name = "server") String serverName, @WebParam(name = "text") final String text, @WebParam(name = "max") final int max) {
        return execute(serverName, "Tag Cloud", new SemServerTask() {
            @Override
            public Object excecute(SemServer sem) throws Exception {
                Cloud c = sem.tagCloud(text, max);
                return c;
            }
        });
    }

    /**
     * Esegue il tagclouding
     *
     * @param serverName nome del server
     * @param texts testi da analizzare
     * @param max massimo numero di elementi
     * @return tagclouding
     */
    @WebMethod(operationName = "tagClouds")
    public String tagClouds(@WebParam(name = "server") String serverName, @WebParam(name = "texts") final String[] texts, @WebParam(name = "max") final int max) {
        return execute(serverName, "Tag Cloud", new SemServerTask() {
            @Override
            public Object excecute(SemServer sem) throws Exception {
                return sem.tagCloud(texts, max);
            }
        });
    }

    // Metodi di supporto 
    /**
     * Traduce una buffered image in un array di bytes
     *
     * @param img immagine
     * @return array di bytes
     *
     */
    private byte[] getBytesFromImage(BufferedImage img) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(img, "png", baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (Exception e) {
            return new byte[0];
        }
    }

    /**
     * Ritorna il server dato il nome
     *
     * @param name nome del server
     * @return server
     */
    private SemServer getSemServer(String name) {
        return SemServerConfiguration.getInstance().getServer(name);
    }

    /**
     * Ritorna una lista di json rappresentati una classificazione
     *
     * @param classifications classificazione
     * @return json di classificazioni
     */
    private String[] getJSonClassifications(List<ClassificationPath> classifications) {
        String[] ret = new String[classifications.size()];
        for (int i = 0; i < classifications.size(); i++) {
            try {
                ret[i] = mapper.writeValueAsString(classifications.get(i));
            } catch (Exception e) {
                SemServerLog.info(e);
            }
        }
        return ret;
    }

    /**
     * Costruisce il messaggio di ritorno del server
     *
     * @param message messaggio da ritornare
     * @param content contenuto da ritornare
     * @return json contenente messaggio e contenuto
     */
    private String getServerMessage(String message, Object content) {
        try {
            String json;
            if (content instanceof JSONObject) {
                JSONObject doc = (JSONObject) content;
                json = doc.toString();
            } else if (content instanceof List) {
                List<String> toMap = new ArrayList<>();
                List cList = (List) content;
                for (Object o : cList) {
                    String element = mapper.writeValueAsString(o);
                    toMap.add(element);
                }
                json = mapper.writeValueAsString(toMap);
            } else if (content instanceof Cloud) {
                Cloud c = (Cloud) content;
                List<Tag> tags = c.allTags();
                List<String> ret = new ArrayList<>();
                for (Tag tag:tags) {
                    JSONObject jo = new JSONObject();
                    jo.put("name", tag.getName());
                    jo.put("link", tag.getLink());
                    jo.put("score",tag.getScore());
                    ret.add(jo.toString());
                }
                json = mapper.writeValueAsString(ret);
            } else {
                json = mapper.writeValueAsString(content);
            }
            Message msg = new Message(message, json);
            return mapper.writeValueAsString(msg);
        } catch (Exception e) {
            return getError(e.getLocalizedMessage());
        }

    }

    /**
     * Costruisce il json per ritornare un messaggio di errore
     *
     * @param message messaggio di errore
     * @return json con il messaggio di errore
     */
    private String getError(String message) {
        return getServerMessage("Error", message);
    }

    /**
     * Gestisce l'esecuzione dei task del server
     *
     * @param serverName nome del server
     * @param message messaggio da ritornare
     * @param task task da eseguire
     * @return risultato json dell'elaborazione
     */
    private String execute(String serverName, String message, SemServerTask task) {
        SemServer sem = getSemServer(serverName);
        if (sem != null) {
            try {
                return getServerMessage(message, task.excecute(sem));
            } catch (Exception e) {
                e.printStackTrace();
                return getError(e.getMessage());
            }
        }
        return getError("Server not found!");
    }

}
