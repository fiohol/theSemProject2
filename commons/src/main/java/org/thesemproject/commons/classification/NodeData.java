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
package org.thesemproject.commons.classification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.thesemproject.commons.utils.LogGui;
import org.thesemproject.commons.utils.interning.InternPool;

/**
 * Rappresenta un nodo della struttura di classificazione
 *
 * @author The Sem Project
 */
public class NodeData {

    /**
     * Identifica il nome del field che deve contenere l'ID della categoria di
     * terzo livello dove è classificato il documento
     */
    public static final String LEVEL_3 = "level3";
    /**
     * Identifica il nome del field che deve contenere l'ID della categoria di
     * primo livello dove è classificato il documento
     */
    public static final String LEVEL_1 = "level1";
    /**
     * Identifica il nome del field che deve contenere l'ID della categoria di
     * quinto livello dove è classificato il documento
     *
     * @since 1.1
     */
    public static final String LEVEL_5 = "level5";
    /**
     * Identifica il nome del field che deve contenere l'ID della categoria di
     * sesto livello dove è classificato il documento
     *
     * @since 1.1
     */
    public static final String LEVEL_6 = "level6";
    /**
     * Identifica il nome del field che deve contenere l'ID della categoria di
     * secondo livello dove è classificato il documento
     */
    public static final String LEVEL_2 = "level2";
    /**
     * Identifica il nome del field che deve contenere l'ID della categoria di
     * quarto livello dove è classificato il documento
     */
    public static final String LEVEL_4 = "level4";
    /**
     * Nome della categoria
     */
    public String nodeName; //Nome del nodo

    /**
     * Livello della categoria
     */
    public String level;

    /**
     * Livelli di partenza
     */
    protected int startLevel;

    /**
     * Nodo padre
     */
    protected NodeData parent;
    /**
     * Mappa dei figli (nome, figlio)
     */
    protected final Map<String, NodeData> children;
    /**
     * Mappa inversa dei figli
     */
    protected final Map<String, String> reverseMap;
    /**
     * Intern
     */
    protected final InternPool intern;
    /**
     * Traduzioni (etichette). Pensata come mappa lingua, valore (non usata)
     */
    protected final Map<String, String> labels;

    /**
     * Crea un nodo radice (ROOT)
     *
     * @param startLevel livello di partenza
     * @param intern internizzatore di stringhe
     */
    public NodeData(int startLevel, InternPool intern) {
        this.nodeName = "root";
        this.level = null;
        this.parent = null;
        this.children = new HashMap<>();
        this.reverseMap = new HashMap<>();
        this.labels = new HashMap<>();
        this.intern = intern;
        this.startLevel = startLevel;
    }

    /**
     * Costruisce un nodo a partire da un altro nodo
     *
     * @param nd nodo
     */
    public NodeData(NodeData nd) {
        this.nodeName = nd.nodeName;
        this.level = nd.level;
        this.parent = nd.parent;
        this.children = new HashMap<>(nd.children);
        this.reverseMap = new HashMap<>(nd.reverseMap);
        this.labels = new HashMap<>(nd.labels);
        this.intern = nd.intern;
        this.startLevel = nd.startLevel;
    }

    /**
     * Crea un nodo dell'albero
     *
     * @param nodeName nome del nodo
     * @param parent nodo padre
     * @param intern internizzatore stringhe
     * @throws Exception Eccezione
     */
    public NodeData(String nodeName, NodeData parent, InternPool intern) throws Exception {
        this.nodeName = nodeName;
        this.children = new HashMap<>();
        this.reverseMap = new HashMap<>();
        this.intern = intern;
        this.labels = new HashMap<>();
        if (parent != null) {
            if (!parent.children.containsKey(nodeName)) {
                if (parent.parent == null) {
                    this.level = LEVEL_1;
                } else if (parent.parent.parent == null) {
                    this.level = LEVEL_2;
                } else if (parent.parent.parent.parent == null) {
                    this.level = LEVEL_3;
                } else if (parent.parent.parent.parent.parent == null) {
                    this.level = LEVEL_4;
                } else if (parent.parent.parent.parent.parent.parent == null) {
                    this.level = LEVEL_5;
                } else {
                    this.level = LEVEL_6;
                }
                this.parent = parent;
                parent.children.put((String) intern.intern(nodeName), this);
                parent.reverseMap.put(getNodeCodeForFilter(nodeName), (String) intern.intern(nodeName));
            } else {
                throw new Exception("This node already exists");
            }
        } else { //Caso root non ho padri
            this.level = null;
        }

        this.startLevel = -1;
    }

    /**
     * Imposta una label per il nodo in una particolare lingua
     *
     * @param language lingua
     * @param label etichetta
     */
    public void setLabel(String language, String label) {
        labels.put(language, label);
    }

    /**
     * Ritorna il codice del nodo da associare nel motore di classificazione
     *
     * @since 1.3.2
     *
     * @param name nome da cui ricavare il codice univoco
     * @return codice del nodo.
     */
    public static String getNodeCodeForFilter(String name) {
        //Dato che questo popola il filtro, se il nodo non è attivato per la clssificazione
        //La query potrà essere completamente libera.
        final StringBuffer sb = new StringBuffer();
        sb.append("C");
        byte[] b = name.getBytes();
        for (byte b1 : b) {
            sb.append(String.valueOf(b1));
        }
        return sb.toString();
    }

    /**
     * Ritorna il figlio con un determinato nuome
     *
     * @param childrenName nome del filglio
     * @return figlio oppure null se non trovato.
     */
    public NodeData getNode(String childrenName) {
        NodeData nd = children.get(childrenName);
        if (nd == null) {
            for (NodeData ch : children.values()) {
                nd = ch.getNode(childrenName);
                if (nd != null) {
                    return nd;
                }
            }
        }
        return nd;
    }

    /**
     * Verifica se un nodo è una foglia
     *
     * @return true se non ha figli
     */
    public boolean hasChildren() {
        return !children.isEmpty();
    }

    /**
     * Ritorna la lista dei nomi dei figli del nodo corrente
     *
     * @return Lista dei nomi dei figli
     */
    public List<String> getChildrenNames() {
        List<String> ret = new ArrayList<>(children.keySet());
        Collections.sort(ret);
        return ret;
    }

    /**
     * Ritorna la lista dei nomi dei figli del nodo corrente
     *
     * @since 1.6
     * @return Lista dei figli
     */
    public List<NodeData> getChildrens() {
        List<NodeData> ret = new ArrayList<>(children.values());
        return ret;
    }

    /**
     * Ritorna il nome di un nodo dato l'ID
     *
     * @param nid ID del nodo
     * @return nome del nodo
     */
    public String getNameFromId(String nid) {
        String name = reverseMap.get(nid);
        if (name == null) { //Provo a vedere se nei figli c'è il nome
            for (NodeData nd : children.values()) {
                name = nd.getNameFromId(nid);
                if (name != null) {
                    return name;
                }
            }
        }
        return name;
    }

    /**
     * Ritorna una label associat al nodo
     *
     * @param language lingua in cui si vuole la label
     * @return label del nodo
     */
    public String getLabel(String language) {
        if (language == null) {
            return nodeName;
        }
        String name = labels.get(language);
        if (name == null) {
            return nodeName;
        }
        return name;
    }

    /**
     * Ritorna la struttura di classificazione in formato csv (una colonna per
     * ogni livello)
     *
     * @param root Nodo radice
     * @return struttura csv della struttura di classificazione
     */
    public static String getCSVDocument(NodeData root) {
        if (root == null) {
            return null;
        }
        if ("root".equals(root.nodeName)) {
            StringBuffer ret = new StringBuffer();
            ret.append(root.getCSV(ret));
            return ret.toString();
        }
        return null;
    }

    /**
     * Rimuove un nodo figlio
     *
     * @param path percorso
     * @param level livello
     */
    public void removeChild(Object[] path, int level) {
        if (level < path.length - 1) {
            NodeData child = (NodeData) children.get(path[level + 1].toString());
            if (child != null) {
                if (path.length == level + 2) {
                    children.remove(child.nodeName);
                    reverseMap.remove(getNodeCodeForFilter(child.nodeName) + "");
                } else {
                    child.removeChild(path, level + 1);
                }
            }
        }
    }

    /**
     * Aggiunge un figlio
     *
     * @param path percorso
     * @param level livello
     */
    protected void addChild(Object[] path, int level) {
        if (level < path.length - 1) {
            NodeData child = (NodeData) children.get(path[level + 1].toString());
            if (path.length == level + 2) {
                try {
                    NodeData nd = new NodeData(path[path.length - 1].toString(), this, intern);
                } catch (Exception ex) {
                    LogGui.printException(ex);
                }
            } else if (child != null) {
                child.addChild(path, level + 1);
            }
        }
    }

    /**
     * Ritorna la rappresentazione dell'albero in formato CSV
     *
     * @param prefix prefisso
     * @return riga del percorso
     */
    protected StringBuffer getCSV(StringBuffer prefix) {
        StringBuffer ret = new StringBuffer();
        if (!"root".equals(nodeName)) {
            if (prefix.length() == 0) {
                prefix.append(nodeName);
            } else {
                prefix.append("\t").append(nodeName);
            }
        }
        for (String name : children.keySet()) {
            StringBuffer sb = new StringBuffer();
            sb.append(prefix);
            NodeData child = (NodeData) children.get(name);
            ret.append(child.getCSV(sb));
        }
        if (children.isEmpty()) {
            ret.append(prefix).append("\r\n");
        }
        return ret;
    }

    /**
     * Ritorna il livello da cui inizia a classificare
     *
     * @since 1.1
     * @return livello da cui iniziare a classificare
     */
    public int getStartLevel() {
        return startLevel;
    }

    /**
     * Imposta il livello da cui iniziare a classificare
     *
     * @param startLevel livello da cui iniziare a classificare
     */
    public void setStartLevel(int startLevel) {
        this.startLevel = startLevel;
    }

    /**
     * Ritrova il percorso di classificazione di un nodo a partire dalla root
     *
     * @param cp classification path
     * @param node nodo
     * @param level livello
     */
    public static void findPath(ClassificationPath cp, NodeData node, int level) {
        cp.addResult(node.parent.nodeName, 1, level - 1);
        if (level > 0) {
            findPath(cp, node.parent, level - 1);
        }
    }

    /**
     * Codice del nodo ricavato a partire dal nome, da usare nei filtri di
     * lucene
     *
     * @return codice
     */
    protected String getNodeCodeForFilter() {
        return NodeData.getNodeCodeForFilter(nodeName);
    }

    /**
     * Ritorna la visita del sottoalbero a partire dal nodo corrente nel formato
     * confrontabile con ClassificationPath.getCompactClassString();
     *
     * @since 1.3.4
     * @param parentPath percorso per arrivare al nodo
     * @return path del nodo e dei sottorami
     */
    public List<String> visitSubTree(String parentPath) {
        List<String> ret = new ArrayList<>();
        final String pp = (parentPath != null) ? (parentPath.length() == 0 ? nodeName : parentPath + ">" + nodeName) : "";
        if (parentPath != null) {
            ret.add(pp);
        }
        if (hasChildren()) {
            children.values().stream().forEach((nd) -> {
                ret.addAll(nd.visitSubTree(pp));
            });
        }
        return ret;
    }

    /**
     * Controlla se il percorso esiste
     *
     * @since 1.6
     * @param cp percorso
     * @return true se esiste
     */
    public boolean verifyPath(ClassificationPath cp) {
        String[] nodes = cp.getPath();
        return verifyPath(nodes, 0);
    }

    /**
     * Verifica se un path è presente nel grafo
     *
     * @param nodes path
     * @param level livello di partenza
     * @return true se il percorso è verificato
     */
    protected boolean verifyPath(String[] nodes, int level) {
        NodeData child = null;
        if (nodes.length <= level) {
            return false;
        }
        String value = nodes[level];
        if (value != null) {
            child = (NodeData) children.get(value);
            if (child == null) {
                return false;
            }
            return child.verifyPath(nodes, level + 1);
        }
        return true;

    }

    /**
     * Ritorna una struttura di classificazione a partire da una lista di righe
     * dove la struttura è contenuta con i nomi dei nodi separati da tabulatore
     * E' utilizzata per ricostruire la struttura a partire da un file csv
     * ottenuto esportando la struttura dal gui editor
     *
     * @param rows righe del file csv
     * @param intern internizzatore
     * @return root della struttura
     */
    public static NodeData getNodeData(List<String> rows, InternPool intern) {
        NodeData root = new NodeData(1, intern);
        Set<String> categories = new HashSet<>();
        for (String row : rows) {
            String[] doc = row.split("\t");
            if (doc.length > 0) {
                String level1 = (String) intern.intern(doc[0]);
                if (level1 != null) {
                    if (!categories.contains(level1)) { //Nuova categoria di livello 1
                        addNode(root, categories, level1, intern);
                    }
                    if (doc.length > 1) {
                        String level2 = (String) intern.intern(doc[1]);
                        if (!categories.contains(level2)) { //Nuova categoria di livello 2
                            NodeData parent = (NodeData) root.getNode(level1);
                            addNode(parent, categories, level2, intern);
                        }
                        if (doc.length > 2) {
                            String level3 = (String) intern.intern(doc[2]);
                            if (!categories.contains(level3)) { //Nuova categoria di livello 3
                                NodeData p1 = (NodeData) root.getNode(level1);
                                if (p1 != null) {
                                    NodeData p2 = (NodeData) p1.getNode(level2);
                                    addNode(p2, categories, level3, intern);
                                }
                            }
                            if (doc.length > 3) {
                                String level4 = (String) intern.intern(doc[3]);
                                if (!categories.contains(level4)) { //Nuova categoria di livello 4
                                    NodeData p1 = (NodeData) root.getNode(level1);
                                    if (p1 != null) {
                                        NodeData p2 = (NodeData) p1.getNode(level2);
                                        if (p2 != null) {
                                            NodeData p3 = (NodeData) p2.getNode(level3);
                                            addNode(p3, categories, level4, intern);
                                        }
                                    }
                                }
                                if (doc.length > 4) {
                                    String level5 = (String) intern.intern(doc[4]);
                                    if (!categories.contains(level5)) { //Nuova categoria di livello 5
                                        NodeData p1 = (NodeData) root.getNode(level1);
                                        if (p1 != null) {
                                            NodeData p2 = (NodeData) p1.getNode(level2);
                                            if (p2 != null) {
                                                NodeData p3 = (NodeData) p2.getNode(level3);
                                                if (p3 != null) {
                                                    NodeData p4 = (NodeData) p3.getNode(level4);
                                                    addNode(p4, categories, level5, intern);
                                                }
                                            }
                                        }
                                    }
                                    if (doc.length > 5) {
                                        String level6 = (String) intern.intern(doc[5]);
                                        if (!categories.contains(level6)) { //Nuova categoria di livello 6
                                            NodeData p1 = (NodeData) root.getNode(level1);
                                            if (p1 != null) {
                                                NodeData p2 = (NodeData) p1.getNode(level2);
                                                if (p2 != null) {
                                                    NodeData p3 = (NodeData) p2.getNode(level3);
                                                    if (p3 != null) {
                                                        NodeData p4 = (NodeData) p3.getNode(level4);
                                                        if (p4 != null) {
                                                            NodeData p5 = (NodeData) p4.getNode(level5);
                                                            addNode(p5, categories, level6, intern);
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

    private static void addNode(NodeData parent, Set<String> cats, String name, InternPool intern) {
        if (parent != null) {
            if (name.trim().length() > 0) {
                NodeData node = (NodeData) parent.getNode((String) intern.intern(name));
                if (node == null) {
                    try {
                        node = new NodeData((String) intern.intern(name), parent, intern);
                    } catch (Exception exception) {
                        LogGui.printException(exception);
                    }
                }
            }
            cats.add((String) intern.intern(name));
        }
    }

}
