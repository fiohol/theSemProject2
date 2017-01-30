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
package org.thesemproject.engine.classification;

import java.util.ArrayList;
import org.thesemproject.commons.classification.ClassificationPath;
import org.thesemproject.commons.utils.LogGui;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.classification.SimpleNaiveBayesClassifier;
import org.apache.lucene.index.LeafReader;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.lucene.classification.KNearestNeighborClassifier;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.thesemproject.commons.classification.NodeData;

import org.thesemproject.commons.utils.interning.InternPool;

/**
 * Questo oggetto rappresenta un nodo gerarchico di classificazione con i
 * relativi figli e i classificatori bayesiano e knn
 */
public class TrainableNodeData extends NodeData {

    private final Map<String, SimpleNaiveBayesClassifier> classifiers; //classificatore sui suoi figli
    private final Map<String, KNearestNeighborClassifier> knns; //classificatore sui suoi figli
    /**
     * Fattore K per KNN
     */
    protected final int k;
    /**
     * True se il nodo è istruito
     */
    protected boolean trained;

    /**
     * Crea un nodo radice (ROOT)
     *
     * @param startLevel livello di partenza
     * @param k soglia KNN
     * @param intern internizzatore di stringhe
     */
    public TrainableNodeData(int startLevel, int k, InternPool intern) {
        super(startLevel, intern);
        this.k = k;
        this.classifiers = new HashMap<>();
        this.knns = new HashMap<>();
        this.trained = false;
    }

    /**
     * Crea un nodo clonando un altro nodo
     *
     * @param nd nodo da clonare
     */
    public TrainableNodeData(TrainableNodeData nd) {
        super(nd);
        this.k = 1;
        this.classifiers = new HashMap<>(nd.classifiers);
        this.knns = new HashMap<>(nd.knns);
        this.trained = nd.trained;
    }

    /**
     * Crea un nodo dell'albero
     *
     * @param nodeName nome del nodo
     * @param parent nodo padre
     * @param k fattore K per il classificatore KNN
     * @param intern internizzatore stringhe
     * @throws Exception Eccezione
     */
    public TrainableNodeData(String nodeName, NodeData parent, int k, InternPool intern) throws Exception {
        super(nodeName, parent, intern);
        this.k = 1;
        this.classifiers = new HashMap<>();
        this.knns = new HashMap<>();
        this.trained = false;

    }

    /**
     * Ritorna una struttura di classificazione a partire da una lista di righe
     * dove la struttura è contenuta con i nomi dei nodi separati da tabulatore
     * E' utilizzata per ricostruire la struttura a partire da un file csv
     * ottenuto esportando la struttura dal gui editor
     *
     * @param rows righe del file csv
     * @param k fattore K per il KNN
     * @param intern internizzatore
     * @return root della struttura
     */
    public static TrainableNodeData getTrainableNodeData(List<String> rows, int k, InternPool intern) {
        TrainableNodeData root = new TrainableNodeData(1, k, intern);
        Set<String> categories = new HashSet<>();
        for (String row : rows) {
            String[] doc = row.split("\t");
            if (doc.length > 0) {
                String level1 = (String) intern.intern(doc[0]);
                if (level1 != null) {
                    if (!categories.contains(level1)) { //Nuova categoria di livello 1
                        addTrainableNode(root, categories, level1, k, intern);
                    }
                    if (doc.length > 1) {
                        String level2 = (String) intern.intern(doc[1]);
                        if (!categories.contains(level2)) { //Nuova categoria di livello 2
                            TrainableNodeData parent = (TrainableNodeData) root.getNode(level1);
                            addTrainableNode(parent, categories, level2, k, intern);
                        }
                        if (doc.length > 2) {
                            String level3 = (String) intern.intern(doc[2]);
                            if (!categories.contains(level3)) { //Nuova categoria di livello 3
                                TrainableNodeData p1 = (TrainableNodeData) root.getNode(level1);
                                if (p1 != null) {
                                    TrainableNodeData p2 = (TrainableNodeData) p1.getNode(level2);
                                    addTrainableNode(p2, categories, level3, k, intern);
                                }
                            }
                            if (doc.length > 3) {
                                String level4 = (String) intern.intern(doc[3]);
                                if (!categories.contains(level4)) { //Nuova categoria di livello 4
                                    TrainableNodeData p1 = (TrainableNodeData) root.getNode(level1);
                                    if (p1 != null) {
                                        TrainableNodeData p2 = (TrainableNodeData) p1.getNode(level2);
                                        if (p2 != null) {
                                            TrainableNodeData p3 = (TrainableNodeData) p2.getNode(level3);
                                            addTrainableNode(p3, categories, level4, k, intern);
                                        }
                                    }
                                }
                                if (doc.length > 4) {
                                    String level5 = (String) intern.intern(doc[4]);
                                    if (!categories.contains(level5)) { //Nuova categoria di livello 5
                                        TrainableNodeData p1 = (TrainableNodeData) root.getNode(level1);
                                        if (p1 != null) {
                                            TrainableNodeData p2 = (TrainableNodeData) p1.getNode(level2);
                                            if (p2 != null) {
                                                TrainableNodeData p3 = (TrainableNodeData) p2.getNode(level3);
                                                if (p3 != null) {
                                                    TrainableNodeData p4 = (TrainableNodeData) p3.getNode(level4);
                                                    addTrainableNode(p4, categories, level5, k, intern);
                                                }
                                            }
                                        }
                                    }
                                    if (doc.length > 5) {
                                        String level6 = (String) intern.intern(doc[5]);
                                        if (!categories.contains(level6)) { //Nuova categoria di livello 6
                                            TrainableNodeData p1 = (TrainableNodeData) root.getNode(level1);
                                            if (p1 != null) {
                                                TrainableNodeData p2 = (TrainableNodeData) p1.getNode(level2);
                                                if (p2 != null) {
                                                    TrainableNodeData p3 = (TrainableNodeData) p2.getNode(level3);
                                                    if (p3 != null) {
                                                        TrainableNodeData p4 = (TrainableNodeData) p3.getNode(level4);
                                                        if (p4 != null) {
                                                            TrainableNodeData p5 = (TrainableNodeData) p4.getNode(level5);
                                                            addTrainableNode(p5, categories, level6, k, intern);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return root;
    }

    private static void addTrainableNode(TrainableNodeData parent, Set<String> cats, String name, int k, InternPool intern) {
        if (parent != null) {
            if (name.trim().length() > 0) {
                TrainableNodeData node = (TrainableNodeData) parent.getNode((String) intern.intern(name));
                if (node == null) {
                    try {
                        node = new TrainableNodeData((String) intern.intern(name), parent, k, intern);
                    } catch (Exception exception) {
                        LogGui.printException(exception);
                    }
                }
            }
            cats.add((String) intern.intern(name));
        }
    }

    /**
     * Ritorna la rappresentazione di una struttura di classificazione in
     * formato NodeData a partire da un file xml
     *
     * @param document file xml
     * @param intern internizzatore
     * @return root della struttura
     */
    public static TrainableNodeData getNodeData(Document document, InternPool intern) {
        TrainableNodeData root = null;
        Element classTree = document.getRootElement();
        if ("ClassificationTree".equals(classTree.getName())) {
            List<Element> children = classTree.getChildren();
            for (Element element : children) {
                if ("Node".equals(element.getName())) {
                    String nodeName = element.getAttributeValue("nodeName");
                    if ("root".equals(nodeName)) {
                        int k = Integer.parseInt(element.getAttributeValue("k"));
                        int s = 1;
                        String sl = element.getAttributeValue("sl");
                        if (sl != null) {
                            s = Integer.parseInt(sl);
                        }
                        root = new TrainableNodeData(s, k, intern);
                        Element labelsElement = element.getChild("labels");
                        if (labelsElement != null) {
                            List<Attribute> attributes = labelsElement.getAttributes();
                            for (Attribute attribute : attributes) {
                                root.setLabel(attribute.getName(), attribute.getValue());
                            }
                        }
                        Element childrenElement = element.getChild("childrens");
                        if (childrenElement != null) {
                            for (Element childNodeElement : childrenElement.getChildren()) {
                                processChildrenElement(childNodeElement, root, intern);
                            }
                        }
                    }
                }
            }
        }
        return root;
    }

    /**
     * Processa un figlio
     *
     * @param element elemento
     * @param parent nodo parente
     * @param intern intern
     */
    protected static void processChildrenElement(Element element, TrainableNodeData parent, InternPool intern) {
        try {
            String nodeName = org.thesemproject.commons.utils.StringUtils.firstUpper(element.getAttributeValue("nodeName"));
            int k = Integer.parseInt(element.getAttributeValue("k"));
            TrainableNodeData node = new TrainableNodeData(nodeName, parent, k, intern);
            Element labelsElement = element.getChild("labels");
            if (labelsElement != null) {
                List<Attribute> attributes = labelsElement.getAttributes();
                attributes.stream().forEach((attribute) -> {
                    node.setLabel(attribute.getName(), attribute.getValue());
                });
            }
            Element childrenElement = element.getChild("childrens");
            if (childrenElement != null) {
                childrenElement.getChildren().stream().forEach((childNodeElement) -> {
                    processChildrenElement(childNodeElement, node, intern);
                });
            }
        } catch (Exception e) {
            LogGui.printException(e);
        }
    }

    /**
     * Si occupa del training del classificatore di nodo
     *
     * @param ar reader lucene
     * @param analyzer analizzatore sintattico
     * @param language lingua
     */
    public void train(LeafReader ar, Analyzer analyzer, String language) {
        try {
            trained = true;
            SimpleNaiveBayesClassifier classifier = classifiers.get(language);
            if (classifier == null) {
                classifier = new SimpleNaiveBayesClassifier();
            }
            KNearestNeighborClassifier knn = knns.get(language);
            if (knn == null) {
                knn = new KNearestNeighborClassifier(k);
            }
            LogGui.info("Istruisco il nodo: " + nodeName);
            if (level == null) { //root
                //Dobbiamo istruire il nodo con tutti i documenti usando il field level1
                switch (startLevel) {
                    case 1:
                        classifier.train(ar, IndexManager.BODY, NodeData.LEVEL_1, analyzer);
                        knn.train(ar, IndexManager.BODY, NodeData.LEVEL_1, analyzer);
                        break;
                    case 2:
                        classifier.train(ar, IndexManager.BODY, NodeData.LEVEL_2, analyzer);
                        knn.train(ar, IndexManager.BODY, NodeData.LEVEL_2, analyzer);
                        break;
                    case 3:
                        classifier.train(ar, IndexManager.BODY, NodeData.LEVEL_3, analyzer);
                        knn.train(ar, IndexManager.BODY, NodeData.LEVEL_3, analyzer);
                        break;
                    case 4:
                        classifier.train(ar, IndexManager.BODY, NodeData.LEVEL_4, analyzer);
                        knn.train(ar, IndexManager.BODY, NodeData.LEVEL_4, analyzer);
                        break;
                    case 5:
                        classifier.train(ar, IndexManager.BODY, NodeData.LEVEL_5, analyzer);
                        knn.train(ar, IndexManager.BODY, NodeData.LEVEL_5, analyzer);
                        break;
                    default:
                        break;
                }
            } else {
                classifier.train(ar, IndexManager.BODY, getNodeCodeForFilter(), analyzer);
                knn.train(ar, IndexManager.BODY, getNodeCodeForFilter(), analyzer);
            }

            /*
            else if (IndexManager.LEVEL_1.equalsIgnoreCase(level)) {
                //Ci troviamo su una categoria figlia di root
                //Dobbiamo istruire il suo classificatore con tutti i documenti che hanno in Level_1 il nome della categoria
                //con i soli campi di Level_2
                TermQuery query = new TermQuery(new Term(IndexManager.LEVEL_1, getNodeCodeForFilter()));
                classifier.train(ar, IndexManager.BODY, IndexManager.LEVEL_2, analyzer, query);
                knn.train(ar, IndexManager.BODY, IndexManager.LEVEL_2, analyzer, query);
            } else if (IndexManager.LEVEL_2.equalsIgnoreCase(level)) {
                //Ci troviamo su una categoria figlia di un livello 1
                //Dobbiamo istruire il suo classificatore con tutti i documenti che hanno in Level_2 il nome della categoria
                //con i soli campi di Level_3
                TermQuery query = new TermQuery(new Term(IndexManager.LEVEL_2, getNodeCodeForFilter()));
                classifier.train(ar, IndexManager.BODY, IndexManager.LEVEL_3, analyzer, query);
                knn.train(ar, IndexManager.BODY, IndexManager.LEVEL_3, analyzer, query);
            } else if (IndexManager.LEVEL_3.equalsIgnoreCase(level)) {
                //Ci troviamo su una categoria figlia di un livello 2
                //Dobbiamo istruire il suo classificatore con tutti i documenti che hanno in Level_3 il nome della categoria
                //con i soli campi di Level_4
                TermQuery query = new TermQuery(new Term(IndexManager.LEVEL_3, getNodeCodeForFilter()));
                classifier.train(ar, IndexManager.BODY, IndexManager.LEVEL_4, analyzer, query);
                knn.train(ar, IndexManager.BODY, IndexManager.LEVEL_4, analyzer, query);
            } else if (IndexManager.LEVEL_4.equalsIgnoreCase(level)) {
                //Ci troviamo su una categoria figlia di un livello 3
                //Dobbiamo istruire il suo classificatore con tutti i documenti che hanno in Level_4 il nome della categoria
                //con i soli campi di Level_5
                TermQuery query = new TermQuery(new Term(IndexManager.LEVEL_4, getNodeCodeForFilter()));
                classifier.train(ar, IndexManager.BODY, IndexManager.LEVEL_5, analyzer, query);
                knn.train(ar, IndexManager.BODY, IndexManager.LEVEL_5, analyzer, query);
            } else if (IndexManager.LEVEL_5.equalsIgnoreCase(level)) {
                //Ci troviamo su una categoria figlia di un livello 4
                //Dobbiamo istruire il suo classificatore con tutti i documenti che hanno in Level_5 il nome della categoria
                //con i soli campi di Level_6
                TermQuery query = new TermQuery(new Term(IndexManager.LEVEL_5, getNodeCodeForFilter()));
                classifier.train(ar, IndexManager.BODY, IndexManager.LEVEL_6, analyzer, query);
                knn.train(ar, IndexManager.BODY, IndexManager.LEVEL_6, analyzer, query);
            } */
            classifiers.put(language, classifier);
            knns.put(language, knn);
        } catch (Exception e) {
            LogGui.printException(e);
        }
    }

    /**
     * Ritorna il classificatore bayesiano in una specifica lingua
     *
     * @param language lingua del classificatore
     * @return classificatore bayesiano
     */
    public SimpleNaiveBayesClassifier getClassifier(String language) {
        return classifiers.get(language);
    }

    /**
     * Ritorna il classificatore KNN per una specifica lingua
     *
     * @param language lingua del classificatore
     * @return classificatore KNN
     */
    public KNearestNeighborClassifier getKnn(String language) {
        return knns.get(language);

    }

    /**
     * Controlla se il percorso è istruito
     *
     * @since 1.6
     * @param cp percorso
     * @return true se è istruito
     */
    public boolean isTrained(ClassificationPath cp) {
        String[] nodes = cp.getPath();
        return TrainableNodeData.this.isTrained(nodes, 0);
    }

    /**
     * Verifica se un path è istruito
     *
     * @param nodes percorso
     * @param level livello
     * @return true se il nodo è istruito
     */
    protected boolean isTrained(String[] nodes, int level) {
        TrainableNodeData child = null;
        if (nodes.length <= level) {
            return false;
        }
        String value = nodes[level];
        if (value != null) {
            child = (TrainableNodeData) children.get(value);
            if (child == null) {
                return false;
            }
            return child.isTrained(nodes, level + 1);
        }
        return this.isTrained();

    }

    /**
     * Torna true se il nodo è stato istruito
     *
     * @return true se istruito
     */
    public boolean isTrained() {
        return trained;
    }

    /**
     * Imposta true se il nodo è istruito
     *
     * @param trained true se istruito
     */
    public void setTrained(boolean trained) {
        this.trained = trained;
    }

    /**
     * Ritorna l'XML
     *
     * @return XML
     */
    public Element getXml() {
        Element element = new Element("Node");
        element.setAttribute("nodeName", nodeName);
        element.setAttribute("k", String.valueOf(k));
        element.setAttribute("sl", String.valueOf(startLevel));
        Element labelsElement = new Element("labels");
        labels.keySet().stream().forEach((key) -> {
            labelsElement.setAttribute(key, labels.get(key));
        });
        element.addContent(labelsElement);
        Element childrenElement = new Element("childrens");
        children.values().stream().forEach((child) -> {
            childrenElement.addContent(((TrainableNodeData) child).getXml());
        });
        element.addContent(childrenElement);
        return element;
    }

    /**
     * Ritorna la rappresentazione XML della struttura di classificazione
     *
     * @param root Nodo radice
     * @return Document XML che rappresenta la struttura di classificazione
     */
    public static Document getDocument(TrainableNodeData root) {
        if (root == null) {
            return null;
        }
        if ("root".equals(root.nodeName)) {
            Document document = new Document();
            Element classTree = new Element("ClassificationTree");
            classTree.addContent(root.getXml());
            document.addContent(classTree);
            return document;
        }
        return null;
    }

    /**
     * Aggiunge un figlio
     *
     * @param path percorso
     * @param level livello
     */
    public void addChild(Object[] path, int level) {
        if (level < path.length - 1) {
            TrainableNodeData child = (TrainableNodeData) children.get(path[level + 1].toString());
            if (path.length == level + 2) {
                try {
                    TrainableNodeData nd = new TrainableNodeData(path[path.length - 1].toString(), this, k, intern);
                } catch (Exception ex) {
                    LogGui.printException(ex);
                }
            } else if (child != null) {
                child.addChild(path, level + 1);
            }
        }
    }

}
