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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import org.jdom2.Element;
import java.util.List;
import java.util.Map;
import javax.persistence.*;
import javax.validation.constraints.Size;
import org.bson.Document;
import org.mcavallo.opencloud.Cloud;
import org.thesemproject.commons.tagcloud.TagCloudResults;
import org.thesemproject.commons.utils.LogGui;
import org.thesemproject.commons.utils.interning.InternPool;
import org.thesemproject.engine.classification.ClassificationPath;
import org.thesemproject.engine.classification.MulticlassEngine;
import org.thesemproject.engine.classification.MyAnalyzer;
import org.thesemproject.engine.classification.NodeData;
import org.thesemproject.engine.classification.Tokenizer;
import org.thesemproject.engine.parser.DocumentParser;
import org.thesemproject.engine.segmentation.SegmentConfiguration;
import org.thesemproject.engine.segmentation.SegmentEngine;
import org.thesemproject.engine.segmentation.SegmentationResults;
import org.thesemproject.engine.segmentation.SegmentationUtils;
import org.thesemproject.engine.segmentation.functions.rank.RankEvaluations;
import org.thesemproject.engine.segmentation.functions.rank.RankEvaluator;

/**
 * Questo oggetto rappresenta un server OpenSem
 *
 * @author The Sem Project
 */
@Entity
public class SemServer {

    @Transient
    private final static InternPool intern = new InternPool();
    @Transient
    private final MulticlassEngine multiclassEngine;
    @Transient
    private final SegmentEngine segmentEngine;
    @Transient
    private final static DocumentParser documentParser = new DocumentParser(intern);
    @Transient
    private final Map<String, RankEvaluations> rankEvaluations;

    @Size(min = 4, max = 2000)
    String path = "";

    @Id
    @Size(min = 4, max = 10)
    String name = "";

    @Size(min = 4, max = 2000)
    String ocrPath = "";

    @Transient
    private boolean isInit;

    /**
     * Costruttore
     *
     * @param name nome dell'istanza
     * @param path percorso indice e configurazioni
     * @param ocrPath percorso OCR
     */
    public SemServer(String name, String path, String ocrPath) {
        this.name = name;
        multiclassEngine = new MulticlassEngine(intern);
        segmentEngine = new SegmentEngine();
        rankEvaluations = new HashMap<>();
        isInit = false;
        this.path = path;
        this.ocrPath = ocrPath;
        init();
    }

    /**
     * Costruttore da XML
     *
     * @param server elemento di configurazione
     */
    public SemServer(Element server) {
        this.name = server.getAttributeValue("name");
        multiclassEngine = new MulticlassEngine(intern);
        segmentEngine = new SegmentEngine();
        rankEvaluations = new HashMap<>();
        isInit = false;
        this.path = server.getAttributeValue("path");
        this.ocrPath = server.getAttributeValue("ocrPath");
        init();
        List<Element> ranks = server.getChildren("rank");
        for (Element rank : ranks) {
            String name = rank.getAttributeValue("name");
            List<Element> rankEvaluators = rank.getChildren("evaluator");
            for (Element reankEvaluator : rankEvaluators) {
                try {
                    String field = reankEvaluator.getAttributeValue("field");
                    String fieldConditionOperator = reankEvaluator.getAttributeValue("fieldConditionOperator");
                    String fieldConditionValue = reankEvaluator.getAttributeValue("fieldConditionValue");
                    int startYear = Integer.parseInt(reankEvaluator.getAttributeValue("startYear"));
                    int endYear = Integer.parseInt(reankEvaluator.getAttributeValue("endYear"));
                    double duration = Double.parseDouble(reankEvaluator.getAttributeValue("duration"));
                    String durationCondition = reankEvaluator.getAttributeValue("durationCondition");
                    double score = Double.parseDouble(reankEvaluator.getAttributeValue("score"));
                    RankEvaluator re = new RankEvaluator(field, fieldConditionOperator, fieldConditionValue, startYear, endYear, duration, durationCondition, score);
                    addEvaluation(name, re);
                } catch (Exception e) {
                    SemServerLog.info("Exception " + e);
                }
            }
        }
    }

    /**
     * Istanzia un oggetto server
     */
    public SemServer() {
        this.name = "";
        multiclassEngine = new MulticlassEngine(intern);
        segmentEngine = new SegmentEngine();
        rankEvaluations = new HashMap<>();
        isInit = false;
        this.path = "";
        this.ocrPath = "";
    }

    //-------------------- SERVER ------------------------------------------
    /**
     * Inizializza i componenti
     */
    public void init() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                isInit = false;
                SemServerLog.info("Inizializzazione server");
                LogGui.printMemorySummary();
                SemServerLog.info("Init multiclassEngine");
                multiclassEngine.init(path, 1);
                SemServerLog.info("Init segmenter...");
                segmentEngine.init(getModelPath(), multiclassEngine);
                isInit = true;
            }
        });
        t.setDaemon(true);
        t.start();
    }

    /**
     * Fa shutdown del server
     */
    public void shutdown() {
        segmentEngine.closeAllReaders();
    }

    /**
     * Ritorna il percorso del modello
     *
     * @return percorso del modello
     */
    public String getModelPath() {
        return new File(path + "/segments.xml").getAbsolutePath();
    }

    /**
     * Crea un file a partire da un array di bytes
     *
     * @param bytes array di bytes
     * @return file
     * @throws IOException eccezione
     */
    private File getFileFromBinary(byte[] bytes) throws IOException {
        File tempFile = File.createTempFile(java.util.UUID.randomUUID().toString(), ".sem");
        RandomAccessFile raf = new RandomAccessFile(tempFile, "rw");
        raf.write(bytes);
        raf.close();
        return tempFile;
    }

    //-------------------- PARSER ------------------------------------------
    /**
     * Ritorna il testo a partire da un file
     *
     * @param fileName percorso del file
     * @param formatted true se si vuole il testo formattato, false se si vuole
     * il plaintext
     * @return testo
     */
    public String getTextFromFile(String fileName, boolean formatted) {
        return getTextFromFile(new File(fileName), formatted);
    }

    /**
     * Ritorna il testo a partire da un file
     *
     * @param f percorso del file
     * @param formatted true se si vuole il testo formattato, false se si vuole
     * il plaintext
     * @return testo
     */
    public String getTextFromFile(File f, boolean formatted) {
        if (formatted) {
            return documentParser.getHtmlFromFile(f);
        }
        return documentParser.getTextFromFile(f, ocrPath);
    }

    /**
     * Ritorna il testo a partire da uno stream binario
     *
     * @param bytes binario
     * @param formatted true se si vuole il testo formattato. false se si vuole
     * il plaintext
     * @return testo
     * @throws IOException eccezione
     */
    public String getTextFromBynary(byte[] bytes, boolean formatted) throws IOException {
        return getTextFromFile(getFileFromBinary(bytes), formatted);
    }

    /**
     * Identifica la lingua in cui il testo è scritto
     *
     * @param text testo
     * @return lingua
     */
    public String getLanguageFromText(String text) {
        return documentParser.getLanguageFromText(text);
    }

    /**
     * Estrae le immagini dal file
     *
     * @param file percorso del file
     * @return mappa nome immagine binario immagine
     */
    public Map<String, BufferedImage> getImagesFromFile(String file) {
        return getImagesFromFile(new File(file));
    }

    /**
     * Estrae le immagini dal file
     *
     * @param file file
     * @return mappa nome immagine binario immagine
     */
    public Map<String, BufferedImage> getImagesFromFile(File file) {
        return documentParser.getImagesFromFile(file);
    }

    /**
     * Estrae le immagini dal binario
     *
     * @param bytes binario
     * @return immagini
     * @throws IOException
     */
    public Map<String, BufferedImage> getImagesFromBinary(byte[] bytes) throws IOException {
        return documentParser.getImagesFromFile(getFileFromBinary(bytes));
    }

    /**
     * Estrae l'immagine più grande da un file
     *
     * @param file percorso file
     * @return immagine
     */
    public BufferedImage getPictureFromFile(String file) {
        return getPictureFromFile(new File(file));
    }

    /**
     * Estrae l'immagine più grande da un file
     *
     * @param file file
     * @return immagine
     */
    public BufferedImage getPictureFromFile(File file) {
        return documentParser.getLargestImageFromFile(file);
    }

    /**
     * Estrae l'immagine dal binario
     *
     * @param bytes binario
     * @return immagine
     * @throws IOException
     */
    public BufferedImage getPictureFromBinary(byte[] bytes) throws IOException {
        return getPictureFromFile(getFileFromBinary(bytes));
    }

    //-------------------- CLASSIFICAZIONE----------------------------------
    /**
     * Classifica un testo
     *
     * @param text testo
     * @return lista delle possibili classificazioni
     */
    public List<ClassificationPath> getClassifications(String text) {
        return multiclassEngine.bayesClassify(text, getLanguageFromText(text));
    }

    /**
     * Classifica un file
     *
     * @param file file
     * @return lista delle possibili classificazioni
     */
    public List<ClassificationPath> getClassifications(File file) {
        String text = getTextFromFile(file, false);
        return multiclassEngine.bayesClassify(text, getLanguageFromText(text));
    }

    /**
     * Classifica un binario
     *
     * @param bytes binario
     * @return lista delle possibili classificazioni
     * @throws IOException eccezione
     */
    public List<ClassificationPath> getClassifications(byte[] bytes) throws IOException {
        String text = getTextFromBynary(bytes, false);
        return multiclassEngine.bayesClassify(text, getLanguageFromText(text));
    }

    /**
     * Ritorna il nodo root di classificazione
     *
     * @return nodo Root
     */
    public NodeData getClassificationTree() {
        return multiclassEngine.getRoot();
    }

    //-------------------- SEGMENTAZIONE------------------------------------
    /**
     * Segmenta (e classifica) un testo
     *
     * @param text testo
     * @return segmentazione
     */
    public Map<SegmentConfiguration, List<SegmentationResults>> getTextSegmentation(String text) {
        return segmentEngine.getSegments(text, multiclassEngine, getLanguageFromText(text));
    }

    /**
     * Segmenta (e classifica) un file
     *
     * @param file file
     * @return segmentazione
     */
    public Map<SegmentConfiguration, List<SegmentationResults>> getFileSegmentation(File file) {
        String text = getTextFromFile(file, false);
        return getTextSegmentation(text);
    }

    /**
     * Segmenta (e classifica) un flusso binario
     *
     * @param bytes flusso binario
     * @return segmentazione
     * @throws IOException eccezione
     */
    public Map<SegmentConfiguration, List<SegmentationResults>> getBinarySegmentation(byte[] bytes) throws IOException {
        return getFileSegmentation(getFileFromBinary(bytes));
    }

    /**
     * Ritorna la versione html della segmentazione
     *
     * @param text testo da segmentare
     * @return testo html
     * @throws Exception eccezione
     */
    public String getFormattedHtml(String text) throws Exception {
        String language = getLanguageFromText(text);
        Map<SegmentConfiguration, List<SegmentationResults>> results = getTextSegmentation(text);
        return SegmentationUtils.getHtml(results, language);
    }

    /**
     * Ritorna la versione html della segmentazione
     *
     * @param file file
     * @return testo html
     * @throws Exception eccezione
     */
    public String getFormattedHtmlFromFile(File file) throws Exception {
        return getFormattedHtml(getTextFromFile(file, false));
    }

    /**
     * Ritorna la versione html della segmentazione
     *
     * @param bytes binario
     * @return testo html
     * @throws Exception eccezione
     */
    public String getFormattedHtmlFromBinary(byte[] bytes) throws Exception {
        return getFormattedHtml(getTextFromBynary(bytes, false));
    }

    /**
     * Ritorna un BSon a partire dalla segmentazione
     *
     * @param results segmentazione
     * @return Rappresentazione BSon
     * @throws Exception eccezione
     */
    public Document getDocument(Map<SegmentConfiguration, List<SegmentationResults>> results) throws Exception {
        Document document = new Document();
        document = SegmentationUtils.getDocument(document, results);
        if (!rankEvaluations.isEmpty()) {
            for (String rankName : rankEvaluations.keySet()) {
                RankEvaluations re = rankEvaluations.get(rankName);
                double rank = re.evaluate(results);
                document.put("Rank." + rankName, rank);
            }
        }
        return document;
    }

    //------------------------ TAG CLOUD -----------------------------------------------
    /**
     * Ritorna il risultato del tagcloud su un testo
     *
     * @param text testo
     * @param max massimo numero di tag
     * @return Tagcloud
     * @throws Exception eccezione
     */
    public Cloud tagCloud(String text, int max) throws Exception {
        TagCloudResults result = new TagCloudResults();
        MyAnalyzer analyzer = multiclassEngine.getAnalyzer(getLanguageFromText(text));
        Tokenizer.getTagClasses(result, text, "", analyzer);
        return result.getCloud(max);
    }

    /**
     * Ritorna il risultato del tagcloud su una lista di testi
     *
     * @param texts testi
     * @param max massimo numero di tag
     * @return Tagcloud
     * @throws Exception eccezione
     */
    public Cloud tagCloud(String[] texts, int max) throws Exception {
        TagCloudResults result = new TagCloudResults();
        for (String text : texts) {
            MyAnalyzer analyzer = multiclassEngine.getAnalyzer(getLanguageFromText(text));
            Tokenizer.getTagClasses(result, text, "", analyzer);
        }
        return result.getCloud(max);
    }

    //------------------------ RANK -----------------------------------------------
    /**
     * Aggiunge un valutatore di rank
     *
     * @param evaluatorName nome del valutatore
     * @param evaluator valutatore
     */
    public void addEvaluation(String evaluatorName, RankEvaluator evaluator) {
        RankEvaluations re = rankEvaluations.get(evaluatorName);
        if (re == null) {
            re = new RankEvaluations();
        }
        re.addRule(evaluator);
    }

    /**
     * Ripulisce i valutatori
     *
     * @param evaluatorName nome del valutatore
     */
    public void clearEvaluations(String evaluatorName) {
        RankEvaluations re = rankEvaluations.get(evaluatorName);
        if (re != null) {
            re.getEvaluators().clear();
        }
    }

    /**
     * Ritorna il nome del server
     *
     * @return nome del server
     */
    public String getName() {
        return name;
    }

    /**
     * Ritorna la rappresentazione XML del server
     *
     * @return rappresentazioneXML
     */
    public Element getXML() {
        Element server = new Element("server");
        server.setAttribute("name", name);
        server.setAttribute("path", path);
        server.setAttribute("ocrPath", ocrPath);
        if (!rankEvaluations.isEmpty()) {
            for (String rankName : rankEvaluations.keySet()) {
                Element rank = new Element("rank");
                rank.setAttribute("name", rankName);
                server.addContent(rank);
                for (RankEvaluator re : rankEvaluations.get(rankName).getEvaluators()) {
                    Element rankEvaluator = new Element("evaluator");
                    rankEvaluator.setAttribute("field", re.getField());
                    rankEvaluator.setAttribute("fieldConditionOperator", re.getFieldConditionOperator());
                    rankEvaluator.setAttribute("fieldConditionValue", re.getFieldConditionValue());
                    rankEvaluator.setAttribute("startYear", String.valueOf(re.getStartYear()));
                    rankEvaluator.setAttribute("endYear", String.valueOf(re.getEndYear()));
                    rankEvaluator.setAttribute("duration", String.valueOf(re.getDuration()));
                    if (re.getDurationCondition() != null) {
                        rankEvaluator.setAttribute("durationCondition", re.getDurationCondition());
                    }
                    rankEvaluator.setAttribute("score", String.valueOf(re.getScore()));
                    rank.addContent(rankEvaluator);
                }
            }
        }
        return server;
    }

    /**
     *
     * @return ritorna il path dell'indice
     */
    public String getPath() {
        return path;
    }

    /**
     * Imposta il path dell'indice
     *
     * @param path path dell'indice
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Ritorna il path dove il sistema OCR scrive
     *
     * @return path OCR
     */
    public String getOcrPath() {
        return ocrPath;
    }

    /**
     * Imposta il path dell'OCR
     *
     * @param ocrPath path dell'OCR
     */
    public void setOcrPath(String ocrPath) {
        this.ocrPath = ocrPath;
    }

    /**
     * Imposta il nome del server
     *
     * @param name nome del server
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Dice se il server è inizializzato
     * @return true se inizializzato
     */
    public boolean isIsInit() {
        return isInit;
    }

    /**
     * Imposta lo stato di inizializzazione del server
     * @param isInit stato inizializzazione
     */
    public void setIsInit(boolean isInit) {
        this.isInit = isInit;
    }
    
    

}
