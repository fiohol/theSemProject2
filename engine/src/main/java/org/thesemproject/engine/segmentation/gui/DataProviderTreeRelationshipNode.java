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
package org.thesemproject.engine.segmentation.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jdom2.Element;

/**
 *
 * Gestisce il nodo di relazione del dataprovider
 */
public class DataProviderTreeRelationshipNode extends ModelTreeNode {

    private String segmentName; //Segmento su cui è attivo

    private Map<String, Map<String, String>> mapping;
    private Map<String, String> fields;
    boolean priority;

    /**
     * inizializza il nodo
     *
     * @param nodeName nome della relazione
     * @param father dataprovider
     */
    public DataProviderTreeRelationshipNode(String nodeName, DataProviderTreeNode father) {
        super(nodeName, TYPE_DATA_PROVIDER_RELATIONSHIP);
        segmentName = "";
        mapping = new HashMap<>();
        fields = father.getFields();
        fields.keySet().stream().forEach((fieldName) -> {
            setMapping(fieldName, "", false, false);
        });
        priority = false;
    }

    /**
     * Imposta il mapping dei fields
     *
     * @param field field
     * @param capture nome della cattura
     * @param key true se il field è chiave
     * @param toImport true se il field è da importare in caso di match
     */
    public void setMapping(String field, String capture, boolean key, boolean toImport) {
        if (fields.containsKey(field)) {
            Map<String, String> values = new HashMap<>();
            values.put("capture", capture);
            values.put("key", String.valueOf(key));
            values.put("import", String.valueOf(toImport));
            mapping.put(field, values);
        }
    }

    /**
     * Ritorna le righe di mapping
     *
     * @param father nodo
     * @return lista delle righe per popolare la JTable
     */
    public List<Object[]> getMappingRows(DataProviderTreeNode father) {
        List<Object[]> ret = new ArrayList();
        fields = father.getFields();
        for (String field : mapping.keySet()) {
            if (fields.containsKey(field)) {
                Object[] line = new Object[4];
                line[0] = field;
                String capture = mapping.get(field).get("capture");
                if (capture == null) {
                    capture = "";
                }
                line[1] = capture;
                line[2] = "true".equalsIgnoreCase(mapping.get(field).get("key"));
                line[3] = "true".equalsIgnoreCase(mapping.get(field).get("import"));
                ret.add(line);
            }
        }
        return ret;
    }

    /**
     * Ritorna il nome del segmento
     *
     * @return nome del segmento
     */
    public String getSegmentName() {
        return segmentName;
    }

    /**
     * Imposta il segment dove deve agire l'arricchimento
     *
     * @param segmentName nome del segmento
     */
    public void setSegmentName(String segmentName) {
        if (segmentName == null) {
            return;
        }
        if (segmentName.trim().length() == 0) {
            return;
        }
        if (!segmentName.equals(this.segmentName)) {
            this.segmentName = segmentName;
            fields.keySet().stream().forEach((fieldName) -> {
                setMapping(fieldName, "", false, false);
            });
        }
    }

    /**
     * Ritorna la rappresentazione XML del componente
     *
     * @return Elemento per il JDOM
     */
    public Element getXmlElement() {
        Element segment = new Element("dpr");
        segment.setAttribute("n", getNodeName());
        segment.setAttribute("p", String.valueOf(priority));
        segment.setAttribute("s", segmentName);

        mapping.keySet().stream().map((field) -> {
            Element f = new Element("f");
            f.setAttribute("n", field);
            f.setAttribute("c", mapping.get(field).get("capture"));
            f.setAttribute("k", mapping.get(field).get("key"));
            f.setAttribute("i", mapping.get(field).get("import"));
            return f;
        }).forEach((f) -> {
            segment.addContent(f);
        });
        return segment;
    }

    /**
     * Verifica se l'arricchimento ha priorità sulla cattura
     *
     * @return true in caso di priorità
     */
    public boolean hasPriority() {
        return priority;
    }

    /**
     * Imposta la priorità
     *
     * @param priority true se ha priorità
     */
    public void setPriority(boolean priority) {
        this.priority = priority;
    }

}
